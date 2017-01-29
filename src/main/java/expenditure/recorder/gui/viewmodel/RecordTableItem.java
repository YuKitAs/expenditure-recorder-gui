package expenditure.recorder.gui.viewmodel;

import expenditure.recorder.gui.model.Record;
import expenditure.recorder.gui.viewmodel.utilities.MoneyFormatter;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class RecordTableItem {
    private final String item;
    private final Integer amountInCent;
    private final Instant date;

    private RecordTableItem(String item, Integer amountInCent, Instant date) {
        this.item = item;
        this.amountInCent = amountInCent;
        this.date = date;
    }

    public static RecordTableItem from(Record record) {
        return new RecordTableItem(record.getItem(), record.getAmountInCent(), record.getDate());
    }

    public static RecordTableItem from(String item, String amount, LocalDate date) {
        return new RecordTableItem(item, (int) (Double.parseDouble(amount) * 100), date.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    public String getItem() {
        return item;
    }

    public String getAmount() {
        return MoneyFormatter.format(amountInCent);
    }

    public Integer getAmountInCent() {
        return amountInCent;
    }

    public String getDate() {
        return DateTimeFormatter.ofPattern("yyyy-MM-dd").format(date.atZone(ZoneId.systemDefault()));
    }
}
