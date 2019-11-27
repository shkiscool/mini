package com.dazhao.service;

import com.dazhao.common.Page;
import com.dazhao.pojo.bo.MaterialGroupParameterBO;
import com.dazhao.pojo.dao.Bill;
import com.dazhao.pojo.dao.Material;
import com.dazhao.pojo.vo.BorrowMaterialVO;
import com.dazhao.pojo.vo.MaterialGroupAndMateialsVO;
import java.util.List;

public interface RestoresService {

    /**
     * 查询借阅单据状态为借阅中的所有单据
     *
     * @param page 分页对象
     * @return 借阅单据集合
     */
    List<Bill> queryBillByBorrowStatus(Page page);

    /**
     * 根据借阅单id查询该id下的物资分组信息
     *
     * @param billId 借阅单据id
     * @return 物资集合
     */
    List<MaterialGroupAndMateialsVO> queryBorrowBillMaterialGroupInformation(Integer billId);


    /**
     * 物资id是否属于该借阅单物资并且物资状态是否为借阅
     *
     * @param billId 单据id
     * @param materialList 物资id集合
     * @return 查询的总数
     */
    int borrowMaterialExists(Integer billId, List<Integer> materialList);

    /**
     * 添加物资归还历史记录并修改物资和单据状态
     *
     * @param billId 借阅单据id
     * @param userId 用户id
     * @param ipAddress ip地址
     * @param materialIdList 物资id集合
     */
    void restoreMaterial(Integer billId, List<Integer> materialIdList, Integer userId, String ipAddress);
}
