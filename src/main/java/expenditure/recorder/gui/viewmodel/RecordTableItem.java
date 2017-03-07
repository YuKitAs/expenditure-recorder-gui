package expenditure.recorder.gui.viewmodel;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import expenditure.recorder.gui.model.Record;
import expenditure.recorder.gui.viewmodel.utilities.MoneyFormatter;

public class RecordTableItem {
    private String id = null;
    private final String item;
    private final Integer amountInCent;
    private final Instant date;

    private RecordTableItem(String id, String item, Integer amountInCent, Instant date) {
        this.id = id;
        this.item = item;
        this.amountInCent = amountInCent;
        this.date = date;
    }

    public static RecordTableItem from(Record record) {
        return new RecordTableItem(record.getId(), record.getItem(), record.getAmountInCent(), record.getDate());
    }

    public static RecordTableItem from(String item, String amount, LocalDate date) {
        return new RecordTableItem(null, item, MoneyFormatter.formatStringToInteger(amount),
                date.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    public String getId() {
        return id;
    }

    public String getItem() {
        return item;
    }

    public String getAmount() {
        return MoneyFormatter.formatIntegerToString(amountInCent);
    }

    public Integer getAmountInCent() {
        return amountInCent;
    }

    // Don't rename this method, since it is used for data binding.
    public String getDate() {
        return DateTimeFormatter.ofPattern("yyyy-MM-dd").format(date.atZone(ZoneId.systemDefault()));
    }

    public Instant getDateInstant() {
        return date;
    }
}
