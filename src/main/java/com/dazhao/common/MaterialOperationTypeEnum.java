package com.dazhao.common;

public enum MaterialOperationTypeEnum {
    MATERIAL_INBOUND(1, "入库"),

    MATERIAL_BORROW(2, "借阅"),

    MATERIAL_TRANSFER(3, "移交"),

    MATERIAL_DESTORY(4, "销毁"),

    MATERIAL_REVERT(5, "归还");

    public Integer getOperationValue() {
        return operationValue;
    }

    public String getOperrationDesc() {
        return operrationDesc;
    }

    private Integer operationValue;
    private String operrationDesc;

    MaterialOperationTypeEnum(Integer operationValue, String operrationDesc) {
        this.operationValue = operationValue;
        this.operrationDesc = operrationDesc;
    }
}
