package expenditure.recorder.gui.viewmodel.utilities;

import org.junit.Test;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class MoneyFormatterTest {
    @Test
    public void formatWithCorrectString() {
        assertThat(MoneyFormatter.formatIntegerToString(100)).isEqualTo("1.00");
        assertThat(MoneyFormatter.formatIntegerToString(10)).isEqualTo("0.10");
        assertThat(MoneyFormatter.formatIntegerToString(0)).isEqualTo("0.00");
    }
}