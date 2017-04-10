package expenditure.recorder.gui.viewmodel.utilities;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

public class DateConverter {
    public static LocalDate convertInstantToLocalDate(Instant instant) {
        return LocalDate.from(instant.atZone(ZoneId.systemDefault()));
    }

    public static Instant convertLocalDateToInstant(LocalDate localDate) {
        return localDate.atStartOfDay(ZoneId.systemDefault()).toInstant();
    }
}
