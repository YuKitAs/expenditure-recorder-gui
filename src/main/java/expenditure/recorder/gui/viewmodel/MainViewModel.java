package expenditure.recorder.gui.viewmodel;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import expenditure.recorder.gui.model.RecordClientDefault;
import expenditure.recorder.gui.view.configuration.ExpenditureRecorderGuiConfiguration;
import expenditure.recorder.gui.viewmodel.filter.CurrentTimeRangeManager;
import expenditure.recorder.gui.viewmodel.filter.RecordFilter;
import expenditure.recorder.gui.viewmodel.utilities.MoneyFormatter;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.SingleSelectionModel;

public class MainViewModel {
    private StringProperty itemText = new SimpleStringProperty();
    private StringProperty amountText = new SimpleStringProperty();
    private ObjectProperty<LocalDate> date = new SimpleObjectProperty<>();

    private StringProperty itemErrorText = new SimpleStringProperty();
    private StringProperty amountErrorText = new SimpleStringProperty();
    private StringProperty dateErrorText = new SimpleStringProperty();

    private ObjectProperty<ObservableList<RecordTableItem>> recordTable = new SimpleObjectProperty<>();

    private ObjectProperty<SingleSelectionModel<String>> filterBox = new SimpleObjectProperty<>();
    private List<String> timeRanges = Arrays.asList("All", "Today", "Last 7 Days", "Last 30 Days");

    private ObjectProperty<LocalDate> fromDate = new SimpleObjectProperty<>();
    private ObjectProperty<LocalDate> toDate = new SimpleObjectProperty<>();

    private StringProperty totalAmountText = new SimpleStringProperty();

    private BooleanProperty filterBoxRadioButtonSelected = new SimpleBooleanProperty();
    private BooleanProperty filterPickerRadioButtonSelected = new SimpleBooleanProperty();

    private CurrentTimeRangeManager currentTimeRangeManager = CurrentTimeRangeManager.getInstance();

    private ExpenditureRecordService expenditureRecordService;

    private RecordFilter recordFilter;

    public MainViewModel(ExpenditureRecorderGuiConfiguration configuration) {
        expenditureRecordService = new ExpenditureRecordService(new RecordClientDefault(configuration.getRecordClientConfiguration()));

        recordTable.setValue(expenditureRecordService.getInitialRecordTableItems());

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

        updateTotalAmount();

        recordFilter = new RecordFilter(recordTable.getValue());
    }

    public void showRecordsFromDatabase() {
        updateTotalAmount();
    }

    public void addRecord() {
        if (!checkInput()) {
            return;
        }

        expenditureRecordService.addRecordTableItem(RecordTableItem.from(itemText.get(), amountText.get(), date.getValue()));
        updateTotalAmount();
        clearInput();
    }

    public void clearInput() {
        clearErrorText();

        setItemTextProperty("");
        setAmountTextProperty("");
        setDateProperty(null);
    }

    public void deleteRecord(RecordTableItem record) {
        expenditureRecordService.removeRecordTableItem(record);

        updateTotalAmount();
    }

    public void filterBoxOnAction() {
        if (filterBoxRadioButtonSelected.get()) {
            currentTimeRangeManager.setCurrentTimeRange(filterBox.get().getSelectedItem());

            displayFilteredRecords();
        }
    }

    public void filterBoxRadioButtonOnAction() {
        currentTimeRangeManager.setCurrentTimeRange(filterBox.get().getSelectedItem());

        displayFilteredRecords();
    }

    public void filterPickerRadioButtonOnAction() {
        currentTimeRangeManager.setCurrentTimeRange("Custom");

        displayFilteredRecords();
    }

    public void fromDatePickerOnAction() {
        currentTimeRangeManager.setFromDate(fromDate.getValue());
        currentTimeRangeManager.setCurrentTimeRange("Custom");

        if (filterPickerRadioButtonSelected.get()) {
            displayFilteredRecords();
        }
    }

    public void toDatePickerOnAction() {
        currentTimeRangeManager.setToDate(toDate.getValue());
        currentTimeRangeManager.setCurrentTimeRange("Custom");

        if (filterPickerRadioButtonSelected.get()) {
            displayFilteredRecords();
        }
    }

    private void displayFilteredRecords() {
        recordFilter.filterRecords();
        recordTable.setValue(recordFilter.getFilteredRecords());
        updateTotalAmount();
    }

    private void updateTotalAmount() {
        totalAmountText.set(
                "€ " + MoneyFormatter.formatIntegerToString(recordTable.get().stream().mapToInt(RecordTableItem::getAmountInCent).sum()));
    }

    private Boolean checkInput() {
        clearErrorText();

        Boolean isCorrect = true;

        if (itemText.get() == null || itemText.get().isEmpty()) {
            setItemErrorTextProperty("Please enter an item.");
            isCorrect = false;
        }

        if (amountText.get() == null || !amountText.get().matches("[0-9]+(\\.[0-9]{0,2})?")) {
            setAmountErrorTextProperty("Please enter a valid amount.");
            isCorrect = false;
        }

        if (date.getValue() == null) {
            setDateErrorTextProperty("Please choose a date.");
            isCorrect = false;
        }

        return isCorrect;
    }

    private void clearErrorText() {
        setItemErrorTextProperty("");
        setAmountErrorTextProperty("");
        setDateErrorTextProperty("");
    }

    public StringProperty getItemTextProperty() {
        return itemText;
    }

    public void setItemTextProperty(String item) {
        itemText.set(item);
    }

    public StringProperty getAmountTextProperty() {
        return amountText;
    }

    public void setAmountTextProperty(String amount) {
        amountText.set(amount);
    }

    public ObjectProperty<LocalDate> getDateProperty() {
        return date;
    }

    public void setDateProperty(LocalDate date) {
        this.date.set(date);
    }

    public StringProperty getItemErrorTextProperty() {
        return itemErrorText;
    }

    public void setItemErrorTextProperty(String itemError) {
        itemErrorText.set(itemError);
    }

    public StringProperty getAmountErrorTextProperty() {
        return amountErrorText;
    }

    public void setAmountErrorTextProperty(String amountError) {
        amountErrorText.set(amountError);
    }

    public StringProperty getDateErrorTextProperty() {
        return dateErrorText;
    }

    public void setDateErrorTextProperty(String dateError) {
        dateErrorText.set(dateError);
    }

    public StringProperty getTotalAmountTextProperty() {
        return totalAmountText;
    }

    public ObjectProperty<ObservableList<RecordTableItem>> getRecordTableProperty() {
        return recordTable;
    }

    public ObjectProperty<SingleSelectionModel<String>> getFilterBoxProperty() {
        return filterBox;
    }

    public BooleanProperty getFilterBoxRadioButtonSelectedProperty() {
        return filterBoxRadioButtonSelected;
    }

    public BooleanProperty getFilterPickerRadioButtonSelectedProperty() {
        return filterPickerRadioButtonSelected;
    }

    public ObjectProperty<LocalDate> getFromDateProperty() {
        return fromDate;
    }

    public ObjectProperty<LocalDate> getToDateProperty() {
        return toDate;
    }
}
