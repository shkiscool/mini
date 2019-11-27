package com.dazhao.service.impl;

import com.dazhao.common.BillEnum;
import com.dazhao.common.DefaultEnum;
import com.dazhao.common.MaterialOperationTypeEnum;
import com.dazhao.common.MaterialStatuEnum;
import com.dazhao.common.OperationLogEnum;
import com.dazhao.common.Page;
import com.dazhao.common.result.CommonResult;
import com.dazhao.common.result.ResponseUtil;
import com.dazhao.common.utils.AccessNumberUtil;
import com.dazhao.pojo.bo.InboundBillAndMaterialGroupBO;
import com.dazhao.pojo.bo.InboundBillQueryParameterBO;
import com.dazhao.pojo.bo.OutBillBO;
import com.dazhao.pojo.dao.Bill;
import com.dazhao.pojo.dao.Material;
import com.dazhao.pojo.dao.MaterialSource;
import com.dazhao.pojo.dao.OperationLog;
import com.dazhao.pojo.mapper.BillAndMaterialMapper;
import com.dazhao.pojo.mapper.BillMapper;
import com.dazhao.pojo.mapper.MaterialHistoryMapper;
import com.dazhao.pojo.mapper.MaterialMapper;
import com.dazhao.pojo.mapper.MaterialSourceMapper;
import com.dazhao.pojo.mapper.OperationLogMapper;
import com.dazhao.pojo.vo.BillAndMaterialVO;
import com.dazhao.pojo.vo.MaterialGroupVO;
import com.dazhao.service.BillService;
import com.github.pagehelper.PageHelper;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BillServiceImpl implements BillService {

    private static final String PICTURE_PATH_SEPARATOR = "|";
    private static final String REPLACE_SYMBOL = "\\|";
    private static final Integer NO_MATERIAL_LIVE_NUMBER = 0;
    private static final Integer MAX_PICTURE_NUMBER = 10;
    /**
     * 无期限天数
     */
    private static final Integer NO_LIMIT_KEEP_DAY = 0;
    /**
     * 无限期到期日期
     */
    private static final String NO_LIMIT_KEEP_EXPIRE_TIME = "9999-12-12 23:59:59";

    @Autowired
    private BillMapper billMapper;

    @Autowired
    private MaterialMapper materialMapper;

    @Autowired
    private BillAndMaterialMapper billAndMaterialMapper;

    @Autowired
    private MaterialHistoryMapper materialHistoryMapper;

    @Autowired
    private OperationLogMapper operationLogMapper;
    @Autowired
    private MaterialSourceMapper materialSourceMapper;

    @Override
    public List<Bill> listBills(Page page, String voucherNumber, String handOverName, String receivedDepartment, String beginTime, String endTime,
            Integer billStatus, Integer billType) {
        return PageHelper.startPage(page).doSelectPage(() -> billMapper.listBills(voucherNumber, handOverName, receivedDepartment, beginTime, endTime,
                billStatus, billType));
    }

    @Override
    @Transactional
    public Bill createInboundBillAndMaterials(InboundBillAndMaterialGroupBO inboundBillAndMaterialGroupBo, Integer userId, String ipAddress) {
        DateTime dateTime = new DateTime();
        Bill bill = prepareBill(inboundBillAndMaterialGroupBo, dateTime);
        List<Material> materialList = prepareMaterialList(inboundBillAndMaterialGroupBo, dateTime);
        billMapper.insertSelective(bill);
        materialMapper.insertList(materialList);
        List<Integer> materialIdList = materialList.stream().map(Material::getId).collect(Collectors.toList());
        billAndMaterialMapper.insertBillAndMaterials(bill.getId(), materialIdList);
        OperationLog operationLog = new OperationLog(userId, ipAddress, OperationLogEnum.INBOUND_BILL_FINSH.getModuleName(),
                OperationLogEnum.INBOUND_BILL_FINSH.getContent() + bill.getVoucherNumber(), new Date());
        operationLogMapper.insert(operationLog);
        return bill;
    }

    @Override
    public List<Bill> queryInboundBillList(Page page, InboundBillQueryParameterBO inboundBillQueryParameterBo) {
        PageHelper.startPage(page.getPageNum(), page.getPageSize());
        return billMapper.queryInboundBillList(inboundBillQueryParameterBo);
    }

    @Override
    public CommonResult updateBillUploadPath(Bill bill, Integer userId, String ipAddress) {
        Bill resultBill = billMapper.selectByPrimaryKey(bill.getId());
        if (resultBill == null) {
            return ResponseUtil.badArgument();
        }
        bill.setUpdateTime(new Date());
        String voucherPicturePath = resultBill.getVoucherPicturePath();
        if (StringUtils.isEmpty(voucherPicturePath)) {
            bill.setUploadVoucher(1);
            billMapper.updateByPrimaryKeySelective(bill);
        } else {
            String[] picturePaths = voucherPicturePath.split(REPLACE_SYMBOL);
            if (picturePaths.length >= MAX_PICTURE_NUMBER) {
                return ResponseUtil.fail("图片上传不能超过10张");
            }
            bill.setVoucherPicturePath(voucherPicturePath + PICTURE_PATH_SEPARATOR + bill.getVoucherPicturePath());
            billMapper.updateByPrimaryKeySelective(bill);
        }
        OperationLog operationLog = new OperationLog(userId, ipAddress, OperationLogEnum.BILL_IMAGE_UPLOAD.getModuleName(),
                OperationLogEnum.BILL_IMAGE_UPLOAD.getContent() + resultBill.getVoucherNumber(), new Date());
        operationLogMapper.insert(operationLog);
        return ResponseUtil.ok("添加成功");
    }

    @Override
    public List<Material> queryMaterialListByInboundBillId(Integer id) {
        return billMapper.queryMaterialListByInboundBillId(id);
    }

    @Override
    public Bill queryInboundBillDetail(Integer id) {
        return billMapper.queryInboundBillById(id);
    }

    @Override
    public List<MaterialGroupVO> queryInboundBillMaterialGroup(Integer id, Page page) {
        PageHelper.startPage(page.getPageNum(), page.getPageSize());
        return billMapper.queryInboundMaterialGroup(id);
    }

    /**
     * 提交出库订单 1：查询所有物资是否都存在 2：增加一条借阅单信息 3：修改material表的物资状态为借阅 4：将借阅物资保存在物资追溯表中作为记录
     *
     * @param materialNumbers 物资编号集合
     * @param receivedDepartment 接收单位
     * @param handOverName 移交人
     * @param billType 单据类型
     * @return 0：失败（传入数据不符合），1：成功
     */
    @Transactional
    @Override
    public int insertBorrowOrTransferBill(List<String> materialNumbers, String receivedDepartment, String handOverName, Integer billType) {

        // 判读数据库中物资是否都存在
        List<Integer> materialIds = materialMapper.listMaterialIdByMaterialNumbers(materialNumbers);
        if (materialIds.size() != materialNumbers.size()) {
            return 0;
        }
        String voucherNumber = ""; //单据编号
        Integer billStatus = 0; //单据状态
        Integer materialStatus = 0; // 库存物资状态
        Integer operationType = 0; // 物资追溯中的操作类型
        if (billType.equals(BillEnum.BILL_TYPE_BORROW.getValue())) {
            voucherNumber = AccessNumberUtil.getBorrowNumber(); // 借阅单号
            billStatus = BillEnum.BILL_STATUS_BORROW.getValue();// 单据状态为借阅中
            materialStatus = MaterialStatuEnum.MATERIAL_BORROW.getStatuValue(); // 物资状态借阅
            operationType = MaterialOperationTypeEnum.MATERIAL_BORROW.getOperationValue(); //借阅类型
        } else if (billType.equals(BillEnum.BILL_TYPE_TRANSFER.getValue())) {
            voucherNumber = AccessNumberUtil.getTransferNumber(); // 移交单号
            billStatus = BillEnum.BILL_STATUS_FINISH.getValue();// 单据状态为已完成
            materialStatus = MaterialStatuEnum.MATERIAL_TRANSFER.getStatuValue(); // 物资状态移交
            operationType = MaterialOperationTypeEnum.MATERIAL_TRANSFER.getOperationValue(); //移交类型
        } else {
            return 0;
        }

        Bill bill = Bill.builder()
                .voucherNumber(voucherNumber)
                .billType(billType)
                .receivedDepartment(receivedDepartment)
                .handOverName(handOverName)
                .billStatus(billStatus)
                .uploadVoucher(DefaultEnum.DEFAULT_UPLOAD_VOUCHER_FALSE.getDefaultValue())// 是否上传凭证
                .operationTime(new Date())
                .createTime(new Date())
                .build();
        // 增加一条借阅或移交单信息
        billMapper.insertSelective(bill);
        // 修改material表的物资状态为借阅
        materialMapper.updateMaterialByMaterialNumbers(materialNumbers, materialStatus);

        //将借阅物资保存在物资追溯表中作为记录
        materialHistoryMapper
                .insertMaterialHistorys(materialIds, bill.getId(), operationType, new Date());
        return 1;
    }

    @Override
    public Bill queryBillByVoucherNumber(String voucherNumber) {
        return billMapper.selectOne(Bill.builder().voucherNumber(voucherNumber).build());
    }

    @Override
    public boolean deleteBillUploadPath(Bill bill, Integer userId, String ipAdrress) {
        Bill resultBill = billMapper.selectByPrimaryKey(bill.getId());
        bill.setUpdateTime(new Date());
        String voucherPicturePath = resultBill.getVoucherPicturePath();
        if (StringUtils.isEmpty(voucherPicturePath)) {
            return false;
        }
        String[] pathArr = voucherPicturePath.split(REPLACE_SYMBOL);
        List<String> pathList = new ArrayList<>(pathArr.length);
        Collections.addAll(pathList, pathArr);
        boolean isSuccess = pathList.removeIf(e -> e.equals(bill.getVoucherPicturePath()));
        if (!isSuccess) {
            return false;
        } else {
            String newVoucherPicturePath = String.join(PICTURE_PATH_SEPARATOR, pathList);
            bill.setVoucherPicturePath(newVoucherPicturePath);
            billMapper.updateByPrimaryKeySelective(bill);
            OperationLog operationLog = OperationLog.builder()
                    .userId(userId)
                    .ip(ipAdrress)
                    .module(OperationLogEnum.BILL_IMAGE_DELETE.getModuleName())
                    .content(OperationLogEnum.BILL_IMAGE_DELETE.getContent() + resultBill.getVoucherNumber())
                    .createTime(new Date()).build();
            operationLogMapper.insert(operationLog);
            return true;
        }
    }

    @Override
    @Transactional
    public Boolean insertBillAndMaterialHistory(OutBillBO outBillBO, Integer userId, String ipAddress) {
        int billType = 0;
        int billStatus = 0;
        int materialStatus = 0;
        int materialOperationType = 0;
        String voucherNumber = null;
        String moduleName = null;
        String content = null;
        switch (outBillBO.getBillType()) {
            case 2:
                billType = BillEnum.BILL_TYPE_BORROW.getValue();
                billStatus = BillEnum.BILL_STATUS_BORROW.getValue();
                materialStatus = MaterialStatuEnum.MATERIAL_BORROW.getStatuValue();
                materialOperationType = MaterialOperationTypeEnum.MATERIAL_BORROW.getOperationValue();
                voucherNumber = AccessNumberUtil.getBorrowNumber();
                moduleName = OperationLogEnum.OUTBOUND_BILL_BORROW.getModuleName();
                content = OperationLogEnum.OUTBOUND_BILL_BORROW.getContent();
                break;
            case 3:
                billType = BillEnum.BILL_TYPE_TRANSFER.getValue();
                billStatus = BillEnum.BILL_STATUS_FINISH.getValue();
                materialStatus = MaterialStatuEnum.MATERIAL_TRANSFER.getStatuValue();
                materialOperationType = MaterialOperationTypeEnum.MATERIAL_TRANSFER.getOperationValue();
                voucherNumber = AccessNumberUtil.getTransferNumber();
                moduleName = OperationLogEnum.OUTBOUND_BILL_TRANSFER.getModuleName();
                content = OperationLogEnum.OUTBOUND_BILL_TRANSFER.getContent();
                break;
            case 4:
                billType = BillEnum.BILL_TYPE_DESTORY.getValue();
                billStatus = BillEnum.BILL_STATUS_FINISH.getValue();
                materialStatus = MaterialStatuEnum.MATERIAL_DESTORY.getStatuValue();
                materialOperationType = MaterialOperationTypeEnum.MATERIAL_DESTORY.getOperationValue();
                voucherNumber = AccessNumberUtil.getDestoryNumber();
                moduleName = OperationLogEnum.OUTBOUND_BILL_DESTORY.getModuleName();
                content = OperationLogEnum.OUTBOUND_BILL_DESTORY.getContent();
                break;
            default:
                return false;

        }
        Date date = new Date();
        //构建表单对象插入
        Bill bill = Bill.builder().billType(billType).voucherNumber(voucherNumber)
                .billStatus(billStatus)
                .handOverName(outBillBO.getHandOverName())
                .receivedDepartment(outBillBO.getReceivedDepartment())
                .createTime(date).updateTime(date).build();
        billMapper.insertSelective(bill);
        //添加物资追溯
        materialHistoryMapper
                .insertMaterialHistorys(outBillBO.getMaterialIdList(), bill.getId(), materialOperationType,
                        date);
        //修改物资状态
        materialMapper.updateMaterialStatusByMaterialId(outBillBO.getMaterialIdList(), materialStatus,
                MaterialStatuEnum.MATERIAL_INBOUND.getStatuValue(), date);
        // 当进行移交和销毁时 入库单单据下的物资检查是否都为移交或销毁 是则逻辑删除入库单单据
        if (BillEnum.BILL_TYPE_DESTORY.getValue().equals(billType) || BillEnum.BILL_TYPE_TRANSFER.getValue().equals(billType)) {
            // 查询涉及单据id统计未删除的是否为0 单据id对应的未删除数量为0的话则修改单据状态deleted为1 为单据删除。
            List<BillAndMaterialVO> billAndMaterialVOS = billMapper.countLiveMaterialOfBill(outBillBO.getMaterialIdList());
            for (BillAndMaterialVO billAndMaterialVO : billAndMaterialVOS) {
                if (billAndMaterialVO.getCount().equals(NO_MATERIAL_LIVE_NUMBER)) {
                    billMapper.deleteInboundBillByBillId(billAndMaterialVO.getBillId());
                }
            }
        }
        OperationLog operationLog = new OperationLog(userId, ipAddress, moduleName,
                content + voucherNumber, new Date());
        operationLogMapper.insert(operationLog);
        return true;
    }

    @Override
    @Transactional
    public Boolean updateConsummationBillStatus(Integer billId, Integer userId, String ipAddress) {
        // 检查单据下的物资是否全部打印成功
        if (billMapper.isPrintAllByBillId(billId) > 0) {
            return false;
        }
        Bill bill = billMapper.selectByPrimaryKey(billId);
        if (bill.getBillType().equals(BillEnum.BILL_TYPE_INBOUND.getValue())) {
            billMapper.updateInboundBillPrintStatus(billId);
            OperationLog operationLog = OperationLog.builder()
                    .userId(userId)
                    .ip(ipAddress)
                    .module(OperationLogEnum.INBOUND_PRINT_RFID.getModuleName())
                    .content(OperationLogEnum.INBOUND_PRINT_RFID.getContent() + bill.getVoucherNumber())
                    .createTime(new Date()).build();
            operationLogMapper.insert(operationLog);
            return true;
        }
        return false;
    }

    @Override
    public void uploadBillSignature(String signatureHash, String signaturePicturePath, Integer billId) {
        billMapper.uploadBillSignature(signatureHash, signaturePicturePath, billId);
    }

    @Override
    public List<Bill> needSignatureBill() {
        return billMapper.needSignatureBill();
    }

    /**
     * 根据入库单生成入库物资信息List
     *
     * @param inboundBillAndMaterialGroupBo 入库单信息
     * @return materialList
     */
    private List<Material> prepareMaterialList(InboundBillAndMaterialGroupBO inboundBillAndMaterialGroupBo, DateTime date) {
        List<Material> materials = inboundBillAndMaterialGroupBo.getMaterialGroup();
        List<Material> materialList = new ArrayList();
        materials.forEach((material -> {
            for (int i = 0; i < material.getTotal(); i++) {
                Material mat = new Material();
                mat.setCreateTime(date.toDate());
                mat.setLocationId(material.getLocationId());
                mat.setMaterialName(material.getMaterialName());
                mat.setMaterialNumber(AccessNumberUtil.getMaterialNumber());
                mat.setMaterialStatus(MaterialStatuEnum.MATERIAL_INBOUND.getStatuValue());
                mat.setMaterialUnit(material.getMaterialUnit());
                if (StringUtils.isNotEmpty(material.getMaterialType())) {
                    material.setMaterialType(material.getMaterialType().trim());
                }
                mat.setMaterialType(
                        StringUtils.isEmpty(material.getMaterialType()) ? "-" : material.getMaterialType());
                if (StringUtils.isNotEmpty(material.getMaterialComment())) {
                    material.setMaterialComment(material.getMaterialComment().trim());
                }
                mat.setMaterialComment(
                        StringUtils.isEmpty(material.getMaterialComment()) ? "-" : material.getMaterialComment());
                mat.setUpdateTime(date.toDate());
                mat.setDeleted(DefaultEnum.DEFAULT_DELETED_FALSE.getDefaultValue());
                mat.setPrintRFID(DefaultEnum.DEFAULT_IS_PRINT_RFID_FALSE.getDefaultValue());
                mat.setSortId(material.getSortId());
                materialList.add(mat);
            }
        }));
        return materialList;
    }

    /**
     * 添加入库单一些其他的信息
     *
     * @param inboundBillAndMaterialGroupBo 入库单信息
     * @return bill
     */
    private Bill prepareBill(InboundBillAndMaterialGroupBO inboundBillAndMaterialGroupBo, DateTime date) {
        Bill bill = new Bill();
        bill.setMaterialSourceId(inboundBillAndMaterialGroupBo.getMaterialSourceId());
        bill.setCaseNumber(inboundBillAndMaterialGroupBo.getCaseNumber());
        bill.setConfiscateName(inboundBillAndMaterialGroupBo.getConfiscateName());
        bill.setAddress(inboundBillAndMaterialGroupBo.getAddress());
        bill.setConsignerName(inboundBillAndMaterialGroupBo.getConsignerName());
        bill.setConsignerDepartment(inboundBillAndMaterialGroupBo.getConsignerDepartment());
        bill.setRecipientName(inboundBillAndMaterialGroupBo.getRecipientName());
        bill.setConfiscateDate(inboundBillAndMaterialGroupBo.getConfiscateDate());
        bill.setReason(inboundBillAndMaterialGroupBo.getReason());
        bill.setClause(inboundBillAndMaterialGroupBo.getClause());
        bill.setBillType(BillEnum.BILL_TYPE_INBOUND.getValue());
        bill.setVoucherNumber(AccessNumberUtil.getInboundNumber());
        bill.setCreateTime(date.toDate());
        bill.setUpdateTime(date.toDate());
        bill.setVoucherPicturePath(StringUtils.isEmpty(inboundBillAndMaterialGroupBo.getVoucherPicturePath()) ? ""
                : inboundBillAndMaterialGroupBo.getVoucherPicturePath());
        bill.setUploadVoucher(StringUtils.isEmpty(inboundBillAndMaterialGroupBo.getVoucherPicturePath()) ? 0 : 1);
        bill.setDeleted(DefaultEnum.DEFAULT_DELETED_FALSE.getDefaultValue());
        bill.setPrintAllRFID(DefaultEnum.DEFAULT_IS_PRINT_RFID_FALSE.getDefaultValue());
        MaterialSource materialSource = materialSourceMapper.selectByPrimaryKey(inboundBillAndMaterialGroupBo.getMaterialSourceId());
        Integer keepDays = materialSource.getKeepDays();
        if (keepDays.equals(NO_MATERIAL_LIVE_NUMBER)) {
            bill.setExpireTime(DateTime.parse(NO_LIMIT_KEEP_EXPIRE_TIME, DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate());
        } else {
            bill.setExpireTime(date.plusDays(materialSource.getKeepDays()).toDate());
        }
        return bill;
    }
}
