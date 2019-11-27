package com.dazhao.external.archive;

public interface WitnessCloudService {

    /**
     * 创建用户（使用out_id,也可以使用phome,email，具体查看接口文档）
     *
     * @param outId 创建的信息
     * @return id
     */
    String createUser(String outId);

    /**
     * 上传文件
     */
    void uploadFileExcel();
}
