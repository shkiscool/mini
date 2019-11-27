package com.dazhao.pojo.dao;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;

@Data
@Table(name = "notification_condition")
@ApiModel(description = "到期提醒销毁条件对象")
public class NotificationCondition {

    /**
     * id
     */
    @ApiModelProperty(notes = "id")
    @Id
    @GeneratedValue(generator = "JDBC")
    @Column(name = "id")
    private Integer id;

    /**
     * 存在天数
     */
    @Column(name = "live_day")
    @ApiModelProperty(notes = "存在天数")
    private Integer liveDay;

    /**
     * 数量
     */
    @Column(name = "count")
    @ApiModelProperty(notes = "数量")
    private Integer count;
    /**
     * 备注
     */
    @Column(name = "comment")
    @ApiModelProperty(notes = "备注")
    private String comment;
    /**
     * 更新时间
     */
    @Column(name = "update_time")
    @ApiModelProperty(notes = "更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
    private Date updateTime;

}
