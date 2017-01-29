package expenditure.recorder.gui.viewmodel;

import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class RecordTableItemTest {
    RecordTableItem recordTableItem;

    @Before
    public void setUp() {
        recordTableItem = RecordTableItem.from("some item", "42", LocalDate.now());
    }

    @Test
    public void getItemWithCorrectItemString() {
        assertThat(recordTableItem.getItem()).isEqualTo("some item");
    }

    @Test
    public void getAmountWithCorrectAmountString() {
        assertThat(recordTableItem.getAmount()).isEqualTo("42.00");
    }

    @Test
    public void getAmountInCentWithCorrectAmountInteger() {
        assertThat(recordTableItem.getAmountInCent()).isEqualTo(4200);
    }

    @Test
    public void getDateWithCorrectDate() {
        assertThat(recordTableItem.getDate()).isEqualTo(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
    }
}