package org.app.zeroresearchtravel.androidhackdataapp;

public class EventDataCustom {
    private String summary;
    private String startDate;
    private String endDate;
    private String diffDays;
    public EventDataCustom(String summary, String startDate, String endDate, String diffDays) {
        this.summary = summary;
        this.startDate = startDate;
        this.endDate = endDate;
        this.diffDays = diffDays;
    }
}
