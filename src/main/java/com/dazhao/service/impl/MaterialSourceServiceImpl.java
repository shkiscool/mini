package com.dazhao.service.impl;

import com.dazhao.common.OperationLogEnum;
import com.dazhao.common.Page;
import com.dazhao.pojo.dao.Bill;
import com.dazhao.pojo.dao.MaterialSource;
import com.dazhao.pojo.dao.OperationLog;
import com.dazhao.pojo.mapper.BillMapper;
import com.dazhao.pojo.mapper.MaterialSourceMapper;
import com.dazhao.pojo.mapper.OperationLogMapper;
import com.dazhao.service.MaterialSourceService;
import com.github.pagehelper.PageHelper;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

@Service
public class MaterialSourceServiceImpl implements MaterialSourceService {

    @Autowired
    private MaterialSourceMapper materialSourceMapper;
    @Autowired
    private OperationLogMapper operationLogMapper;

    @Autowired
    private BillMapper billMapper;

    /**
     * 新增入库源
     */
    @Override
    @Transactional
    public void insertSource(MaterialSource materialSource, Integer userId, String ipAddress) {
        materialSourceMapper.insert(materialSource);
        OperationLog operationLog = new OperationLog(userId, ipAddress, OperationLogEnum.ADD_SOURCE.getModuleName(),
                OperationLogEnum.ADD_SOURCE.getContent() + materialSource.getSourceName(), new Date());
        operationLogMapper.insert(operationLog);
    }

    /**
     * 根据id删除入库源
     */
    @Override
    public boolean deleteSourceById(Integer id, Integer userId, String ipAddress) {
        //判断该入库源是否再用
        Example billExample = new Example(Bill.class);
        billExample.and().andEqualTo("materialSourceId", id);
        int billCount = billMapper.selectCountByExample(billExample);
        if (billCount > 0) {
            return false;
        }
        MaterialSource materialSource = materialSourceMapper.selectByPrimaryKey(id);
        materialSourceMapper.updateByPrimaryKeySelective(MaterialSource.builder().id(id).deleted(1).build());
        OperationLog operationLog = new OperationLog(userId, ipAddress, OperationLogEnum.DELETE_SOURCE.getModuleName(),
                OperationLogEnum.DELETE_SOURCE.getContent() + materialSource.getSourceName(), new Date());
        operationLogMapper.insert(operationLog);
        return true;
    }

    /**
     * 查询入库源列表
     */
    @Override
    public List<MaterialSource> listSources(Page page) {
        Example materialSourceExample = new Example(MaterialSource.class);
        materialSourceExample.and().andEqualTo("deleted", 0);
        return PageHelper.startPage(page).doSelectPage(() -> materialSourceMapper.selectByExample(materialSourceExample));
    }

    /**
     * 修改入库源
     */
    @Override
    public void updateSource(MaterialSource materialSource) {
        materialSourceMapper.updateByPrimaryKeySelective(materialSource);
    }

    @Override
    public boolean isExistMaterialSourceById(Integer soourceId) {
        return materialSourceMapper.selectByPrimaryKey(soourceId) != null ? true : false;
    }

    @Override
    public Boolean isExistUserMaterialSource(Integer id) {
        return materialSourceMapper.isExistUserMaterialSource(id) > 0;
    }
}
