package expenditure.recorder.gui.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;

public class Record {
    private static class Properties {
        private static final String ID = "id";
        private static final String DATE = "date";
        private static final String AMOUNT_IN_CENT = "amountInCent";
        private static final String ITEM = "item";
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final String id;
    private final String item;
    private final Integer amountInCent;
    private final Instant date;

    @JsonCreator
    public Record(@JsonProperty(Properties.ID) String id, @JsonProperty(Properties.ITEM) String item,
            @JsonProperty(Properties.AMOUNT_IN_CENT) Integer amountInCent, @JsonProperty(Properties.DATE) Instant date) {
        this.id = id;
        this.item = item;
        this.amountInCent = amountInCent;
        this.date = date;
    }

    public String getId() {
        return id;
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
