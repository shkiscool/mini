package com.dazhao.service.impl;

import com.dazhao.common.OperationLogEnum;
import com.dazhao.pojo.dao.MaterialSort;
import com.dazhao.pojo.dao.MaterialSource;
import com.dazhao.pojo.dao.OperationLog;
import com.dazhao.pojo.mapper.MaterialSortMapper;
import com.dazhao.pojo.mapper.OperationLogMapper;
import com.dazhao.service.MaterialSortService;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

@Service
public class MaterialSortServiceImpl implements MaterialSortService {

    @Autowired
    private MaterialSortMapper materialSortMapper;

    @Autowired
    private OperationLogMapper operationLogMapper;

    @Override
    public List<MaterialSort> queryMaterialSortList() {
        return materialSortMapper.selectAll();
    }

    @Override
    @Transactional
    public void addMaterialSort(MaterialSort materialSort, Integer userId, String ipAddress) {
        Date date = new Date();
        materialSort.setCreateTime(date);
        materialSort.setUpdateTime(date);
        materialSortMapper.insertSelective(materialSort);
        OperationLog operationLog = new OperationLog(userId, ipAddress, OperationLogEnum.ADD_SORT.getModuleName(),
                OperationLogEnum.ADD_SORT.getContent() + materialSort.getSortName(), new Date());
        operationLogMapper.insert(operationLog);

    }

    @Override
    @Transactional
    public void deleteMaterialSortById(Integer materialSortId, Integer userId, String ipAddress) {
        MaterialSort materialSort = materialSortMapper.selectByPrimaryKey(materialSortId);
        materialSortMapper.deleteByPrimaryKey(materialSortId);
        OperationLog operationLog = new OperationLog(userId, ipAddress, OperationLogEnum.DELETE_SORT.getModuleName(),
                OperationLogEnum.DELETE_SORT.getContent() + materialSort.getSortName(), new Date());
        operationLogMapper.insert(operationLog);

    }

    @Override
    public boolean isUseMaterialSort(Integer materialSortId) {
        int total = materialSortMapper.isUseMaterialSort(materialSortId);
        return total == 0;
    }

    @Override
    public boolean isExistMaterialSortById(Integer sortId) {
        return materialSortMapper.selectByPrimaryKey(sortId) != null ? true : false;
    }

    @Override
    public boolean isExistMaterialSortByName(String sortName) {
        Example example = new Example(MaterialSort.class);
        Criteria criteria = example.createCriteria();
        criteria.andEqualTo("sortName", sortName.trim());
        return materialSortMapper.selectCountByExample(example) > 0;
    }
}
