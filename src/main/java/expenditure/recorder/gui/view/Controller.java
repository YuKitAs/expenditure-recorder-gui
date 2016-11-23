package expenditure.recorder.gui.view;

import expenditure.recorder.gui.model.Record;
import expenditure.recorder.gui.viewmodel.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML
    private TextField itemField;

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
    private RadioButton filterBoxRadioButton;

    @FXML
    private RadioButton filterPickerRadioButton;

    private final ToggleGroup radioButtonGroup = new ToggleGroup();

    private final ObservableList<String> timeRanges = FXCollections.observableArrayList("All", "Today", "Last 7 Days",
            "Last 30 Days");

    private MainViewModel mainViewModel = new MainViewModel();

    // private final RecordsPersistence recordsPersistence;

    public Controller() {
        // recordsPersistence = null;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Binding
        itemField.textProperty().bindBidirectional(mainViewModel.itemTextProperty());
        amountField.textProperty().bindBidirectional(mainViewModel.amountTextProperty());
        datePicker.valueProperty().bindBidirectional(mainViewModel.dateProperty());

        itemErrorText.textProperty().bind(mainViewModel.itemErrorTextProperty());
        amountErrorText.textProperty().bind(mainViewModel.amountErrorTextProperty());
        dateErrorText.textProperty().bind(mainViewModel.dateErrorTextProperty());

        recordTable.itemsProperty().bind(mainViewModel.recordTableProperty());

        filterBox.selectionModelProperty().bindBidirectional(mainViewModel.filterBoxProperty());

        filterBoxRadioButton.selectedProperty().bindBidirectional(mainViewModel.filterBoxRadioButtonSelectedProperty());
        filterPickerRadioButton.selectedProperty().bindBidirectional(mainViewModel.filterPickerRadioButtonSelectedProperty());

        fromDatePicker.valueProperty().bindBidirectional(mainViewModel.fromDateProperty());
        toDatePicker.valueProperty().bindBidirectional(mainViewModel.toDateProperty());

        totalAmountText.textProperty().bind(mainViewModel.totalAmountTextProperty());

        // Init
        initRadioButton();
        initFilterBox();
        initDatePicker();

        // setOnAction
        addButton.setOnAction(event -> {
            itemCol.setCellValueFactory(new PropertyValueFactory<Record, String>("item"));
            amountCol.setCellValueFactory(new PropertyValueFactory<Record, String>("amount"));
            dateCol.setCellValueFactory(new PropertyValueFactory<Record, String>("date"));

            mainViewModel.addRecord();
        });

        clearButton.setOnAction(event -> mainViewModel.clearInput());

        deleteButton.setOnAction(event -> mainViewModel.deleteRecord(recordTable.getSelectionModel().getSelectedItem()));
        //deleteButton.disableProperty().bind(mainViewModel.deleteButtonDisabledProperty());

        filterBoxRadioButton.setOnAction(event -> mainViewModel.filterBoxRadioButtonOnAction());
        filterPickerRadioButton.setOnAction(event -> mainViewModel.filterPickerRadioButtonOnAction());

        filterBox.setOnAction(event -> mainViewModel.filterBoxOnAction());

        fromDatePicker.setOnAction(event -> mainViewModel.fromDatePickerOnAction());
        toDatePicker.setOnAction(event -> mainViewModel.toDatePickerOnAction());
    }

    private void initFilterBox() {
        filterBox.setItems(timeRanges);
        filterBox.getSelectionModel().selectFirst();
    }

    private void initDatePicker() {
        filterDateInDatePicker(datePicker, LocalDate.now(), false);
        filterDateInDatePicker(fromDatePicker, toDatePicker.getValue(), false);
        filterDateInDatePicker(toDatePicker, fromDatePicker.getValue(), true);
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

    private void initRadioButton() {
        filterBoxRadioButton.setToggleGroup(radioButtonGroup);
        filterPickerRadioButton.setToggleGroup(radioButtonGroup);
    }

    /*private void persistentRecords() {
        List<Record> plainRecordList = convertToPlainList(records);

        // recordsPersistence.persistent(plainRecordList);
    }

    private List<Record> convertToPlainList(ObservableList<Record> records) {
        List<Record> plainRecordList = new ArrayList<>();

        plainRecordList.addAll(records);

        return plainRecordList;
    }*/
}
