package expenditure.recorder.gui.model;

import com.google.api.client.util.DateTime;
import com.google.api.client.util.Key;

import java.time.Instant;

public class Record {
    @Key
    private String item;
    @Key
    private Integer amountInCent;
    @Key
    private DateTime date;

    public String getItem() {
        return item;
    }

    public Integer getAmountInCent() {
        return amountInCent;
    }

    public Instant getDate() {
        return Instant.ofEpochMilli(date.getValue());
    }
}
