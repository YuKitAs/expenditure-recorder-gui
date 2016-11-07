import expenditure.recorder.gui.model.Record;
import expenditure.recorder.gui.model.TimeRange;
import expenditure.recorder.gui.view.Controller;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ControllerTest {
    private Controller controller;

    private Method method;

    private Field field;

    private final ObservableList<Record> records = FXCollections.observableArrayList(
            new Record("foo", "€ 1", "2012-01-01"),
            new Record("bar", "€ 42", "2016-11-06"));

    @Before
    public void setUp() throws NoSuchMethodException {
        controller = new Controller();
    }

    @Test
    public void setCurrentTimeRange_WithCorrectTimeRange() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, NoSuchFieldException {
        method = Controller.class.getDeclaredMethod("setCurrentTimeRange", String.class);
        method.setAccessible(true);

        field = Controller.class.getDeclaredField("currentTimeRange");
        field.setAccessible(true);

        method.invoke(controller, "All");
        assertThat(field.get(controller)).isEqualTo(TimeRange.ALL);

        method.invoke(controller, "Today");
        assertThat(field.get(controller)).isEqualTo(TimeRange.TODAY);

        method.invoke(controller, "Last 7 Days");
        assertThat(field.get(controller)).isEqualTo(TimeRange.LAST_7_DAYS);

        method.invoke(controller, "Last 30 Days");
        assertThat(field.get(controller)).isEqualTo(TimeRange.LAST_30_DAYS);
    }

    @Test
    public void convertToPlainList_WithCorrectRecords() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        method = Controller.class.getDeclaredMethod("convertToPlainList", ObservableList.class);
        method.setAccessible(true);

        List<Record> plainRecordList = (ArrayList<Record>) method.invoke(controller, records);

        Record rec1 = plainRecordList.get(0);
        Record rec2 = plainRecordList.get(1);

        assertThat(rec1.getItem()).isEqualTo("foo");
        assertThat(rec1.getAmount()).isEqualTo("€ 1");
        assertThat(rec1.getDate()).isEqualTo("2012-01-01");

        assertThat(rec2.getItem()).isEqualTo("bar");
        assertThat(rec2.getAmount()).isEqualTo("€ 42");
        assertThat(rec2.getDate()).isEqualTo("2016-11-06");
    }
}
