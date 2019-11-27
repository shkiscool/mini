package com.dazhao.pojo.dao;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 入库源
 */
@Table(name = "bill")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "单据")
public class Bill {

    /**
     * 单据id
     */
    @ApiModelProperty(notes = "单据id")
    @Id
    @GeneratedValue(generator = "JDBC")
    @Column(name = "id")
    private Integer id;

    /**
     * 入库源id
     */
    @ApiModelProperty(notes = "入库源id")
    @Column(name = "material_source_id")
    private Integer materialSourceId;

    /**
     * 凭证单号
     */
    @ApiModelProperty(notes = "凭证单号")
    @Column(name = "voucher_number")
    private String voucherNumber;

    /**
     * 单据类型(1：入库、2：借阅、3：移交、4：销毁)
     */
    @ApiModelProperty(notes = "单据类型(1：入库、2：借阅、3：移交、4：销毁)")
    @Column(name = "bill_type")
    private Integer billType;

    /**
     * 案件编号（入库时有值）
     */
    @ApiModelProperty(notes = "案件编号（入库时有值）")
    @Column(name = "case_number")
    private String caseNumber;

    /**
     * 罚没对象名称（入库时有值）
     */
    @ApiModelProperty(notes = "罚没对象名称（入库时有值）")
    @Column(name = "confiscate_name")
    private String confiscateName;

    /**
     * 地址（入库时有值）
     */
    @ApiModelProperty(notes = "地址（入库时有值）")
    @Column(name = "address")
    private String address;

    /**
     * 送交人名称（入库时有值）
     */
    @ApiModelProperty(notes = "送交人名称（入库时有值）")
    @Column(name = "consigner_name")
    private String consignerName;

    /**
     * 送交单位（入库时有值）
     */
    @ApiModelProperty(notes = "送交单位（入库时有值）")
    @Column(name = "consigner_department")
    private String consignerDepartment;
    /**
     * 案由（入库时有值）
     */
    @ApiModelProperty(notes = "案由（入库时有值）")
    @Column(name = "reason")
    private String reason;

    /**
     * 处罚条款（入库时有值）
     */
    @ApiModelProperty(notes = "处罚条款（入库时有值）")
    @Column(name = "clause")
    private String clause;

    /**
     * 罚没日期日期（入库时有值）
     */
    @ApiModelProperty(notes = "罚没日期日期（入库时有值）")
    @Column(name = "confiscate_date")
    @JsonFormat(pattern = "yyyy-MM-dd", locale = "zh", timezone = "GMT+8")
    private Date confiscateDate;

    @ApiModelProperty(notes = "单据下的rfid是否都打印完成默认未完成0 完成1(入库需要)")
    @Column(name = "is_print_all_rfid")
    private Integer printAllRFID;

    /**
     * 接收单位（出库时有值）
     */
    @ApiModelProperty(notes = "接收单位（出库时有值）")
    @Column(name = "received_department")
    private String receivedDepartment;

    /**
     * 移交人（出库时有值）
     */
    @ApiModelProperty(notes = "移交人（出库时有值）")
    @Column(name = "hand_over_name")
    private String handOverName;

    /**
     * 单据物资状态(1：借阅中、2：已归还、3,：待出库、4：已完成（出库时有值）)
     */
    @ApiModelProperty(notes = "单据物资状态(1：借阅中、2：已归还、3,：待出库、4：已完成（出库时有值）)")
    @Column(name = "bill_status")
    private Integer billStatus;

    /**
     * 接收人名称
     */
    @ApiModelProperty(notes = "接收人名称")
    @Column(name = "recipient_name")
    private String recipientName;

    /**
     * 凭证照片路径
     */
    @ApiModelProperty(notes = "凭证照片路径")
    @Column(name = "voucher_picture_path")
    private String voucherPicturePath;

    /**
     * 是否上传凭证(默认0上传1)
     */
    @ApiModelProperty(notes = "是否上传凭证(默认0上传1)")
    @Column(name = "is_upload_voucher")
    private Integer uploadVoucher;

    /**
     * 出入库日期
     */
    @ApiModelProperty(notes = "出入库日期")
    @Column(name = "operation_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
    private Date operationTime;

    /**
     * 创建时间
     */
    @ApiModelProperty(notes = "创建时间")
    @Column(name = "create_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
    private Date createTime;

    /**
     * 到期时间
     */
    @ApiModelProperty(notes = "到期时间")
    @Column(name = "expire_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
    private Date expireTime;

    /**
     * 更新时间
     */
    @ApiModelProperty(notes = "更新时间")
    @Column(name = "update_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
    private Date updateTime;
    /**
     * 是否同步法眼 0未同步 1 同步
     */
    @ApiModelProperty(notes = "是否同步法眼 0未同步 1 同步")
    @Column(name = "wether_sync")
    private Integer wetherSync;

    /**
     * 逻辑删除
     */
    @ApiModelProperty(notes = "逻辑删除")
    @Column(name = "deleted")
    private Integer deleted;

    /**
     * 数字签名
     */
    @ApiModelProperty(notes = "数字签名")
    @Column(name = "signature")
    private String signature;

    /**
     * 数字签名标志
     */
    @ApiModelProperty(notes = "数字签名标志 0 签名 1 未签名")
    @Column(name = "signature_flag")
    private Integer signatureFlag;

    /**
     * 数字签名图片地址
     */
    @ApiModelProperty(notes = "数字签名图片地址")
    @Column(name = "signature_picture_path")
    private String signaturePicturePath;

    /**
     * 入库源名称(不是表字段)
     */
    @Transient
    private String sourceName;

}
