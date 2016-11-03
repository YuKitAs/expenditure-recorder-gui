import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Record {
    private final SimpleStringProperty item;
    private final SimpleIntegerProperty amount;
    private final SimpleStringProperty date;

    public Record(String item, int amount, String date) {

        this.item = new SimpleStringProperty(item);
        this.amount = new SimpleIntegerProperty(amount);
        this.date = new SimpleStringProperty(date);
    }

    public String getItem() {
        return item.get();
    }

    public void setItem(String item) {
        this.item.set(item);
    }

    public int getAmount() {
        return amount.get();
    }

    public void setAmount(int amount) {
        this.amount.set(amount);
    }

    public String getDate() {
        return date.get();
    }

    public void setDate() {
        this.date.set(String.valueOf(date));
    }
}
