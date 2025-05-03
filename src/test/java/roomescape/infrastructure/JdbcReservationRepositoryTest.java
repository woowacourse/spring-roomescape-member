package roomescape.infrastructure;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.business.model.entity.Reservation;
import roomescape.business.model.entity.ReservationTime;
import roomescape.business.model.entity.Theme;
import roomescape.test_util.JdbcTestUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@JdbcTest
@Import({JdbcReservationRepository.class, JdbcReservationTimeRepository.class, JdbcThemeRepository.class})
class JdbcReservationRepositoryTest {

    private static final LocalDate DATE1 = LocalDate.now().plusDays(5);
    private static final LocalDate DATE2 = LocalDate.now().plusDays(7);
    private static final LocalTime TIME = LocalTime.of(10, 0);

    private final JdbcReservationRepository sut;
    private final JdbcReservationTimeRepository reservationTimeRepository;
    private final JdbcThemeRepository themeRepository;
    private final JdbcTestUtil testUtil;

    @Autowired
    public JdbcReservationRepositoryTest(final JdbcReservationRepository sut, final JdbcReservationTimeRepository reservationTimeRepository, final JdbcThemeRepository themeRepository, final JdbcTemplate jdbcTemplate) {
        this.sut = sut;
        this.reservationTimeRepository = reservationTimeRepository;
        this.themeRepository = themeRepository;
        this.testUtil = new JdbcTestUtil(jdbcTemplate);
    }

    @AfterEach
    void tearDown() {
        testUtil.deleteAll();
    }

    @Test
    void 예약을_저장할_수_있다() {
        final ReservationTime time = reservationTimeRepository.save(ReservationTime.beforeSave(TIME));
        final Theme theme = themeRepository.save(Theme.beforeSave("호러", "", ""));

        final Reservation result = sut.save(Reservation.beforeSave("돔푸", DATE1, time, theme));

        assertThat(result).isNotNull();
    }

    @Test
    void 모든_예약을_찾을_수_있다() {
        final long timeId = testUtil.insertTheme("호러");
        final long themeId = testUtil.insertReservationTime(TIME);
        final long id1 = testUtil.insertReservation("돔푸", DATE1, timeId, themeId);
        final long id2 = testUtil.insertReservation("레몬", DATE2, timeId, themeId);

        final List<Reservation> result = sut.findAll();

        assertThat(result.size()).isEqualTo(2);
        assertThat(result.get(0).getId()).isEqualTo(id1);
        assertThat(result.get(1).getId()).isEqualTo(id2);
    }

    @Test
    void ID_기준으로_예약을_찾을_수_있다() {
        final long timeId = testUtil.insertTheme("호러");
        final long themeId = testUtil.insertReservationTime(TIME);
        final long id = testUtil.insertReservation("돔푸", DATE1, timeId, themeId);

        final Optional<Reservation> result = sut.findById(id);

        assertThat(result.isPresent()).isTrue();
        final Reservation reservation = result.get();
        assertThat(reservation.getId()).isEqualTo(id);
        assertThat(reservation.getDate()).isEqualTo(DATE1);
        assertThat(reservation.getTime().getId()).isEqualTo(timeId);
        assertThat(reservation.getTheme().getId()).isEqualTo(themeId);
    }

    @Test
    void ID_기준으로_존재하는지_확인할_수_있다() {
        final long timeId = testUtil.insertTheme("호러");
        final long themeId = testUtil.insertReservationTime(TIME);
        final long id = testUtil.insertReservation("돔푸", DATE1, timeId, themeId);

        final boolean result = sut.existById(id);

        assertThat(result).isTrue();
    }

    @Test
    void 시간_ID_기준으로_존재하는지_확인할_수_있다() {
        final long timeId = testUtil.insertTheme("호러");
        final long themeId = testUtil.insertReservationTime(TIME);
        testUtil.insertReservation("돔푸", DATE1, timeId, themeId);

        final boolean result = sut.existByTimeId(timeId);

        assertThat(result).isTrue();
    }

    @Test
    void 테마_ID_기준으로_존재하는지_확인할_수_있다() {
        final long timeId = testUtil.insertTheme("호러");
        final long themeId = testUtil.insertReservationTime(TIME);
        testUtil.insertReservation("돔푸", DATE1, timeId, themeId);

        final boolean result = sut.existByThemeId(themeId);

        assertThat(result).isTrue();
    }

    @Test
    void 시간_날짜_테마_기준으로_존재하는지_확인할_수_있다() {
        final long timeId = testUtil.insertReservationTime(TIME);
        final Theme theme = themeRepository.save(Theme.beforeSave("호러", "", ""));
        testUtil.insertReservation("돔푸", DATE1, timeId, theme.getId());

        final boolean result = sut.isDuplicateDateAndTimeAndTheme(DATE1, TIME, theme);

        assertThat(result).isTrue();
    }

    @Test
    void ID를_통해_예약을_삭제할_수_있다() {
        final long timeId = testUtil.insertTheme("호러");
        final long themeId = testUtil.insertReservationTime(TIME);
        final long id = testUtil.insertReservation("돔푸", DATE1, timeId, themeId);

        sut.deleteById(id);

        assertThat(testUtil.countReservation()).isEqualTo(0);
    }
}
