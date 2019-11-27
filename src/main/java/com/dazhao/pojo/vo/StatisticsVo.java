package com.dazhao.pojo.vo;

import lombok.Data;

@Data
/**
 * 统计数据对象
 */
public class StatisticsVo {

    /**
     * 名称
     */
    private String item;
    /**
     * 数量
     */
    private Integer count;
    /**
     * 占比
     */
    private String percent;
    /**
     * 时间
     */
    private String time;
}
