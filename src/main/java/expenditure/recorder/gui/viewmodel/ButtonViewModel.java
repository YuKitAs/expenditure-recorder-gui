package expenditure.recorder.gui.viewmodel;

import expenditure.recorder.gui.model.Record;
import expenditure.recorder.gui.viewmodel.helper.CurrentTimeRangeUpdater;
import expenditure.recorder.gui.viewmodel.helper.RecordUpdater;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;

public class ButtonViewModel {
    private Button addButton;
    private Button clearButton;
    private Button deleteButton;

    private RadioButton filterBoxRadioButton;
    private RadioButton filterPickerRadioButton;

    private final ToggleGroup radioButtonGroup = new ToggleGroup();

    private TableView<Record> recordTable;

    private ObservableList<Record> records = FXCollections.observableArrayList();

    private TextField itemField;
    private TextField amountField;

    private DatePicker datePicker;

    private Text itemErrorText;
    private Text amountErrorText;
    private Text dateErrorText;

    private ComboBox<String> filterBox;

    private RecordUpdater recordUpdater;

    private CurrentTimeRangeUpdater currentTimeRangeUpdater;

    public ButtonViewModel(Button addButton, Button clearButton, Button deleteButton, RadioButton filterBoxRadioButton,
                           RadioButton filterPickerRadioButton, TableView<Record> recordTable, ObservableList<Record> records,
                           TextField itemField, TextField amountField, DatePicker datePicker, Text itemErrorText, Text amountErrorText,
                           Text dateErrorText, ComboBox<String> filterBox, RecordUpdater recordUpdater) {
        this.addButton = addButton;
        this.clearButton = clearButton;
        this.deleteButton = deleteButton;
        this.filterBoxRadioButton = filterBoxRadioButton;
        this.filterPickerRadioButton = filterPickerRadioButton;
        this.recordTable = recordTable;
        this.records = records;
        this.itemField = itemField;
        this.amountField = amountField;
        this.datePicker = datePicker;
        this.itemErrorText = itemErrorText;
        this.amountErrorText = amountErrorText;
        this.dateErrorText = dateErrorText;
        this.filterBox = filterBox;
        this.recordUpdater = recordUpdater;
        currentTimeRangeUpdater = CurrentTimeRangeUpdater.getInstance();
    }

    public void initRadioButton() {
        filterBoxRadioButton.setToggleGroup(radioButtonGroup);
        filterPickerRadioButton.setToggleGroup(radioButtonGroup);
    }

    public void setAddButtonOnAction(TableColumn itemCol, TableColumn amountCol, TableColumn dateCol) {
        addButton.setOnAction(event -> {
            itemErrorText.setText("");
            amountErrorText.setText("");
            dateErrorText.setText("");

            try {
                if (itemField.getText().isEmpty()) {
                    itemErrorText.setText("Please enter an item.");
                    return;
                }

                if (!amountField.getText().matches("[1-9]+[0-9]*")) {
                    amountErrorText.setText("Please enter a valid integer.");
                    return;
                }

                if (datePicker.getValue() == null) {
                    dateErrorText.setText("Please choose a date.");
                    return;
                }

                records.add(new Record(itemField.getText(), "€ " + amountField.getText(), datePicker
                        .getValue().toString()));

                itemCol.setCellValueFactory(new PropertyValueFactory<Record, String>("item"));
                amountCol.setCellValueFactory(new PropertyValueFactory<Record, String>("amount"));
                dateCol.setCellValueFactory(new PropertyValueFactory<Record, String>("date"));

            } catch (NullPointerException | NumberFormatException e) {
            }

            recordUpdater.updateRecords(currentTimeRangeUpdater.getCurrentTimeRange());
            // persistentRecords();
        });
    }


    public void setClearButtonOnAction() {
        clearButton.setOnAction(event -> {
            itemField.clear();
            amountField.clear();
            datePicker.getEditor().clear();

            itemErrorText.setText("");
            amountErrorText.setText("");
            dateErrorText.setText("");
        });
    }

    public void setDeleteButtonOnAction() {
        deleteButton.setDisable(true);

        deleteButton.setOnAction(event -> {

            records.remove(recordTable.getSelectionModel().getSelectedItem());

            recordUpdater.updateRecords(currentTimeRangeUpdater.getCurrentTimeRange());

            recordUpdater.updateTotalAmount(records);

            deleteButton.setDisable(true);

            // persistentRecords();
        });
    }

    public void setRadioButtonOnAction() {
        filterBoxRadioButton.setOnAction(event -> {
            currentTimeRangeUpdater.setCurrentTimeRange(filterBox.getSelectionModel().getSelectedItem());

            recordUpdater.updateRecords(currentTimeRangeUpdater.getCurrentTimeRange());
        });

        filterPickerRadioButton.setOnAction(event -> {
            currentTimeRangeUpdater.setCurrentTimeRange("Custom");

            recordUpdater.updateRecords(currentTimeRangeUpdater.getCurrentTimeRange());
        });
    }
}
