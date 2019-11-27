package com.dazhao.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "分页对象")
public class Page {

    @ApiParam(value = "页码", defaultValue = "1")
    private int pageNum = 1;
    @ApiParam(value = "每页数量", defaultValue = "10")
    private int pageSize = 10;
    @ApiParam("排序(默认升序，降序加 desc eg: 根据时间排序传入参数 time desc) 字段+排序规则")
    private String orderBy;

}
