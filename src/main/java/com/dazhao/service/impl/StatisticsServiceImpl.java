package com.dazhao.service.impl;

import com.dazhao.pojo.bo.DateStatisticsMaterialBO;
import com.dazhao.pojo.dao.Bill;
import com.dazhao.pojo.dao.Location;
import com.dazhao.pojo.dao.NotificationCondition;
import com.dazhao.pojo.dao.StocktakingRecord;
import com.dazhao.pojo.mapper.BillMapper;
import com.dazhao.pojo.mapper.LocationMapper;
import com.dazhao.pojo.mapper.MaterialHistoryMapper;
import com.dazhao.pojo.mapper.MaterialMapper;
import com.dazhao.pojo.mapper.NotificationMapper;
import com.dazhao.pojo.mapper.StocktakingRecordMapper;
import com.dazhao.pojo.vo.BillAndMaterialVO;
import com.dazhao.pojo.vo.StatisticsVo;
import com.dazhao.service.StatisticsService;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class StatisticsServiceImpl implements StatisticsService {

    @Autowired
    private MaterialMapper materialMapper;
    @Autowired
    private MaterialHistoryMapper materialHistoryMapper;
    @Autowired
    private LocationMapper locationMapper;
    @Autowired
    private BillMapper billMapper;
    @Autowired
    private NotificationMapper notificationMapper;
    @Autowired
    private StocktakingRecordMapper stocktakingRecordMapper;

    private static final String INBOUND_STATICS_KEY = "inboundStaticsData";
    private static final String OUTBOUND_STATICS_KEY = "outboundStaticsData";
    private static final String RECENT_INBOUND_BILL_KEY = "recentInboundBill";
    private static final String RECENT_OUTBOUND_BILL_KEY = "recentOutboundBill";
    private static final String RECENT_STOCKTAKING_RECORD_KEY = "recentStocktakingRecord";
    private static final Integer ONE_HUNDRED = 100;
    private static final Integer INBOUND_TYPE = 1;
    private static final Integer TIME_UNIT_DAY = 0;
    private static final Integer OUTBOUND_TYPE = 0;
    private static final String PERCENT = "%";
    private static final Integer EXPIRE_NOTIFICATION_CONDITION_ID = 1;
    private static final int ONE_YEAR = 1;
    private static final String DATE_FORMAT = "yyyy-MM-dd";

    @Override
    public List<StatisticsVo> querySourceProportion() {
        List<StatisticsVo> statisticsVos = materialMapper.listSourceProportion();
        return statisticsVos;

    }

    @Override
    public List<StatisticsVo> querySortProportion(Integer sourceId) {
        List<StatisticsVo> statisticsVos = materialMapper.listSortProportion(sourceId);
        return statisticsVos;

    }

    /**
     * 消除占比和不为100的误差
     *
     * @param statisticsVos 占比数据
     * @return 新的占比数据
     */
    private List<StatisticsVo> eliminateDeviationValue(List<StatisticsVo> statisticsVos) {
        int sum = 0;
        for (StatisticsVo statisticsVo : statisticsVos) {
            sum = sum + Integer.valueOf(statisticsVo.getPercent().replace(PERCENT, ""));
        }
        if (sum == ONE_HUNDRED) {
            return statisticsVos;
        }
        if (sum > ONE_HUNDRED) {
            int deviationValue = sum - ONE_HUNDRED;
            statisticsVos.get(0).setPercent((Integer.valueOf(statisticsVos.get(0).getPercent().replace(PERCENT, "")) - deviationValue) + PERCENT);
            return statisticsVos;
        } else {
            int deviationValue = ONE_HUNDRED - sum;
            statisticsVos.get(0).setPercent((Integer.valueOf(statisticsVos.get(0).getPercent().replace(PERCENT, "")) + deviationValue) + PERCENT);
            return statisticsVos;
        }
    }

    @Override
    public Map<String, List<DateStatisticsMaterialBO>> materialFluctuateTrend(Integer timeUnit) {
        Map<String, List<DateStatisticsMaterialBO>> result = new HashMap<>();
        DateTime nowTime = new DateTime();
        String beginTime = nowTime.toString(DATE_FORMAT);
        String endTime = nowTime.minusYears(ONE_YEAR).toString(DATE_FORMAT);

        List<DateStatisticsMaterialBO> inbountDateStatisticsMaterialBO = getDays(endTime, beginTime, timeUnit);
        List<StatisticsVo> inboundStaticsData = materialMapper.inboundMaterialStatistics(timeUnit);
        setDateStatisticsMaterialBOCount(inbountDateStatisticsMaterialBO, inboundStaticsData);

        List<StatisticsVo> outboundStaticsData = materialHistoryMapper.outboundMaterialStatistics(timeUnit);
        List<DateStatisticsMaterialBO> outboundDateStatisticsMaterialBO = getDays(endTime, beginTime, timeUnit);
        setDateStatisticsMaterialBOCount(outboundDateStatisticsMaterialBO, outboundStaticsData);

        result.put("INBOUND_STATICS_KEY", inbountDateStatisticsMaterialBO);
        result.put("OUTBOUND_STATICS_KEY", outboundDateStatisticsMaterialBO);
        return result;
    }

    @Override
    public List<Location> queryLocationStock() {
        return locationMapper.queryLocationStock();
    }

    @Override
    public List<BillAndMaterialVO> queryBillExpire() {
        List<BillAndMaterialVO> billAndMaterialVOS = billMapper.queryBillExpire();
        NotificationCondition notificationCondition = notificationMapper.selectByPrimaryKey(EXPIRE_NOTIFICATION_CONDITION_ID);
        if (billAndMaterialVOS == null) {
            return null;
        }
        if (billAndMaterialVOS.size() >= notificationCondition.getCount()) {
            return billAndMaterialVOS;
        }
        return null;
    }

    @Override
    public int queryMaterialExpireCount() {
        return materialMapper.queryMaterialExpireCount();
    }

    @Override
    public Map<String, Object> queryRecentOperation() {
        Map<String, Object> resultObject = new HashMap<>();
        Bill recentInboundBill = billMapper.queryRecentBillOperation(INBOUND_TYPE);
        Bill recentRestoresBill = materialHistoryMapper.queryRestoresOperation();

        Bill inboundBill = null;
        if (recentInboundBill != null && recentRestoresBill != null) {
            if (recentInboundBill.getCreateTime().compareTo(recentRestoresBill.getCreateTime()) >= 0) {
                inboundBill = recentInboundBill;
            } else {
                inboundBill = recentRestoresBill;
            }
        } else {
            if (recentInboundBill != null) {
                inboundBill = recentInboundBill;
            }
            if (recentRestoresBill != null) {
                inboundBill = recentRestoresBill;
            }
        }
        Bill recentOutboundBill = billMapper.queryRecentBillOperation(OUTBOUND_TYPE);
        StocktakingRecord recentStocktakingRecord = stocktakingRecordMapper.queryRecentStoktaingRecord();
        if (recentStocktakingRecord != null) {
            int problemCount = stocktakingRecordMapper.queryStocktakingProblemByRecordId(recentStocktakingRecord.getId());
            recentStocktakingRecord.setProblemCount(problemCount);
        }
        resultObject.put(RECENT_INBOUND_BILL_KEY, inboundBill);
        resultObject.put(RECENT_OUTBOUND_BILL_KEY, recentOutboundBill);
        resultObject.put(RECENT_STOCKTAKING_RECORD_KEY, recentStocktakingRecord);
        return resultObject;
    }


    /**
     * 获取两个日期之间的所有日期
     *
     * @param startTime 开始日期
     * @param endTime 结束日期
     */
    private static List<DateStatisticsMaterialBO> getDays(String startTime, String endTime, Integer timeUnit) {
        // 返回的日期集合
        List<DateStatisticsMaterialBO> days = new ArrayList();
        DateFormat dateFormat;
        if (timeUnit.equals(TIME_UNIT_DAY)) {
            dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        } else {
            dateFormat = new SimpleDateFormat("yyyy-MM");
        }
        try {
            Date start = dateFormat.parse(startTime);
            Date end = dateFormat.parse(endTime);

            Calendar tempStart = Calendar.getInstance();
            tempStart.setTime(start);

            Calendar tempEnd = Calendar.getInstance();
            tempEnd.setTime(end);
            if (timeUnit.equals(TIME_UNIT_DAY)) {
                tempEnd.add(Calendar.DATE, +1);// 日期加1(包含结束)
            } else {
                tempEnd.add(Calendar.MONTH, +1);
            }
            while (tempStart.before(tempEnd)) {
                if (!dateFormat.format(tempStart.getTime()).equals(dateFormat.format(start))) {
                    days.add(new DateStatisticsMaterialBO(dateFormat.format(tempStart.getTime())));
                }
                if (timeUnit.equals(TIME_UNIT_DAY)) {
                    tempStart.add(Calendar.DAY_OF_YEAR, 1);
                } else {
                    tempStart.add(Calendar.MONTH, 1);
                }

            }
        } catch (ParseException e) {
            log.error("生成当前时间到一年前期间日期集合异常", e);
        }
        return days;
    }

    /**
     * 根据根据日期统计出来的数量对应的赋值给返回数据的对应日期的数量
     *
     * @param dateStatisticsMaterialBOs 当前到一年前的日期数据对象集合
     * @param inboundStaticsData 根据日期统计出来的数据集合
     */
    private void setDateStatisticsMaterialBOCount(List<DateStatisticsMaterialBO> dateStatisticsMaterialBOs, List<StatisticsVo> inboundStaticsData) {
        inboundStaticsData.forEach(e -> {
            String time = e.getTime();
            dateStatisticsMaterialBOs.forEach(r -> {
                if (r.getDate().equals(time)) {
                    r.setCount(e.getCount());
                }
            });
        });

    }
}
