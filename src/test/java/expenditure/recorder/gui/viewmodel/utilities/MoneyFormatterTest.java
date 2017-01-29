package expenditure.recorder.gui.viewmodel.utilities;

import org.junit.Test;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class MoneyFormatterTest {
    @Test
    public void formatWithCorrectString() {
        assertThat(MoneyFormatter.format(100)).isEqualTo("1.00");
        assertThat(MoneyFormatter.format(10)).isEqualTo("0.10");
        assertThat(MoneyFormatter.format(0)).isEqualTo("0.00");
    }
}