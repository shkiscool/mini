package com.dazhao.service;

import com.dazhao.common.Page;
import com.dazhao.common.result.CommonResult;
import com.dazhao.pojo.bo.InboundBillAndMaterialGroupBO;
import com.dazhao.pojo.bo.InboundBillQueryParameterBO;
import com.dazhao.pojo.bo.OutBillBO;
import com.dazhao.pojo.dao.Bill;
import com.dazhao.pojo.dao.Material;
import com.dazhao.pojo.vo.MaterialGroupVO;
import java.util.List;

public interface BillService {

    /**
     * 获取单据列表
     *
     * @param page 分页对象
     * @return 单据列表
     */
    List<Bill> listBills(Page page, String voucherNumber, String handOverName, String receivedDepartment, String beginTime, String endTime,
            Integer billStatus,
            Integer billType);

    /**
     * 创建入库单据和物资
     *
     * @param inboundBillAndMaterialGroupBo 单据信息
     * @param userId 用户id
     * @param ipAdrress 请求ip
     */
    Bill createInboundBillAndMaterials(InboundBillAndMaterialGroupBO inboundBillAndMaterialGroupBo, Integer userId, String ipAdrress);

    /**
     * 查询所有/条件查询所有入库单
     *
     * @param page 分页信息
     * @param inboundBillQueryParameterBo 条件查询对象
     */
    List<Bill> queryInboundBillList(Page page, InboundBillQueryParameterBO inboundBillQueryParameterBo);

    /**
     * 添加单据的入库凭证单图片路径
     */
    CommonResult updateBillUploadPath(Bill bill, Integer userId, String ipAddress);

    /**
     * 根据单据id查询单据下的所有物资
     *
     * @param id 单据id
     * @return 物资集合
     */
    List<Material> queryMaterialListByInboundBillId(Integer id);


    /**
     * 提交出库订单 1：查询所有物资是否都存在 2：增加一条借阅单信息 3：修改material表的物资状态为借阅 4：将借阅物资保存在物资追溯表中作为记录
     *
     * @param materialNumbers 物资编号集合
     * @param receivedDepartment 接收单位
     * @param handOverName 移交人
     * @param billType 单据类型
     * @return 0：失败（传入数据不符合），1：成功
     */
    int insertBorrowOrTransferBill(List<String> materialNumbers, String receivedDepartment, String handOverName, Integer billType);

    /**
     * 根据入库单据id查询入库单据详情
     *
     * @param id 入库单id
     * @return 返回入库单详情
     */
    Bill queryInboundBillDetail(Integer id);

    /**
     * 查询入库单下面的物资返回分组信息
     *
     * @param id 入库单据id
     * @param page 分页对象
     * @return 物资分组信息
     */
    List<MaterialGroupVO> queryInboundBillMaterialGroup(Integer id, Page page);

    /**
     * 根据凭证单查询单据信息
     *
     * @param voucherNumber 凭证单号
     * @return 单据信息
     */
    Bill queryBillByVoucherNumber(String voucherNumber);

    /**
     * 删除单据凭证单的图片地址
     *
     * @param bill 单据对象
     * @param userId 用户id
     * @param ipAddress ip地址
     * @return 是否成功
     */
    boolean deleteBillUploadPath(Bill bill, Integer userId, String ipAddress);

    /**
     * 创建出库单据和物资追溯信息
     *
     * @param userId 用户id
     * @param ipAddress ip地址
     * @param outBillBO 创建信息
     */
    Boolean insertBillAndMaterialHistory(OutBillBO outBillBO, Integer userId, String ipAddress);

    /**
     * 根据物资id批量更新物资打印rfid的状态为已打印状态
     *
     * @param billId 单据id
     * @param userId 用户id
     * @param ipAddress ip地址
     */
    Boolean updateConsummationBillStatus(Integer billId, Integer userId, String ipAddress);

    /**
     * 更新单据签名
     *
     * @param signatureHash 签名hash值
     * @param signaturePicturePath 签名hash值
     * @param billId 单据id
     */
    void uploadBillSignature(String signatureHash, String signaturePicturePath, Integer billId);

    /**
     * 需要签名的单据
     *
     * @return 单据集合
     */
    List<Bill> needSignatureBill();

}

