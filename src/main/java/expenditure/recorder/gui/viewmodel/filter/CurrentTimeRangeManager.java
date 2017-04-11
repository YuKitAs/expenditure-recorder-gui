package expenditure.recorder.gui.viewmodel.filter;

import java.time.LocalDate;

public class CurrentTimeRangeManager {
    public static LocalDate getStartDate(String timeRange) {
        switch (timeRange) {
            case "Today":
                return LocalDate.now();
            case "Last 7 Days":
                return LocalDate.now().minusWeeks(1);
            case "Last 30 Days":
                return LocalDate.now().minusDays(30);
            default:
                return null;
        }
    }
}
