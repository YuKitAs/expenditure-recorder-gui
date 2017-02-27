package expenditure.recorder.gui.viewmodel.utilities;

import org.junit.Test;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class MoneyFormatterTest {
    @Test
    public void formatFromIntegerToString_WithCorrectString() {
        assertThat(MoneyFormatter.formatIntegerToString(100)).isEqualTo("1.00");
        assertThat(MoneyFormatter.formatIntegerToString(10)).isEqualTo("0.10");
        assertThat(MoneyFormatter.formatIntegerToString(0)).isEqualTo("0.00");
    }

    @Test
    public void formatStringToInteger_WithCorrectInteger() {
        assertThat(MoneyFormatter.formatStringToInteger("10.12")).isEqualTo(1012);
        assertThat(MoneyFormatter.formatStringToInteger("0.12")).isEqualTo(12);
    }
}