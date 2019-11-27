package com.dazhao.pojo.bo;

import lombok.Data;

/**
 * 日期物资统计对象
 */
@Data
public class DateStatisticsMaterialBO {

    private String date;

    private Integer count = 0;

    public DateStatisticsMaterialBO(String date) {
        this.date = date;
    }
}
