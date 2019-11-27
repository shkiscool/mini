package com.dazhao.common;

public enum MaterialStatuEnum {
    MATERIAL_INBOUND(1, "在库"),

    MATERIAL_BORROW(2, "借阅"),

    MATERIAL_TRANSFER(3, "移交"),

    MATERIAL_DESTORY(4, "销毁"),

    MATERIAL_FREEZE(5, "冻结"),

    MATERIAL_EXCEPTION(6, "异常");

    private Integer statuValue;
    private String statuDesc;

    public Integer getStatuValue() {
        return statuValue;
    }

    public String getStatuDesc() {
        return statuDesc;
    }


    MaterialStatuEnum(Integer statuValue, String statuDesc) {
        this.statuValue = statuValue;
        this.statuDesc = statuDesc;
    }

}
