package expenditure.recorder.gui.viewmodel.utilities;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

import static java.math.BigDecimal.ROUND_HALF_UP;

public class MoneyFormatter {
    public static String formatIntegerToString(Integer amountInCent) {
        DecimalFormat df = new DecimalFormat("0.00");
        df.setRoundingMode(RoundingMode.HALF_UP);
        return df.format(amountInCent / 100.0);
    }

    public static Integer formatStringToInteger(String amount) {
        BigDecimal amountBigDecimal = new BigDecimal(amount);
        amountBigDecimal.setScale(2, ROUND_HALF_UP);
        return (amountBigDecimal.multiply(new BigDecimal("100"))).intValue();
    }
}
