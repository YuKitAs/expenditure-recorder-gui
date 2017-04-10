package expenditure.recorder.gui.view;

import java.net.URL;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ResourceBundle;

import expenditure.recorder.gui.model.Record;
import expenditure.recorder.gui.view.configuration.ConfigurationFileReader;
import expenditure.recorder.gui.view.configuration.ExpenditureRecorderGuiConfiguration;
import expenditure.recorder.gui.viewmodel.ExpenditureRecordViewModel;
import expenditure.recorder.gui.viewmodel.RecordTableItem;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;

public class ExpenditureRecordView implements Initializable {
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
    private TableView<RecordTableItem> recordTable;
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

    private final ObservableList<String> timeRanges = FXCollections.observableArrayList("All", "Today", "Last 7 Days", "Last 30 Days");

    private static final String HOME_FOLDER_PATH = System.getProperty("user.home");
    private static final String CONFIG_FILE_NAME = ".expenditure-recorder-gui.conf";

    private final ExpenditureRecordViewModel expenditureRecordViewModel = new ExpenditureRecordViewModel(
            ExpenditureRecorderGuiConfiguration.from(ConfigurationFileReader.getFile(Paths.get(HOME_FOLDER_PATH, CONFIG_FILE_NAME))));

    public ExpenditureRecordView() {
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Binding
        itemField.textProperty().bindBidirectional(expenditureRecordViewModel.getItemTextProperty());
        amountField.textProperty().bindBidirectional(expenditureRecordViewModel.getAmountTextProperty());
        datePicker.valueProperty().bindBidirectional(expenditureRecordViewModel.getDateProperty());

        itemErrorText.textProperty().bind(expenditureRecordViewModel.getItemErrorTextProperty());
        amountErrorText.textProperty().bind(expenditureRecordViewModel.getAmountErrorTextProperty());
        dateErrorText.textProperty().bind(expenditureRecordViewModel.getDateErrorTextProperty());

        recordTable.itemsProperty().bindBidirectional(expenditureRecordViewModel.getRecordTableProperty());

        filterBox.selectionModelProperty().bindBidirectional(expenditureRecordViewModel.getFilterBoxProperty());

        filterBoxRadioButton.selectedProperty().bindBidirectional(expenditureRecordViewModel.getFilterBoxRadioButtonSelectedProperty());
        filterPickerRadioButton.selectedProperty()
                .bindBidirectional(expenditureRecordViewModel.getFilterPickerRadioButtonSelectedProperty());

        fromDatePicker.valueProperty().bindBidirectional(expenditureRecordViewModel.getFromDateProperty());
        toDatePicker.valueProperty().bindBidirectional(expenditureRecordViewModel.getToDateProperty());

        totalAmountText.textProperty().bind(expenditureRecordViewModel.getTotalAmountTextProperty());

        // Init
        itemCol.setCellValueFactory(new PropertyValueFactory<Record, String>("item"));
        amountCol.setCellValueFactory(new PropertyValueFactory<Record, String>("amount"));
        dateCol.setCellValueFactory(new PropertyValueFactory<Record, String>("date"));
        initRadioButton();
        initFilterBox();
        initDatePicker();

        // setOnActions
        addButton.setOnAction(event -> expenditureRecordViewModel.addRecord());
        clearButton.setOnAction(event -> expenditureRecordViewModel.clearInput());
        deleteButton.setOnAction(event -> expenditureRecordViewModel.deleteRecord(recordTable.getSelectionModel().getSelectedItem()));
        //deleteButton.disableProperty().bind(expenditureRecordViewModel.deleteButtonDisabledProperty());

        //filterBoxRadioButton.setOnAction(event -> expenditureRecordViewModel.filterBoxRadioButtonOnAction());
        //filterPickerRadioButton.setOnAction(event -> expenditureRecordViewModel.filterPickerRadioButtonOnAction());

        //filterBox.setOnAction(event -> expenditureRecordViewModel.filterBoxOnAction());

        //fromDatePicker.setOnAction(event -> expenditureRecordViewModel.fromDatePickerOnAction());
        //toDatePicker.setOnAction(event -> expenditureRecordViewModel.toDatePickerOnAction());
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
}
