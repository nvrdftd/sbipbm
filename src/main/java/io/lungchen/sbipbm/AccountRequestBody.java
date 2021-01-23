package io.lungchen.sbipbm;

import org.json.JSONObject;

public class AccountRequestBody extends RequestBody {
    private String accountId;
    private Double initialBalance;

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public Double getInitialBalance() {
        return initialBalance;
    }

    public void setInitialBalance(Double initialBalance) {
        this.initialBalance = initialBalance;
    }

    public String toJSONString() {
        return new JSONObject(this).toString();
    }

    @Override
    public String toString() {
        return getId().toString() + getTimestamp().toString() + "accountId" + getAccountId()
                + "initialBalance" + getInitialBalance().toString() + getKey();
    }
}
