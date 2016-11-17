package expenditure.recorder.gui.viewmodel;

import expenditure.recorder.gui.model.TimeRange;

import java.time.LocalDate;

public class CurrentTimeRangeUpdater {
    private static CurrentTimeRangeUpdater instance = null;
    private TimeRange currentTimeRange = TimeRange.ALL;
    private LocalDate fromDate = null;
    private LocalDate toDate = null;

    private CurrentTimeRangeUpdater() {
    }

    public static CurrentTimeRangeUpdater getInstance() {
        if (instance == null) {
            instance = new CurrentTimeRangeUpdater();
        }

        return instance;
    }

    public void setCurrentTimeRange(String timeRange) {
        switch (timeRange) {
            case "All":
                currentTimeRange = TimeRange.ALL;
                break;
            case "Today":
                currentTimeRange = TimeRange.TODAY;
                break;
            case "Last 7 Days":
                currentTimeRange = TimeRange.LAST_7_DAYS;
                break;
            case "Last 30 Days":
                currentTimeRange = TimeRange.LAST_30_DAYS;
                break;
            case "Custom":
                currentTimeRange = TimeRange.CUSTOM;
                break;
        }
    }

    public LocalDate getFromDate() {
        return fromDate;
    }

    public void setFromDate(LocalDate fromDate) {
        this.fromDate = fromDate;
    }

    public LocalDate getToDate() {
        return toDate;
    }

    public void setToDate(LocalDate toDate) {
        this.toDate = toDate;
    }

    public TimeRange getCurrentTimeRange() {
        return currentTimeRange;
    }
}
