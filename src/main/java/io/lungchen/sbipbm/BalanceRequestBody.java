package io.lungchen.sbipbm;

import org.json.JSONObject;

public class BalanceRequestBody extends RequestBody {
    private String accountId;

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String toJSONString() {
        return new JSONObject(this).toString();
    }

    @Override
    public String toString() {
        return getId().toString() + getTimestamp().toString() + "accountId" + getAccountId() + getKey();
    }
}
