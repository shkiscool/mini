package com.dazhao.service.impl;

import com.dazhao.pojo.dao.NotificationCondition;
import com.dazhao.pojo.mapper.NotificationMapper;
import com.dazhao.service.NotificationService;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private NotificationMapper notificationMapper;

    private static final Integer REMIND_CONDITION_ID = 1;
    private static final Integer AFFECT_NUMBER = 0;


    @Override
    public NotificationCondition getNotificationsCondition() {
        return notificationMapper.selectByPrimaryKey(REMIND_CONDITION_ID);
    }

    @Override
    public Boolean updateNotificationsCondition(NotificationCondition notificationCondition) {
        notificationCondition.setId(REMIND_CONDITION_ID);
        notificationCondition.setUpdateTime(new Date());
        return notificationMapper.updateByPrimaryKeySelective(notificationCondition) > AFFECT_NUMBER;
    }
}
