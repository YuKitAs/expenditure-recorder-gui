import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.util.Callback;

import javax.swing.text.DateFormatter;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
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
    private ComboBox<String> timeRangeFilter;

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

    private ObservableList<Record> records = FXCollections.observableArrayList();

    private ObservableList<Record> filteredRecords = FXCollections.observableArrayList();

    private final ObservableList<String> timeRanges = FXCollections.observableArrayList(
            "All", "Today", "Last 7 Days", "Last 30 Days");

    private int totalAmount;

    private final RecordsPersistence recordsPersistence;

    public Controller() {
        recordsPersistence = null;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initDatePicker();
        initTimeRangeFilter();

        setAddAction();
        setClearAction();
        setDeleteAction();
        setFilterAction();
    }

    private void initDatePicker() {
        datePicker.setDayCellFactory(datePicker -> {
            return new DateCell() {
                @Override
                public void updateItem(LocalDate item, boolean empty) {
                    if (item.isAfter(LocalDate.now())) {
                        this.setDisable(true);
                    }
                }
            };
        });
    }

    private void initTimeRangeFilter() {
        timeRangeFilter.setItems(timeRanges);
    }

    private void setAddAction() {
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

                recordTable.setItems(records);
            } catch (NullPointerException | NumberFormatException e) {
            }

            updateTotalAmount(records);
            persistentRecords();
        });
    }

    private void setClearAction() {
        clearButton.setOnAction(event -> {
            itemField.clear();
            amountField.clear();
            datePicker.getEditor().clear();

            itemErrorText.setText("");
            amountErrorText.setText("");
            dateErrorText.setText("");
        });
    }

    private void setDeleteAction() {
        deleteButton.setOnAction(event -> {
            records.remove(recordTable.getSelectionModel().getSelectedItem());

            recordTable.setItems(records);

            updateTotalAmount(records);
            persistentRecords();
        });
    }

    private void setFilterAction() {
        timeRangeFilter.setOnAction(event -> {
            filteredRecords.clear();
            String timeRange = timeRangeFilter.getSelectionModel().getSelectedItem();

            switch (timeRange) {
                case "All":
                    recordTable.setItems(records);
                    updateTotalAmount(records);
                    break;
                case "Today":
                    records.forEach(record -> {
                        LocalDate date = LocalDate.parse(record.getDate());

                        if (date.equals(LocalDate.now(ZoneId.of("CET")))) {
                            filteredRecords.add(record);
                        }
                    });

                    recordTable.setItems(filteredRecords);
                    updateTotalAmount(filteredRecords);

                    break;
                case "Last 7 Days":
                    records.forEach(record -> {
                        LocalDate date = LocalDate.parse(record.getDate());

                        if (date.isAfter(LocalDate.now().minusWeeks(1))) {
                            filteredRecords.add(record);
                        }
                    });

                    recordTable.setItems(filteredRecords);
                    updateTotalAmount(filteredRecords);

                    break;
                case "Last 30 Days":
                    records.forEach(record -> {
                        LocalDate date = LocalDate.parse(record.getDate());

                        if (date.isAfter(LocalDate.now().minusMonths(1))) {
                            filteredRecords.add(record);
                        }
                    });

                    recordTable.setItems(filteredRecords);
                    updateTotalAmount(filteredRecords);

                    break;
            }
        });
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
