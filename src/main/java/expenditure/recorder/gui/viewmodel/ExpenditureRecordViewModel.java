package expenditure.recorder.gui.viewmodel;

import java.time.LocalDate;

import expenditure.recorder.gui.model.RecordClientDefault;
import expenditure.recorder.gui.view.configuration.ExpenditureRecorderGuiConfiguration;
import expenditure.recorder.gui.viewmodel.filter.CurrentTimeRangeManager;
import expenditure.recorder.gui.viewmodel.utilities.MoneyFormatter;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;

public class ExpenditureRecordViewModel {
    private StringProperty itemText = new SimpleStringProperty();
    private StringProperty amountText = new SimpleStringProperty();
    private ObjectProperty<LocalDate> date = new SimpleObjectProperty<>();

    private StringProperty itemErrorText = new SimpleStringProperty();
    private StringProperty amountErrorText = new SimpleStringProperty();
    private StringProperty dateErrorText = new SimpleStringProperty();

    private ObjectProperty<ObservableList<RecordTableItem>> recordTable = new SimpleObjectProperty<>();

    private ObjectProperty<String> filterBoxItem = new SimpleObjectProperty<>();

    private ObjectProperty<LocalDate> fromDate = new SimpleObjectProperty<>();
    private ObjectProperty<LocalDate> toDate = new SimpleObjectProperty<>();

    private BooleanProperty fromDateDisabled = new SimpleBooleanProperty();
    private BooleanProperty toDateDisabled = new SimpleBooleanProperty();

    private StringProperty totalAmountText = new SimpleStringProperty();

    private StringProperty itemKeyWord = new SimpleStringProperty();

    private ExpenditureRecordService expenditureRecordService;

    public ExpenditureRecordViewModel(ExpenditureRecorderGuiConfiguration configuration) {
        expenditureRecordService = new ExpenditureRecordService(new RecordClientDefault(configuration.getRecordClientConfiguration()));

        SimpleListProperty<RecordTableItem> filteredRecordItemsProperty = new SimpleListProperty<>(
                expenditureRecordService.getAllRecordTableItemsFromServer());
        filteredRecordItemsProperty.addListener((observable, oldValue, newValue) -> totalAmountText.setValue(
                "€ " + MoneyFormatter.formatIntegerToString(
                        observable.getValue().stream().mapToInt(RecordTableItem::getAmountInCent).sum())));

        recordTable.bind(filteredRecordItemsProperty);

        fromDate.addListener((observable, oldValue, newValue) -> expenditureRecordService.setStartDate(newValue));
        toDate.addListener((observable, oldValue, newValue) -> expenditureRecordService.setEndDate(newValue));

        filterBoxItem.addListener(((observable, oldValue, newValue) -> {
            expenditureRecordService.setStartDate(CurrentTimeRangeManager.getStartDate(newValue));

            if (newValue.equals("Customize")) {
                setFromDateDisabled(false);
                setToDateDisabled(false);
            } else {
                setFromDateDisabled(true);
                setToDateDisabled(true);

                if (newValue.equals("All")) {
                    expenditureRecordService.setEndDate(null);
                } else {
                    expenditureRecordService.setEndDate(LocalDate.now());
                }
            }
        }));

        itemKeyWord.addListener((observable, oldValue, newValue) -> expenditureRecordService.setPredicate(newValue));
    }

    public void addRecord() {
        if (!checkInput()) {
            return;
        }

        expenditureRecordService.addRecordTableItem(RecordTableItem.from(itemText.get(), amountText.get(), date.getValue()));

        clearInput();
    }

    public void deleteRecord(RecordTableItem record) {
        expenditureRecordService.removeRecordTableItem(record);
    }

    public void clearInput() {
        clearErrorText();

        setItemTextProperty("");
        setAmountTextProperty("");
        setDateProperty(null);
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

    public ObjectProperty<String> getFilterBoxItemProperty() {
        return filterBoxItem;
    }

    public ObjectProperty<LocalDate> getFromDateProperty() {
        return fromDate;
    }

    public ObjectProperty<LocalDate> getToDateProperty() {
        return toDate;
    }

    public BooleanProperty fromDateDisabledProperty() {
        return fromDateDisabled;
    }

    public BooleanProperty toDateDisabledProperty() {
        return toDateDisabled;
    }

    public void setFromDateDisabled(boolean fromDateDisabled) {
        this.fromDateDisabled.set(fromDateDisabled);
    }

    public void setToDateDisabled(boolean toDateDisabled) {
        this.toDateDisabled.set(toDateDisabled);
    }

    public StringProperty getItemKeyWordProperty() {
        return itemKeyWord;
    }
}
