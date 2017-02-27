package expenditure.recorder.gui.viewmodel;

import org.junit.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class RecordTableItemTest {
    private static final LocalDate DATE = LocalDate.of(2017, 2, 27);
    private static final String DATE_STRING = "2017-02-27";

    private RecordTableItem recordTableItem = RecordTableItem.from("some item", "42", DATE);

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
        assertThat(recordTableItem.getDate()).isEqualTo(DATE_STRING);
    }
}