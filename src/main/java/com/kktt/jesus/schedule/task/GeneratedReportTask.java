package com.kktt.jesus.schedule.task;

public class GeneratedReportTask extends BaseTask {

    private Integer id;

    private String requestId;

    private String reportId;

    private long startMillis = 0;

    private long endMillis = 0;

    // for json decode.
    public GeneratedReportTask() {
    }

    public GeneratedReportTask(Integer id) {
        this.id = id;
    }

    public GeneratedReportTask(Integer id, long startMillis, long endMillis) {
        this.id = id;
        this.startMillis = startMillis;
        this.endMillis = endMillis;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getReportId() {
        return reportId;
    }

    public void setReportId(String reportId) {
        this.reportId = reportId;
    }

    public long getStartMillis() {
        return startMillis;
    }

    public void setStartMillis(long startMillis) {
        this.startMillis = startMillis;
    }

    public long getEndMillis() {
        return endMillis;
    }

    public void setEndMillis(long endMillis) {
        this.endMillis = endMillis;
    }
}
