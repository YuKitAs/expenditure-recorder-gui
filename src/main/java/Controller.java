import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;

import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML
    private javafx.scene.control.TextField itemField;

    @FXML
    private TextField amountField;

    @FXML
    private DatePicker datePicker;

    @FXML
    private Button addButton;

    @FXML
    private Button clearButton;

    @FXML
    private Button deleteButton;

    @FXML
    private ComboBox<String> filterBox;

    @FXML
    private DatePicker fromDatePicker;

    @FXML
    private DatePicker toDatePicker;

    @FXML
    private TableView<Record> recordTable;

    @FXML
    private TableColumn itemCol;

    @FXML
    private TableColumn amountCol;

    @FXML
    private TableColumn dateCol;

    @FXML
    private Text totalAmountText;

    @FXML
    private Text itemErrorText;

    @FXML
    private Text amountErrorText;

    @FXML
    private Text dateErrorText;

    @FXML
    private Text deleteErrorText;

    @FXML
    private RadioButton filterBoxRadioButton;

    @FXML
    private RadioButton filterPickerRadioButton;

    private final ToggleGroup radioButtonGroup = new ToggleGroup();

    private ObservableList<Record> records = FXCollections.observableArrayList();

    private ObservableList<Record> filteredRecords = FXCollections.observableArrayList();

    private TimeRange currentTimeRange = TimeRange.ALL;

    private final ObservableList<String> timeRanges = FXCollections.observableArrayList(
            "All", "Today", "Last 7 Days", "Last 30 Days");

    private int totalAmount;

    private LocalDate fromDate = null;

    private LocalDate toDate = null;

    private final RecordsPersistence recordsPersistence;

    public Controller() {
        recordsPersistence = null;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initDatePicker();
        initFilterBox();
        initRadioButton();

        setAddButtonAction();
        setClearButtonAction();
        setDeleteButtonAction();
        setFilterBoxAction();
        setRadioButtonAction();
        setFromDatePickerAction();
        setToDatePickerAction();
    }

    private void filterDateInDatePicker(DatePicker filteredDatePicker, LocalDate dateLimit, boolean dateAfterAllowed) {
        if (dateLimit == null) {
            return;
        }

        filteredDatePicker.setDayCellFactory(dp -> new DateCell() {
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                if (item.isAfter(dateLimit) && !dateAfterAllowed) {
                    this.setDisable(true);
                } else if (item.isBefore(dateLimit) && dateAfterAllowed) {
                    this.setDisable(true);
                }
            }
        });
    }

    private void initDatePicker() {
        filterDateInDatePicker(datePicker, LocalDate.now(), false);
    }

    private void initFilterBox() {
        filterBox.setItems(timeRanges);
        filterBox.getSelectionModel().selectFirst();
    }

    private void initRadioButton() {
        filterBoxRadioButton.setToggleGroup(radioButtonGroup);
        filterPickerRadioButton.setToggleGroup(radioButtonGroup);
    }

    private void setAddButtonAction() {
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

            updateRecords();
            // persistentRecords();
        });
    }

    private void setClearButtonAction() {
        clearButton.setOnAction(event -> {
            itemField.clear();
            amountField.clear();
            datePicker.getEditor().clear();

            itemErrorText.setText("");
            amountErrorText.setText("");
            dateErrorText.setText("");
        });
    }

    private void setDeleteButtonAction() {
        deleteButton.setOnAction(event -> {
            if (recordTable.getSelectionModel().getSelectedItem() == null) {
                deleteErrorText.setText("Please select a record.");
                return;
            }

            records.remove(recordTable.getSelectionModel().getSelectedItem());

            updateRecords();

            updateTotalAmount(records);

            // persistentRecords();
        });
    }

    private void setRadioButtonAction() {
        filterBoxRadioButton.setOnAction(event -> {
            setCurrentTimeRange(filterBox.getSelectionModel().getSelectedItem());

            updateRecords();
        });

        filterPickerRadioButton.setOnAction(event -> {
            setCurrentTimeRange("Custom");

            updateRecords();
        });
    }

    private void setFilterBoxAction() {
        filterBox.setOnAction(event -> {
            if (filterBoxRadioButton.isSelected()) {
                setCurrentTimeRange(filterBox.getSelectionModel().getSelectedItem());

                updateRecords();
            }
        });
    }

    private void setFromDatePickerAction() {
        fromDatePicker.setOnAction(event -> {
            fromDate = fromDatePicker.getValue();

            if (filterPickerRadioButton.isSelected()) {
                updateRecords();
            }
        });
    }

    private void setToDatePickerAction() {
        toDatePicker.setOnAction(event -> {
            toDate = toDatePicker.getValue();

            if (filterPickerRadioButton.isSelected()) {
                updateRecords();
            }
        });
    }

    private void setCurrentTimeRange(String timeRange) {
        switch (timeRange) {
            case "All":
                currentTimeRange = TimeRange.ALL;
                break;
            case "Today":
                currentTimeRange = TimeRange.TODAY;
                break;
            case "Last 7 Days":
                currentTimeRange = TimeRange.LAST_7_DAYS;
                break;
            case "Last 30 Days":
                currentTimeRange = TimeRange.LAST_30_DAYS;
                break;
            case "Custom":
                currentTimeRange = TimeRange.CUSTOM;
                break;
        }
    }

    private void updateRecords() {
        filteredRecords.clear();

        if (currentTimeRange.equals(TimeRange.ALL)) {
            recordTable.setItems(records);
            updateTotalAmount(records);
        } else if (currentTimeRange.equals(TimeRange.TODAY)) {
            records.forEach(record -> {
                LocalDate date = LocalDate.parse(record.getDate());

                if (date.equals(LocalDate.now(ZoneId.of("CET")))) {
                    filteredRecords.add(record);
                }
            });

            recordTable.setItems(filteredRecords);

            updateTotalAmount(filteredRecords);
        } else if (currentTimeRange.equals(TimeRange.LAST_7_DAYS)) {
            records.forEach(record -> {
                LocalDate date = LocalDate.parse(record.getDate());

                if (date.isAfter(LocalDate.now().minusWeeks(1))) {
                    filteredRecords.add(record);
                }
            });

            recordTable.setItems(filteredRecords);

            updateTotalAmount(filteredRecords);
        } else if (currentTimeRange.equals(TimeRange.LAST_30_DAYS)) {
            records.forEach(record -> {
                LocalDate date = LocalDate.parse(record.getDate());

                if (date.isAfter(LocalDate.now().minusMonths(1))) {
                    filteredRecords.add(record);
                }
            });

            recordTable.setItems(filteredRecords);

            updateTotalAmount(filteredRecords);
        } else if (currentTimeRange.equals(TimeRange.CUSTOM)) {
            if (fromDate == null && toDate == null) {
                recordTable.setItems(records);
                updateTotalAmount(records);

                return;
            }

            filterDateInDatePicker(fromDatePicker, toDate, false);
            filterDateInDatePicker(toDatePicker, fromDate, true);

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

            recordTable.setItems(filteredRecords);

            updateTotalAmount(filteredRecords);
        }
    }

    private void updateTotalAmount(ObservableList<Record> records) {
        totalAmount = 0;

        records.forEach(record -> totalAmount += Integer.parseInt(record.getAmount().substring(2)));

        totalAmountText.setText("€ " + Integer.toString(totalAmount));
    }

    private void persistentRecords() {
        List<Record> plainRecordList = convertToPlainList(records);

        recordsPersistence.persistent(plainRecordList);
    }

    private List<Record> convertToPlainList(ObservableList<Record> records) {
        List<Record> plainRecordList = new ArrayList<>();

        plainRecordList.addAll(records);

        return plainRecordList;
    }
}
