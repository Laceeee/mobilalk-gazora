package com.laci.gazora.model;

public class GasItem {
    private String id;
    private String amount;
    private String date;

    public GasItem(String amount, String date) {
        this.amount = amount;
        this.date = date;
    }

    public GasItem() {}

    public String _getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
