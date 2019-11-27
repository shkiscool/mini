package com.dazhao.common;

public enum OperationLogEnum {

    BILL_IMAGE_UPLOAD("上传凭证", "上传凭证 单据号"),
    BILL_IMAGE_DELETE("删除凭证", "删除凭证 单据号"),
    INBOUND_PRINT_RFID("入库-一般入库", "打印RFID 入库单"),
    INBOUND_BILL_FINSH("入库-一般入库", "新增入库单"),
    RESTORES_MATERIAL("入库-借阅归还", "归还借阅物资 出库单"),
    OUTBOUND_BILL_BORROW("出库-借阅", "完成借阅 出库单"),
    OUTBOUND_BILL_TRANSFER("出库-移交", "完成移交 出库单"),
    OUTBOUND_BILL_DESTORY("出库-销毁", "完成销毁 出库单"),
    UPDATE_LOCATION("修改仓位", "完成修改"),
    STOCKTAKING_RECORD("盘点", "完成盘点"),
    CREATE_USER("人员设置", "新增人员"),
    UPDATE_USER("人员设置", "修改用户信息"),
    LOGIN_LOG("登录", "登录成功"),
    USER_DELETE("人员设置", "删除人员 "),
    ADD_SORT("物资类别", "新增 "),
    DELETE_SORT("物资类别", "删除 "),
    ADD_SOURCE("入库源", "新增 "),
    DELETE_SOURCE("入库源", "删除 "),
    DELETE_LOCATION("仓位管理", "删除仓位"),
    ADD_LOCATION("仓位管理", "添加仓位"),
    STOCKTAKING_IMAGE_UPLOAD("盘点凭证", "上传凭证单 盘点ID"),
    STOCKTAKING_IMAGE_DELETE("盘点凭证", "删除凭证单 盘点ID");
    private String moduleName;
    private String content;

    public String getModuleName() {
        return moduleName;
    }

    public String getContent() {
        return content;
    }

    OperationLogEnum(String moduleName, String content) {
        this.moduleName = moduleName;
        this.content = content;
    }
}
