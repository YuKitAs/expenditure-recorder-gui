package expenditure.recorder.gui.viewmodel;

import expenditure.recorder.gui.model.Record;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.time.Month;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by yuka on 28.01.17.
 */
public class MainViewModelTest {
    MainViewModel viewModel = new MainViewModel();

    @Before
    public void setUp() {
        viewModel.setItemTextProperty("some item");
        viewModel.setAmountTextProperty("42");
        viewModel.setDateProperty(LocalDate.of(2017, Month.JANUARY, 1));
    }

    @Test
    public void addRecordWithCorrectRecord() {
        viewModel.addRecord();
        Record record = viewModel.getRecords().get(0);

        assertThat(record.getItem()).isEqualTo("some item");
        assertThat(record.getAmount()).isEqualTo("42");
        assertThat(record.getDate()).isEqualTo(LocalDate.of(2017, Month.JANUARY, 1).toString());
    }

    @Test
    public void clearInputWithClearedInput() {
        assertThat(viewModel.getItemTextProperty().get()).isNotEmpty();
        assertThat(viewModel.getAmountTextProperty().get()).isNotEmpty();
        assertThat(viewModel.getDateProperty().get()).isNotNull();

        viewModel.clearInput();

        assertThat(viewModel.getItemTextProperty().get()).isEmpty();
        assertThat(viewModel.getAmountTextProperty().get()).isEmpty();
        assertThat(viewModel.getDateProperty().get()).isNull();
    }

    @Test
    public void clearInputWithClearedErrorText() {
        viewModel.setItemErrorTextProperty("some item input error");
        viewModel.setAmountErrorTextProperty("some amount input error");
        viewModel.setDateErrorTextProperty("some date input error");

        assertThat(viewModel.getItemErrorTextProperty().get()).isNotEmpty();
        assertThat(viewModel.getAmountErrorTextProperty().get()).isNotEmpty();
        assertThat(viewModel.getDateErrorTextProperty().get()).isNotEmpty();

        viewModel.clearInput();

        assertThat(viewModel.getItemErrorTextProperty().get()).isEmpty();
        assertThat(viewModel.getAmountErrorTextProperty().get()).isEmpty();
        assertThat(viewModel.getDateErrorTextProperty().get()).isEmpty();
    }

}