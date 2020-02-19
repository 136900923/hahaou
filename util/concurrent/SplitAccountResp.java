package com.up72.sjfeng.dto.resp.bank;

/**
 * 分户查询对应结果
 */
public class SplitAccountResp {

    private String acctType;// 子账户类型

    private String acctTypeText;// 子账户类型文本

    private Object result;// 返回结果

    public SplitAccountResp(){}

    public SplitAccountResp(String acctType, String acctTypeText, Object result){
        this.acctType = acctType;
        this.acctTypeText = acctTypeText;
        this.result = result;
    }

    public String getAcctType() {
        return acctType;
    }

    public void setAcctType(String acctType) {
        this.acctType = acctType;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public String getAcctTypeText() {
        return acctTypeText;
    }

    public void setAcctTypeText(String acctTypeText) {
        this.acctTypeText = acctTypeText;
    }
}
