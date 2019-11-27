package com.dazhao.common.utils;

import java.text.SimpleDateFormat;
import java.util.concurrent.atomic.AtomicInteger;

public class AccessNumberUtil {

    private static AtomicInteger Guid = new AtomicInteger(0);
    private static final String INBOUND_NUMBER_PREFIX = "R";
    private static final String BORROW_NUMBER_PREFIX = "J";
    private static final String TRANSFER_NUMBER_PREFIX = "Y";
    private static final String DESTORY_NUMBER_PREFIX = "X";

    private AccessNumberUtil() {}

    /**
     * 生成唯一id
     *
     * @param timeFormat 时间格式
     * @param index 时间戳截取开始位置
     */
    public static String getGuid(String timeFormat, int index) {
        String ran = getNumberSuffix(Math.abs(Guid.incrementAndGet() % 100));
        long now = System.currentTimeMillis();
        SimpleDateFormat dateFormat = new SimpleDateFormat(timeFormat);
        String time = dateFormat.format(now);
        String info = now + "";
        return time + info.substring(index) + ran;
    }

    /**
     * 获取物资编码
     *
     * @return 物资编码
     */
    public static String getMaterialNumber() {
        return getGuid("yyyy", 0);
    }

    /**
     * 获取入库单据编码
     *
     * @return 入库单据编码
     */
    public static String getInboundNumber() {
        return INBOUND_NUMBER_PREFIX + getGuid("yyyyMMddHHmmss", 13);
    }

    /**
     * 获取借阅单据编码
     *
     * @return 借阅单据编码
     */
    public static String getBorrowNumber() {
        return BORROW_NUMBER_PREFIX + getGuid("yyyyMMddHHmmss", 13);
    }

    /**
     * 获取移交单据编码
     *
     * @return 移交单据编码
     */
    public static String getTransferNumber() {
        return TRANSFER_NUMBER_PREFIX + getGuid("yyyyMMddHHmmss", 13);
    }

    /**
     * 获取销毁单据编码
     *
     * @return 销毁单据编码
     */
    public static String getDestoryNumber() {
        return DESTORY_NUMBER_PREFIX + getGuid("yyyyMMddHHmmss", 13);
    }

    /**
     * 生成新的上传的文件（图片）名称前缀
     *
     * @return 文件名称前缀
     */
    public static String getFilePerfixNumber() {
        return getGuid("yyyyMMddHHmmss", 13);
    }

    private static String getNumberSuffix(int value) {
        String numberSuffix = String.valueOf(value);
        if (numberSuffix.length() > 3) {
            return numberSuffix.substring(0, 3);
        }
        return String.format("%3d", value).replace(" ", "0");
    }

}
