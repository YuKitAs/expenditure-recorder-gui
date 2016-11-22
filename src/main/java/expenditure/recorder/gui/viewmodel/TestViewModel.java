package expenditure.recorder.gui.viewmodel;

import com.sun.glass.ui.MenuItem;
import com.sun.xml.internal.bind.v2.runtime.property.ValueProperty;
import expenditure.recorder.gui.model.Record;
import javafx.beans.property.*;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

import java.time.LocalDate;

public class TestViewModel {
    private StringProperty itemText = new SimpleStringProperty();
    private StringProperty amountText = new SimpleStringProperty();
    private ObjectProperty<LocalDate> date = new SimpleObjectProperty<>();

    private StringProperty itemErrorText = new SimpleStringProperty();
    private StringProperty amountErrorText = new SimpleStringProperty();
    private StringProperty dateErrorText = new SimpleStringProperty();

    private ObjectProperty<ObservableList<Record>> recordTable = new SimpleObjectProperty<>();
    private ObservableList<Record> records = FXCollections.observableArrayList();

    private StringProperty totalAmountText = new SimpleStringProperty();


    public TestViewModel() {

    }

    public void updateRecords() {
        if (checkInput()) {
            records.add(new Record(itemText.get(), amountText.get(), date.get().toString()));
            recordTable.setValue(records);
        }
    }

    private Boolean checkInput() {
        clearErrorText();

        Boolean isCorrect = true;

        if (itemText.get() == null) {
            itemErrorText.set("Please enter an item.");
            isCorrect = false;
        }

        if (amountText.get() == null || !amountText.get().matches("[1-9]+[0-9]*")) {
            amountErrorText.set("Please enter a valid integer.");
            isCorrect = false;
        }

        if (date.get() == null) {
            dateErrorText.set("Please choose a date.");
            isCorrect = false;
        }

        return isCorrect;
    }

    public void clearInput() {
        clearErrorText();

        itemText.set(null);
        amountText.set(null);
        date.set(null);
    }

    private void clearErrorText() {
        itemErrorText.set("");
        amountErrorText.set("");
        dateErrorText.set("");
    }

    public String getItemText() {
        return itemText.get();
    }

    public StringProperty itemTextProperty() {
        return itemText;
    }

    public String getAmountText() {
        return amountText.get();
    }

    public StringProperty amountTextProperty() {
        return amountText;
    }

    public String getItemErrorText() {
        return itemErrorText.get();
    }

    public StringProperty itemErrorTextProperty() {
        return itemErrorText;
    }

    public String getAmountErrorText() {
        return amountErrorText.get();
    }

    public StringProperty amountErrorTextProperty() {
        return amountErrorText;
    }

    public String getDateErrorText() {
        return dateErrorText.get();
    }

    public StringProperty dateErrorTextProperty() {
        return dateErrorText;
    }

    public LocalDate getDate() {
        return date.get();
    }

    public ObjectProperty<LocalDate> dateProperty() {
        return date;
    }

    public String getTotalAmountText() {
        return totalAmountText.get();
    }

    public StringProperty totalAmountTextProperty() {
        return totalAmountText;
    }

    public void setTotalAmountText(String totalAmountText) {
        this.totalAmountText.set(totalAmountText);
    }

    public ObservableList getRecordTable() {
        return recordTable.get();
    }

    public ObjectProperty<ObservableList<Record>> recordTableProperty() {
        return recordTable;
    }
}
