package expenditure.recorder.gui.model;

import com.google.api.client.util.Key;

import java.time.Instant;

public class Record {
    @Key
    private final String item;
    @Key
    private final Integer amountInCent;
    @Key
    private final Instant date;

    public Record(String item, Integer amountInCent, Instant date) {
        this.item = item;
        this.amountInCent = amountInCent;
        this.date = date;
    }

    public String getItem() {
        return item;
    }

    public Integer getAmountInCent() {
        return amountInCent;
    }

    public Instant getDate() {
        return date;
    }
}
