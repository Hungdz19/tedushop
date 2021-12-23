package com.example.demosdk3;

import org.json.JSONArray;

public class SeverRoleItem {
    public String id, name, level;
    public JSONArray jsonArray;

    public SeverRoleItem(String id, String name, String level, JSONArray jsonArray) {
        this.id = id;
        this.name = name;
        this.level = level;
        this.jsonArray = jsonArray;
    }
}
