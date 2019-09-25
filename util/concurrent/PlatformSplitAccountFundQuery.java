package com.sjfeng.bankinterface;

import com.up72.framework.dto.Result;
import com.up72.sjfeng.constant.Cnst;
import com.up72.sjfeng.dto.resp.bank.SplitAccountResp;
import com.up72.util.ArrayThreadPool;
import com.up72.ylfz.result.FzBaseResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * 分户资金查询
 *
 * @ClassName SplitAccountCapitalQuery
 * @Description
 * @Author 周录鹏
 * @Date 2019/1/18 15:59
 * @Version 1.0
 **/
public class PlatformSplitAccountFundQuery {

    private static final String BUSINESS_INFO = "平台分户资金查询";// 业务信息

    private static final Logger LOGGER = LoggerFactory.getLogger(PlatformSplitAccountFundQuery.class);

    public static List<SplitAccountResp> getAllSplitAccount() {

        ArrayThreadPool<SplitAccountResp> pool = new ArrayThreadPool();

        Map<String, Object> resultMap = new HashMap<>();
        for (Iterator<Map.Entry<String, String>> iterator = Cnst.BankSplitAccountType.TYPE_MAP.entrySet().iterator(); iterator.hasNext(); ) {
            Map.Entry entry = iterator.next();
            String key = (String) entry.getKey();
            String value = (String) entry.getValue();

            // 平台账户没有预付货款分户，跳过
            if (Cnst.BankSplitAccountType.PAYMENT_IN_ADVANCE_SPLIT_ACCOUNT.equals(key)) {
                continue;
            }
            pool.submit(() -> {
//                System.out.println("网络操作开始" + key);
//                Thread.sleep(data * 1000);
                Result<FzBaseResult> result = SplitAccountFundQueryInterface.getResult(null, key);
//                System.out.println("网络操作结束" + key);
                return new SplitAccountResp(key, value, result);
            });
        }

        List<SplitAccountResp> list = pool.get();
//        for (SplitAccountResp resp : list) {
//            System.out.println(JSON.toJSONString(resp));
//        }
        pool.stop();

        list.sort(new Comparator<SplitAccountResp>() {
            @Override
            public int compare(SplitAccountResp o1, SplitAccountResp o2) {
                int no = 0;
                if (o1.getAcctType().compareTo(o2.getAcctType()) > 0) {
                    no = 1;
                } else if (o1.getAcctType().compareTo(o2.getAcctType()) < 0) {
                    no = -1;
                } else if (o1.getAcctType().compareTo(o2.getAcctType()) == 0) {
                    no = 0;
                }
                return no;
            }
        });
//        for (SplitAccountResp resp : list) {
//            System.out.println(JSON.toJSONString(resp));
//        }
        return list;
    }

    public static void main(String[] args) {
        getAllSplitAccount();
    }
}
