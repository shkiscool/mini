package com.dazhao.service;

import com.dazhao.common.Page;
import com.dazhao.pojo.dao.OperationLog;
import java.util.List;

public interface OperationLogService {

    /**
     * 查询操作日志
     *
     * @param startTime 操作起始时间
     * @param endTime 操作结束时间
     * @return 操作日志集合
     */
    List<OperationLog> queryOperationLogList(Page page,String startTime, String endTime);
}
