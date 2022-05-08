package com.laci.gazora.model;


public class AddressItem {
    private String id;
    private String name;

    public AddressItem(String name) {
        this.name = name;
    }

    public AddressItem() {};

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String _getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
