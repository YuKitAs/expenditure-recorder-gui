package expenditure.recorder.gui.viewmodel.filter;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

public class FilterPredicate {
    private TimeRange currentTimeRange;

    public FilterPredicate(TimeRange currentTimeRange) {
        this.currentTimeRange = currentTimeRange;
    }

    public boolean timeRangeFilter(Instant dateInstant) {
        LocalDate date = getDateFromInstant(dateInstant);

        switch (currentTimeRange) {
            case TODAY:
                return date.equals(LocalDate.now(ZoneId.systemDefault()));
            case LAST_7_DAYS:
                return date.isAfter(LocalDate.now().minusWeeks(1));
            case LAST_30_DAYS:
                return date.isAfter(LocalDate.now().minusDays(30));
            default:
                return true;
        }
    }

    public boolean timeRangeFilter(LocalDate fromDate, LocalDate toDate, Instant dateInstant) {
        LocalDate date = getDateFromInstant(dateInstant);

        if (fromDate == null && toDate == null) {
            return true;
        }

        if (fromDate == null) {
            if (date.isBefore(toDate) || date.isEqual(toDate)) {
                return true;
            }
        } else if (toDate == null) {
            if (date.isAfter(fromDate) || date.isEqual(fromDate)) {
                return true;
            }
        } else {
            if ((date.isAfter(fromDate) || date.isEqual(fromDate)) && (date.isBefore(toDate) || date.isEqual(toDate))) {
                return true;
            }
        }

        return false;
    }

    private LocalDate getDateFromInstant(Instant dateInstant) {
        return LocalDate.from(dateInstant.atZone(ZoneId.systemDefault()));
    }
}
