package expenditure.recorder.gui.viewmodel;

import expenditure.recorder.gui.model.Record;
import expenditure.recorder.gui.model.TimeRange;
import expenditure.recorder.gui.viewmodel.helper.CurrentTimeRangeManager;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.List;

public class MainViewModel {
    private StringProperty itemText = new SimpleStringProperty();
    private StringProperty amountText = new SimpleStringProperty();
    private ObjectProperty<LocalDate> date = new SimpleObjectProperty<>();

    private StringProperty itemErrorText = new SimpleStringProperty();
    private StringProperty amountErrorText = new SimpleStringProperty();
    private StringProperty dateErrorText = new SimpleStringProperty();

    private ObjectProperty<ObservableList<Record>> recordTable = new SimpleObjectProperty<>();
    private ObservableList<Record> records = FXCollections.observableArrayList();
    private ObservableList<Record> filteredRecords = FXCollections.observableArrayList();

    private ObjectProperty<SingleSelectionModel<String>> filterBox = new SimpleObjectProperty<>();
    private List<String> timeRanges = Arrays.asList("All", "Today", "Last 7 Days", "Last 30 Days");

    private ObjectProperty<LocalDate> fromDate = new SimpleObjectProperty<>();
    private ObjectProperty<LocalDate> toDate = new SimpleObjectProperty<>();

    private StringProperty totalAmountText = new SimpleStringProperty();
    private int totalAmount;

    private BooleanProperty filterBoxRadioButtonSelected = new SimpleBooleanProperty();
    private BooleanProperty filterPickerRadioButtonSelected = new SimpleBooleanProperty();

    private CurrentTimeRangeManager currentTimeRangeManager = CurrentTimeRangeManager.getInstance();


    public MainViewModel() {
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

        totalAmountText.set("€ 0");
    }

    public void addRecord() {
        if (checkInput()) {
            records.add(new Record(itemText.get(), amountText.get(), date.get().toString()));
            recordTable.setValue(records);

            updateTotalAmount(records);
        }
    }

    public void clearInput() {
        clearErrorText();

        itemText.set(null);
        amountText.set(null);
        date.set(null);
    }

    public void deleteRecord(Record record) {
        recordTable.getValue().remove(record);
        updateTotalAmount(records);
    }

    public void filterBoxOnAction() {
        if (filterBoxRadioButtonSelected.get()) {
            currentTimeRangeManager.setCurrentTimeRange(filterBox.get().getSelectedItem());
            filterRecords();
        }
    }

    public void filterBoxRadioButtonOnAction() {
        currentTimeRangeManager.setCurrentTimeRange(filterBox.get().getSelectedItem());
        filterRecords();
    }

    public void filterPickerRadioButtonOnAction() {
        currentTimeRangeManager.setCurrentTimeRange("Custom");
        filterRecords();
    }

    public void fromDatePickerOnAction() {
        currentTimeRangeManager.setFromDate(fromDate.getValue());
        currentTimeRangeManager.setCurrentTimeRange("Custom");

        if (filterPickerRadioButtonSelected.get()) {
            filterRecords();
        }
    }

    public void toDatePickerOnAction() {
        currentTimeRangeManager.setToDate(toDate.getValue());
        currentTimeRangeManager.setCurrentTimeRange("Custom");

        if (filterPickerRadioButtonSelected.get()) {
            filterRecords();
        }
    }

    public void filterRecords() {
        filteredRecords.clear();

        TimeRange currentTimeRange = currentTimeRangeManager.getCurrentTimeRange();

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
        LocalDate fromDate = currentTimeRangeManager.getFromDate();
        LocalDate toDate = currentTimeRangeManager.getToDate();

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
        records.forEach(record -> totalAmount += Integer.parseInt(record.getAmount()));

        totalAmountText.set("€ " + Integer.toString(totalAmount));
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

    private void clearErrorText() {
        itemErrorText.set("");
        amountErrorText.set("");
        dateErrorText.set("");
    }

    public StringProperty itemTextProperty() {
        return itemText;
    }

    public StringProperty amountTextProperty() {
        return amountText;
    }

    public StringProperty itemErrorTextProperty() {
        return itemErrorText;
    }

    public StringProperty amountErrorTextProperty() {
        return amountErrorText;
    }

    public StringProperty dateErrorTextProperty() {
        return dateErrorText;
    }

    public ObjectProperty<LocalDate> dateProperty() {
        return date;
    }

    public StringProperty totalAmountTextProperty() {
        return totalAmountText;
    }

    public ObjectProperty<ObservableList<Record>> recordTableProperty() {
        return recordTable;
    }

    public ObjectProperty<SingleSelectionModel<String>> filterBoxProperty() {
        return filterBox;
    }

    public BooleanProperty filterBoxRadioButtonSelectedProperty() {
        return filterBoxRadioButtonSelected;
    }

    public BooleanProperty filterPickerRadioButtonSelectedProperty() {
        return filterPickerRadioButtonSelected;
    }

    public ObjectProperty<LocalDate> fromDateProperty() {
        return fromDate;
    }

    public ObjectProperty<LocalDate> toDateProperty() {
        return toDate;
    }
}
