package com.dazhao.pojo.mapper;

import com.dazhao.pojo.bo.MaterialGroupParameterBO;
import com.dazhao.pojo.dao.Bill;
import com.dazhao.pojo.dao.Material;
import com.dazhao.pojo.dao.MaterialHistory;
import com.dazhao.pojo.vo.BorrowMaterialVO;
import com.dazhao.pojo.vo.MaterialHistoryInformationVO;
import com.dazhao.pojo.vo.MaterialHistoryVO;
import com.dazhao.pojo.vo.StatisticsVo;
import java.util.Date;
import java.util.List;
import javax.xml.crypto.Data;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

public interface MaterialHistoryMapper extends Mapper<MaterialHistory>, MySqlMapper<MaterialHistory> {

    /**
     * 获取物资分组详情 （借阅，移交，销毁）
     *
     * @param voucherNumber 凭证单号
     * @param operationType 操作类型(1：入库、2：借阅、3：移交、4：销毁、5：归还)
     * @return 物资分组列表
     */
    List<MaterialHistoryVO> listMaterialGroup(@Param("voucherNumber") String voucherNumber,
            @Param("operationType") Integer operationType);


    /**
     * 获取物资明细列表
     *
     * @param voucherNumber 凭证单号
     * @param materialName 没收实物名称
     * @param materialUnit 计量单位
     * @param materialType 型号
     * @param locationNumber 仓位编号
     * @param sortName 分类名称
     * @return 物资明细
     */
    List<MaterialHistoryVO> listMaterialDetails(@Param("voucherNumber") String voucherNumber, @Param("materialName") String materialName,
            @Param("materialUnit") String materialUnit, @Param("materialType") String materialType, @Param("locationNumber") String locationNumber,
            @Param("operationType") Integer operationType, @Param("materialComment") String materialComment, @Param("sortName") String sortName);


    /**
     * 批量增加物资追溯详情
     *
     * @param materialIds 物资id
     * @param billId 单据id
     * @param operationType 操作类型
     * @param createTime 创建日期
     */
    void insertMaterialHistorys(@Param("materialIds") List<Integer> materialIds, @Param("billId") Integer billId,
            @Param("operationType") Integer operationType, @Param("createTime") Date createTime);

    /**
     * 查询借阅单下面的物资分组详情
     *
     * @param materialGroupParameterBO 分组条件对象
     */
    List<BorrowMaterialVO> queryBorrowMaterialGroupDetailByConditon(MaterialGroupParameterBO materialGroupParameterBO);

    /**
     * 物资id是否属于该借阅单物资并且物资状态是否为借阅
     *
     * @param billId 单据id
     * @param materialList 物资id集合
     * @return 查询的总数
     */
    int borrowMaterialExists(@Param("billId") Integer billId, @Param("list") List<Integer> materialList);

    /**
     * 查询一个物资追溯历史
     *
     * @param materialId 物资id
     * @return 物资历史集合
     */
    List<MaterialHistoryInformationVO> queryMaterialHistoryById(Integer materialId);

    /**
     * 插入修改物资仓位的物资操作记录
     *
     * @param locationNumber 变更后的仓位id
     * @param materailList 变更物资集合
     * @param operationNameId 操作人id
     * @param createTime 创建时间
     */
    void insertUploadLocationMaterialHistorys(@Param("locationNumber") String locationNumber, @Param("materialList") List<Material> materailList,
            @Param("operationNameId") Integer operationNameId,
            @Param("createTime") Date createTime);

    /**
     * 根据时间单位天或月统计出库数量
     *
     * @param timeUnit 时间单位类型
     */
    List<StatisticsVo> outboundMaterialStatistics(@Param("timeUnit") Integer timeUnit);

    /**
     * 查询最近归还的物资信息
     *
     * @return 对应借阅单据
     */
    Bill queryRestoresOperation();
}
