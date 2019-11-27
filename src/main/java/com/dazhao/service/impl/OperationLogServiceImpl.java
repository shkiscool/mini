package com.dazhao.service.impl;

import com.dazhao.common.Page;
import com.dazhao.pojo.dao.OperationLog;
import com.dazhao.pojo.mapper.OperationLogMapper;
import com.dazhao.service.OperationLogService;
import com.github.pagehelper.PageHelper;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OperationLogServiceImpl implements OperationLogService {

    @Autowired
    private OperationLogMapper operationLogMapper;

    @Override
    public List<OperationLog> queryOperationLogList(Page page, String startTime, String endTime) {
        PageHelper.startPage(page.getPageNum(),page.getPageSize());
        return operationLogMapper.queryOperationLogList(startTime, endTime);
    }
}
