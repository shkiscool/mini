package com.dazhao.common;

public enum BillEnum {
    BILL_TYPE_INBOUND(1, "入库"),

    BILL_TYPE_BORROW(2, "借阅"),

    BILL_TYPE_TRANSFER(3, "移交"),

    BILL_TYPE_DESTORY(4, "销毁"),

    BILL_STATUS_BORROW(1, "借阅中"),

    BILL_STATUS_HOMING(2, "已归还"),

    BILL_STATUS_LEAVING(3, "待出库"),

    BILL_STATUS_FINISH(4, "已完成");


    private Integer value;
    private String desc;

    public Integer getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }


    BillEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }
}
