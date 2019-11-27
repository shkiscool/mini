package com.dazhao.pojo.mapper;

import com.dazhao.pojo.dao.OperationLog;
import com.dazhao.pojo.dao.Permission;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

public interface OperationLogMapper extends Mapper<OperationLog>, MySqlMapper<OperationLog> {

    /**
     * 查询操作日志
     *
     * @param startTime 操作起始时间
     * @param endTime 操作结束时间
     * @return 操作日志集合
     */
    List<OperationLog> queryOperationLogList(@Param("startTime") String startTime, @Param("endTime") String endTime);


}
