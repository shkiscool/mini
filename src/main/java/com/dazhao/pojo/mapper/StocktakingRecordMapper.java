package com.dazhao.pojo.mapper;

import com.dazhao.pojo.dao.StocktakingRecord;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

public interface StocktakingRecordMapper extends Mapper<StocktakingRecord>, MySqlMapper<StocktakingRecord> {

    /**
     * 查询盘点记录
     *
     * @param checkName 检查人
     * @param beginTime 检查时间
     * @param endTime 检查结束时间
     */
    List<StocktakingRecord> queryStocktakingRecord(@Param(value = "checkName") String checkName, @Param(value = "beginTime") String beginTime,
            @Param(value = "endTime") String endTime);

    /**
     * 根据盘点记录id查询盘点详情
     *
     * @param id 记录id
     * @return 详情对象
     */
    StocktakingRecord queryStocktakingRecordById(Integer id);

    /**
     * 查询最后盘点的记录
     *
     * @return 盘点记录对象
     */
    StocktakingRecord queryRecentStoktaingRecord();

    /**
     * 根据盘点id查询统计该次盘点有多少问题数据
     *
     * @param id 盘点记录id
     * @return 问题总数
     */
    int queryStocktakingProblemByRecordId(Integer id);

    /**
     * 更新盘点签名信息
     *
     * @param signatureHash 签名哈希值
     * @param signaturePicturePath 签名图片地址
     * @param billId 盘点id
     */
    void uploadStocktakingSignature(@Param("signatureHash") String signatureHash,
            @Param("signaturePicturePath") String signaturePicturePath, @Param("billId") Integer billId);
}
