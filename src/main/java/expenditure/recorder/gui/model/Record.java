package expenditure.recorder.gui.model;

import javafx.beans.property.SimpleStringProperty;

public class Record {
    private final SimpleStringProperty item;
    private final SimpleStringProperty amount;
    private final SimpleStringProperty date;

    public Record(String item, String amount, String date) {

        this.item = new SimpleStringProperty(item);
        this.amount = new SimpleStringProperty(amount);
        this.date = new SimpleStringProperty(date);
    }

    public String getItem() {
        return item.get();
    }

    public String getAmount() {
        return amount.get();
    }

    public String getDate() {
        return date.get();
    }
}
