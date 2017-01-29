package expenditure.recorder.gui.viewmodel.utilities;

import java.math.RoundingMode;
import java.text.DecimalFormat;

public class MoneyFormatter {
    public static String format(Integer amountInCent) {
        DecimalFormat df = new DecimalFormat("0.00");
        df.setRoundingMode(RoundingMode.HALF_UP);
        return df.format(amountInCent / 100.0);
    }
}
