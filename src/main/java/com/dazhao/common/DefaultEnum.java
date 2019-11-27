package com.dazhao.common;

import javax.xml.bind.annotation.XmlType.DEFAULT;

public enum DefaultEnum {
    DEFAULT_DELETED_FALSE(0, "默认为未删除0，删除为1"),

    DEFAULT_DELETED_TRUE(1, "默认为未删除0，删除为1"),

    DEFAULT_UPLOAD_VOUCHER_FALSE(0, "默认为未上传凭证0，上传为1"),

    DEFAULT_UPLOAD_VOUCHER_TRUE(1, "默认为未上传凭证0，上传为1"),

    DEFAULT_IS_PRINT_RFID_FALSE(0, "默认为未打印0，打印为1"),

    DEFAULT_MATERIAL_MAX(9999, "物资数量最大9999"),

    DEFAULT_IS_PRINT_RFID_TRUE(1, "默认为未打印0，打印为1"),

    ROLE_SUPER_ADMIN(1, "超级管理员"),

    ROLE_ADMIN(2, "管理员");

    private Integer defaultValue;
    private String defaultDesc;

    public Integer getDefaultValue() {
        return defaultValue;
    }

    public String getDefaultDesc() {
        return defaultDesc;
    }

    DefaultEnum(Integer defaultValue, String defaultDesc) {
        this.defaultValue = defaultValue;
        this.defaultDesc = defaultDesc;
    }
}
