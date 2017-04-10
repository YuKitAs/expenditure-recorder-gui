package expenditure.recorder.gui.viewmodel.filter;

import org.junit.Test;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class FilterPredicateTest {
    private FilterPredicate filterPredicate;
    private final static Instant TODAY = Instant.now();
    private final static Instant IN_LAST_7_DAYS = TODAY.minus(Duration.ofDays(6));
    private final static Instant IN_LAST_30_DAYS = TODAY.minus(Duration.ofDays(29));
    private final static Instant BEFORE_LAST_30_DAYS = TODAY.minus(Duration.ofDays(30));
    private final static LocalDate FROM_DATE = LocalDate.now().minusDays(3);
    private final static LocalDate TO_DATE = LocalDate.now().plusDays(3);

    @Test
    public void filterAllTimeRange() {
        filterPredicate = new FilterPredicate(TimeRange.ALL);

        assertThat(filterPredicate.timeRangeFilter(TODAY)).isTrue();
        assertThat(filterPredicate.timeRangeFilter(IN_LAST_7_DAYS)).isTrue();
        assertThat(filterPredicate.timeRangeFilter(IN_LAST_30_DAYS)).isTrue();
    }

    @Test
    public void filterTodayTimeRange() {
        filterPredicate = new FilterPredicate(TimeRange.TODAY);

        assertThat(filterPredicate.timeRangeFilter(TODAY)).isTrue();
        assertThat(filterPredicate.timeRangeFilter(IN_LAST_7_DAYS)).isFalse();
    }

    @Test
    public void filterLastSevenDaysTimeRange() {
        filterPredicate = new FilterPredicate(TimeRange.LAST_7_DAYS);

        assertThat(filterPredicate.timeRangeFilter(IN_LAST_7_DAYS)).isTrue();
        assertThat(filterPredicate.timeRangeFilter(IN_LAST_30_DAYS)).isFalse();
    }

    @Test
    public void filterLastThirtyDaysTimeRange() {
        filterPredicate = new FilterPredicate(TimeRange.LAST_30_DAYS);

        assertThat(filterPredicate.timeRangeFilter(IN_LAST_30_DAYS)).isTrue();
        assertThat(filterPredicate.timeRangeFilter(BEFORE_LAST_30_DAYS)).isFalse();
    }

    @Test
    public void customTimeRangeFilterWithNullFromDate() {
        filterPredicate = new FilterPredicate(TimeRange.CUSTOM);

        assertThat(filterPredicate.timeRangeFilter(null, TO_DATE, TODAY)).isTrue();
        assertThat(filterPredicate.timeRangeFilter(null, TO_DATE, TODAY.plus(Duration.ofDays(4)))).isFalse();
    }

    @Test
    public void customTimeRangeFilterWithNullToDate() {
        filterPredicate = new FilterPredicate(TimeRange.CUSTOM);

        assertThat(filterPredicate.timeRangeFilter(FROM_DATE, null, TODAY)).isTrue();
        assertThat(filterPredicate.timeRangeFilter(FROM_DATE, null, IN_LAST_7_DAYS)).isFalse();
    }

    @Test
    public void customTimeRangeFilter() {
        filterPredicate = new FilterPredicate(TimeRange.CUSTOM);

        assertThat(filterPredicate.timeRangeFilter(FROM_DATE, TO_DATE, TODAY)).isTrue();
        assertThat(filterPredicate.timeRangeFilter(FROM_DATE, TO_DATE, TODAY.plus(Duration.ofDays(4)))).isFalse();
        assertThat(filterPredicate.timeRangeFilter(FROM_DATE, TO_DATE, IN_LAST_7_DAYS)).isFalse();
    }
}