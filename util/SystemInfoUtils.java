package com.up72.hn.util;

import com.up72.hn.model.HnActivity;
import com.up72.hn.model.HnOrder;
import com.up72.hn.model.HnUser;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * 系统信息工具类
 */
public class SystemInfoUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(SystemInfoUtils.class);

    /**
     * 捐赠成功信息
     * @param map
     * @return
     */
    public static String generateContributeSuccessInfo(Map<String, Object> map) {
        StringBuffer sb = new StringBuffer();
        String coin = MapUtils.getString(map, "coin");
        if (StringUtils.isNotBlank(coin)) {
            sb.append("捐赠成功！系统已收到您捐赠的");
            sb.append(coin);
            sb.append("金币");
        }
        return sb.toString();
    }

    /**
     * 生成成为vip信息
     * @param user
     * @return
     */
    public static String generateBecomeVipInfo(HnUser user) {
        StringBuffer sb = new StringBuffer();
        sb.append("开通VIP成功，有效期至");
        String date = DateUtil.formatDateByNumFullDate(user.getVipEndTime());
        sb.append(date);
        return sb.toString();
    }

    /**
     * 活动报名成功
     * @param activity
     * @return
     */
    public static String generateActivityEnrollSuccessInfo(HnActivity activity) {
        StringBuffer sb = new StringBuffer();
        sb.append("您成功报名了【");
        sb.append(activity.getTitle());
        sb.append("】活动");
        return sb.toString();
    }

    /**
     * 订单支付成功
     * @param order
     * @return
     */
    public static String generateOrderPaySuccessInfo(HnOrder order) {
        StringBuffer sb = new StringBuffer();
        sb.append("订单编号");
        sb.append(order.getSn());
        sb.append("支付成功");
        return sb.toString();
    }

    /**
     * 发货通知
     * @param order
     * @return
     */
    public static String generateConsignNoticeInfo(HnOrder order) {
        StringBuffer sb = new StringBuffer();
        sb.append("您的订单");
        sb.append(order.getSn());
        sb.append("已发货");
        return sb.toString();
    }


}
