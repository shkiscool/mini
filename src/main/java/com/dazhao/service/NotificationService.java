package com.dazhao.service;

import com.dazhao.pojo.dao.NotificationCondition;

public interface NotificationService {

    /**
     * 查询唯一的到期提醒条件对象
     *
     * @return 提醒条件对象
     */
    NotificationCondition getNotificationsCondition();

    /**
     * 更新唯一到期提醒数据
     */
    Boolean updateNotificationsCondition(NotificationCondition notificationCondition);
}
