package io.lungchen.sbipbm;

import org.json.JSONObject;

public class TransferRequestBody extends RequestBody {
    private String from;
    private String to;
    private Double amount;

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String toJSONString() {
        return new JSONObject(this).toString();
    }

    @Override
    public String toString() {
        return getId().toString() + getTimestamp().toString()
                + "from" + getFrom() + "to" + getTo() + "amount" + getAmount().toString() + getKey();
    }
}
