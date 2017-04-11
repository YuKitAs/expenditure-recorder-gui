package expenditure.recorder.gui.viewmodel.utilities;

import org.junit.Test;

import java.time.Instant;
import java.time.LocalDate;
import java.time.Month;

import static org.assertj.core.api.Assertions.assertThat;

public class DateConverterTest {
    private final LocalDate localDate = LocalDate.of(2017, Month.JANUARY, 1);
    private final Instant dateInstant = Instant.ofEpochMilli(1483225200000L);

    @Test
    public void convertInstantToLocalDate() {
        assertThat(DateConverter.convertInstantToLocalDate(dateInstant)).isEqualTo(localDate);
    }

    @Test
    public void convertLocalDateToInstant() {
        assertThat(DateConverter.convertLocalDateToInstant(localDate)).isEqualTo(dateInstant);
    }
}