package com.sd.spartan.todolist.model;

public class SectionModel {
    String  id, title, sec_id, date ;

    public SectionModel(String id, String title,  String date, String sec_id) {
        this.id = id;
        this.title = title;
        this.sec_id = sec_id;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getSec_id() {
        return sec_id;
    }

    public String getDate() {
        return date;
    }
}
