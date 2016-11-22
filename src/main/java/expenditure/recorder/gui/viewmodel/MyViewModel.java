package expenditure.recorder.gui.viewmodel;

import com.sun.glass.ui.MenuItem;
import com.sun.xml.internal.bind.v2.runtime.property.ValueProperty;
import expenditure.recorder.gui.model.Record;
import expenditure.recorder.gui.model.TimeRange;
import expenditure.recorder.gui.viewmodel.helper.CurrentTimeRangeUpdater;
import expenditure.recorder.gui.viewmodel.helper.RecordUpdater;
import javafx.beans.property.*;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MyViewModel {
    private StringProperty itemText = new SimpleStringProperty();
    private StringProperty amountText = new SimpleStringProperty();
    private ObjectProperty<LocalDate> date = new SimpleObjectProperty<>();

    private StringProperty itemErrorText = new SimpleStringProperty();
    private StringProperty amountErrorText = new SimpleStringProperty();
    private StringProperty dateErrorText = new SimpleStringProperty();

    private ObjectProperty<ObservableList<Record>> recordTable = new SimpleObjectProperty<>();
    private ObservableList<Record> records = FXCollections.observableArrayList();
    private ObservableList<Record> filteredRecords = FXCollections.observableArrayList();

    // private BooleanProperty deleteButtonDisabled = new SimpleBooleanProperty();

    private ObjectProperty<SingleSelectionModel<String>> filterBox = new SimpleObjectProperty<>();
    private List<String> timeRanges = Arrays.asList("All", "Today", "Last 7 Days", "Last 30 Days");

    private StringProperty totalAmountText = new SimpleStringProperty();
    private int totalAmount;

    private CurrentTimeRangeUpdater currentTimeRangeUpdater = CurrentTimeRangeUpdater.getInstance();


    public MyViewModel() {
        filterBox.set(new SingleSelectionModel<String>() {
            @Override
            protected String getModelItem(int index) {
                return timeRanges.get(index);
            }

            @Override
            protected int getItemCount() {
                return 4;
            }
        });
    }

    public void filterPickerRadioButtonOnAction() {
        currentTimeRangeUpdater.setCurrentTimeRange(filterBox.get().getSelectedItem());
        updateRecords();
    }

    public void filterBoxRadioButtonOnAction() {
        currentTimeRangeUpdater.setCurrentTimeRange("Custom");
        updateRecords();
    }

    public void addRecord() {
        if (checkInput()) {
            records.add(new Record(itemText.get(), amountText.get(), date.get().toString()));
            recordTable.setValue(records);
        }
    }

    public void updateRecords() {
        filteredRecords.clear();

        TimeRange currentTimeRange = currentTimeRangeUpdater.getCurrentTimeRange();

        if (currentTimeRange.equals(TimeRange.ALL)) {
            recordTable.setValue(records);
            updateTotalAmount(records);
        } else if (currentTimeRange.equals(TimeRange.TODAY)) {
            ShowRecordsOfToday();
        } else if (currentTimeRange.equals(TimeRange.LAST_7_DAYS)) {
            ShowRecordsInLast7Days();
        } else if (currentTimeRange.equals(TimeRange.LAST_30_DAYS)) {
            ShowRecordsInLast30Days();
        } else if (currentTimeRange.equals(TimeRange.CUSTOM)) {
            showRecordsInCustomTimeRange();
        }
    }

    private void ShowRecordsOfToday() {
        records.forEach(record -> {
            LocalDate date = LocalDate.parse(record.getDate());

            if (date.equals(LocalDate.now(ZoneId.of("CET")))) {
                filteredRecords.add(record);
            }
        });

        recordTable.setValue(filteredRecords);

        updateTotalAmount(filteredRecords);
    }

    private void ShowRecordsInLast7Days() {
        records.forEach(record -> {
            LocalDate date = LocalDate.parse(record.getDate());

            if (date.isAfter(LocalDate.now().minusWeeks(1))) {
                filteredRecords.add(record);
            }
        });

        recordTable.setValue(filteredRecords);

        updateTotalAmount(filteredRecords);
    }

    private void ShowRecordsInLast30Days() {
        records.forEach(record -> {
            LocalDate date = LocalDate.parse(record.getDate());

            if (date.isAfter(LocalDate.now().minusMonths(1))) {
                filteredRecords.add(record);
            }
        });

        recordTable.setValue(filteredRecords);

        updateTotalAmount(filteredRecords);
    }

    private void showRecordsInCustomTimeRange() {
        LocalDate fromDate = currentTimeRangeUpdater.getFromDate();
        LocalDate toDate = currentTimeRangeUpdater.getToDate();

        if (fromDate == null && toDate == null) {
            recordTable.setValue(records);
            updateTotalAmount(records);

            return;
        }

        records.forEach(record -> {
            LocalDate date = LocalDate.parse(record.getDate());

            if (fromDate == null) {
                if (date.isBefore(toDate) || date.isEqual(toDate)) {
                    filteredRecords.add(record);
                }
            } else if (toDate == null) {
                if (date.isAfter(fromDate) || date.isEqual(fromDate)) {
                    filteredRecords.add(record);
                }
            } else {
                if ((date.isAfter(fromDate) || date.isEqual(fromDate)) && (date.isBefore(toDate) || date.isEqual(toDate))) {
                    filteredRecords.add(record);
                }
            }
        });

        recordTable.setValue(filteredRecords);

        updateTotalAmount(filteredRecords);
    }

    private void updateTotalAmount(ObservableList<Record> records) {
        totalAmount = 0;

        records.forEach(record -> totalAmount += Integer.parseInt(record.getAmount().substring(2)));

        totalAmountText.set("€ " + Integer.toString(totalAmount));
    }

    public void deleteRecord(Record record) {
        recordTable.getValue().remove(record);
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

    /*public boolean isDeleteButtonDisabled() {
        return deleteButtonDisabled.get();
    }

    public BooleanProperty deleteButtonDisabledProperty() {
        return deleteButtonDisabled;
    }*/

    public SingleSelectionModel<String> getFilterBox() {
        return filterBox.get();
    }

    public ObjectProperty<SingleSelectionModel<String>> filterBoxProperty() {
        return filterBox;
    }
}
