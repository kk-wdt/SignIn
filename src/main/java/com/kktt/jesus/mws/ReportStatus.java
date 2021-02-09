package com.kktt.jesus.mws;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum ReportStatus {

    FAILED("-1"), DONE("_DONE_"), CANCELLED("_CANCELLED_"), DONE_NO_DATA("_DONE_NO_DATA_"),

    IN_PROGRESS("_IN_PROGRESS_"), SUBMITTED("_SUBMITTED_");

    private String value;
    private static final Logger logger = LoggerFactory.getLogger(ReportStatus.class);

    ReportStatus(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }

    public static ReportStatus from(String str) {
        switch (str) {
            case "_DONE_": return DONE;
            case "_CANCELLED_": return CANCELLED;
            case "_DONE_NO_DATA_": return DONE_NO_DATA;
            case "_IN_PROGRESS_": return IN_PROGRESS;
            case "_SUBMITTED_": return SUBMITTED;
            default: {
                logger.warn("没发现的ReportStatus: {}", str);
                return IN_PROGRESS;
            }
        }
    }
}
