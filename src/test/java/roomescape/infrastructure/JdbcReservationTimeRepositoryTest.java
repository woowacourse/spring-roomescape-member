package roomescape.infrastructure;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.business.model.entity.ReservationTime;
import roomescape.test_util.JdbcTestUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@JdbcTest
@Import(JdbcReservationTimeRepository.class)
class JdbcReservationTimeRepositoryTest {

    private static final LocalTime TIME1 = LocalTime.of(10, 0);
    private static final LocalTime TIME2 = LocalTime.of(15, 0);

    private final JdbcReservationTimeRepository sut;
    private final JdbcTestUtil testUtil;

    @Autowired
    public JdbcReservationTimeRepositoryTest(final JdbcReservationTimeRepository sut, final JdbcTemplate jdbcTemplate) {
        this.sut = sut;
        this.testUtil = new JdbcTestUtil(jdbcTemplate);
    }

    @AfterEach
    void tearDown() {
        testUtil.deleteAll();
    }

    @Test
    void 예약_시간을_저장할_수_있다() {
        final ReservationTime result = sut.save(ReservationTime.beforeSave(TIME1));

        assertThat(result).isNotNull();
    }

    @Test
    void 모든_예약_시간을_찾을_수_있다() {
        final long id1 = testUtil.insertReservationTime(TIME1);
        final long id2 = testUtil.insertReservationTime(TIME2);

        final List<ReservationTime> result = sut.findAll();

        assertThat(result.size()).isEqualTo(2);
        assertThat(result.get(0).getId()).isEqualTo(id1);
        assertThat(result.get(1).getId()).isEqualTo(id2);
    }

    @Test
    void 해당_날짜와_테마에_예약되지_않은_모든_예약_시간을_찾을_수_있다() {
        final long timeId1 = testUtil.insertReservationTime(LocalTime.of(10, 0));
        final long timeId2 = testUtil.insertReservationTime(LocalTime.of(12, 0));
        final long timeId3 = testUtil.insertReservationTime(LocalTime.of(14, 0));
        final long themeId = testUtil.insertTheme("주홍색 연구");
        testUtil.insertReservation("돔푸", LocalDate.now().plusDays(10), timeId1, themeId);

        final List<ReservationTime> result = sut.findAvailableReservationTimesByDateAndThemeId(LocalDate.now().plusDays(10), themeId);

        assertThat(result.size()).isEqualTo(2);
        assertThat(result.get(0).getId()).isEqualTo(timeId2);
        assertThat(result.get(1).getId()).isEqualTo(timeId3);
    }

    @Test
    void ID_기준으로_예약_시간을_찾을_수_있다() {
        final long id = testUtil.insertReservationTime(TIME1);

        final Optional<ReservationTime> result = sut.findById(id);

        assertThat(result.isPresent()).isTrue();
        final ReservationTime reservationTime = result.get();
        assertThat(reservationTime.getId()).isEqualTo(id);
        assertThat(reservationTime.getStartAt()).isEqualTo(TIME1);
    }

    @Test
    void ID_기준으로_존재하는지_확인할_수_있다() {
        final long id = testUtil.insertReservationTime(TIME1);

        final boolean result = sut.existById(id);

        assertThat(result).isTrue();
    }

    @Test
    void 시간_기준으로_존재하는지_확인할_수_있다() {
        testUtil.insertReservationTime(TIME1);

        final boolean result = sut.existByTime(TIME1);

        assertThat(result).isTrue();
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 29})
    void 두_시간_사이에_존재하는지_확인할_수_있다(int minute) {
        testUtil.insertReservationTime(LocalTime.of(10, minute));

        final boolean result = sut.existBetween(LocalTime.of(10, 0), LocalTime.of(10, 30));

        assertThat(result).isTrue();
    }

    @Test
    void ID를_통해_예약_시간을_삭제할_수_있다() {
        final long id = testUtil.insertReservationTime(TIME1);

        sut.deleteById(id);

        assertThat(testUtil.countReservationTime()).isEqualTo(0);
    }
}
