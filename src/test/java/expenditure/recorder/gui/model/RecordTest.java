package expenditure.recorder.gui.model;

import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by yuka on 28.01.17.
 */
public class RecordTest {
    Record record;

    @Before
    public void setUp() throws Exception {
        record = new Record("some item", "some amount", "some date");
    }

    @Test
    public void getItemWithCorrectItem() {
        assertThat(record.getItem()).isEqualTo("some item");
    }

    @Test
    public void getAmountWithCorrectAmount() {
        assertThat(record.getAmount()).isEqualTo("some amount");
    }

    @Test
    public void getDateWithCorrectDate() {
        assertThat(record.getDate()).isEqualTo("some date");
    }
}