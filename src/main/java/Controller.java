import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;

import java.net.URL;
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

    private final ObservableList<String> timeRanges = FXCollections.observableArrayList("All", "Weekly", "Monthly");

    private int totalAmount;

    private final RecordsPersistence recordsPersistence;

    public Controller() {
        recordsPersistence = null;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initTimeRangeFilter();

        recordTable.setEditable(true);

        setAddAction();
        setClearAction();
        setDeleteAction();
    }

    private void initTimeRangeFilter() {
        timeRangeFilter.setItems(timeRanges);
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

                records.add(new Record(itemField.getText(), Integer.parseInt(amountField.getText()), datePicker
                        .getValue().toString()));

                itemCol.setCellValueFactory(new PropertyValueFactory<Record, String>("item"));
                amountCol.setCellValueFactory(new PropertyValueFactory<Record, Integer>("amount"));
                dateCol.setCellValueFactory(new PropertyValueFactory<Record, String>("date"));

                recordTable.setItems(records);
            } catch (NullPointerException | NumberFormatException e) {
            }

            updateTotalAmount();
            persistentRecords();
        });
    }

    private void setDeleteAction() {
        deleteButton.setOnAction(event -> {
            records.remove(recordTable.getSelectionModel().getSelectedItem());

            recordTable.setItems(records);

            updateTotalAmount();
            persistentRecords();
        });
    }

    private void updateTotalAmount() {
        totalAmount = 0;

        records.forEach(record -> totalAmount += record.getAmount());

        totalAmountText.setText("€ " + Integer.toString(totalAmount));
    }

    private void persistentRecords() {
        List<Record> plainRecordList = convertToPlainList(records);

        recordsPersistence.persistent(plainRecordList);
    }

    private List<Record> convertToPlainList(ObservableList<Record> records) {
        return Collections.emptyList();
    }
}
