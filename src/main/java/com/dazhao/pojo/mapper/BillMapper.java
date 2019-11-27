package com.dazhao.pojo.mapper;

import com.dazhao.pojo.dao.Bill;
import com.dazhao.pojo.bo.InboundBillQueryParameterBO;
import com.dazhao.pojo.dao.Material;
import com.dazhao.pojo.vo.BillAndMaterialVO;
import com.dazhao.pojo.vo.MaterialGroupAndMateialsVO;
import com.dazhao.pojo.vo.MaterialGroupVO;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

public interface BillMapper extends Mapper<Bill>, MySqlMapper<Bill> {

    /**
     * 查询所有/条件查询所有入库单
     */
    List<Bill> queryInboundBillList(InboundBillQueryParameterBO inboundBillQueryParameterBo);

    /**
     * 根据单据id查询单据下的所有物资
     */
    List<Material> queryMaterialListByInboundBillId(Integer id);


    Map<String, Object> queryInboundBillDetail(Integer id);

    List<Bill> listBills(@Param("voucherNumber") String voucherNumber, @Param("handOverName") String handOverName,
            @Param("receivedDepartment") String receivedDepartment, @Param("beginTime") String beginTime, @Param("endTime") String endTime,
            @Param("billStatus") Integer billStatus, @Param("billType") Integer billType);

    /**
     * 根据入库单据id查询入库单据详情
     */
    Bill queryInboundBillById(Integer id);

    /**
     * 根据入库单id查询入库单下面的物资返回分组信息
     */
    List<MaterialGroupVO> queryInboundMaterialGroup(Integer id);


    /**
     * 查询借阅单据状态为借阅中的所有单据
     */
    List<Bill> queryBillByBorrowStatus();

    /**
     * 根据借阅单id查询该id下的物资分组信息
     *
     * @param billId 借阅单据id
     * @return 物资集合
     */
    List<MaterialGroupAndMateialsVO> queryBorrowBillMaterialGroupInformation(Integer billId);

    /**
     * 查看借阅单下未归还的数量
     *
     * @param billId 单据id
     * @return 物资数量
     */
    int isRestoreMaterialAll(Integer billId);

    /**
     * 根据提醒条件查询到期单据信息
     *
     * @return 到期单据集合
     */
    List<BillAndMaterialVO> queryBillExpire();

    /**
     * 查询最近入库或最近出库的数据
     *
     * @param billType 1进 0 出
     * @return 单据
     */
    Bill queryRecentBillOperation(@Param("billType") int billType);

    /**
     * 跟新入库单据打印状态
     *
     * @param billId 单据id
     */
    int updateInboundBillPrintStatus(@Param("billId") Integer billId);

    /**
     * 入库单下的物资是否都打印了RFID
     *
     * @param billId 单据id
     * @return 未打印的条数
     */
    int isPrintAllByBillId(@Param("billId") Integer billId);

    /**
     * 根据物资id集合统计涉及到的单据下的物资存在的条数
     *
     * @param materialIdList 物资id集合
     */
    List<BillAndMaterialVO> countLiveMaterialOfBill(@Param("materialIdList") List<Integer> materialIdList);

    /**
     * 根据入库单id 逻辑删除入库单
     *
     * @param billId 单据id
     */
    void deleteInboundBillByBillId(@Param("inboundBillId") String billId);

    /**
     * 修改入库单是否同步法眼方法标志状态
     */
    void updateWetherSyncStatus(@Param("billId") Integer billId);

    /**
     * 更新单据签名
     *
     * @param signatureHash 签名hash值
     * @param billId 单据id
     */
    void uploadBillSignature(@Param("signatureHash") String signatureHash,
            @Param("signaturePicturePath") String signaturePicturePath, @Param("billId") Integer billId);

    /**
     * 获取需要签名的单据
     */
    List<Bill> needSignatureBill();
}
