package roomescape.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.entity.Reservation;
import roomescape.entity.ReservationTime;
import roomescape.entity.Theme;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@JdbcTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class H2ReservationDaoTest {

    /**
     * data.sql에 초기값이 설정되어 있습니다.
     * 예약 데이터가 6개 들어가있고, 테스트 전 data.sql 확인바랍니다.
     */

    private ReservationDao reservationDao;

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        reservationDao = new H2ReservationDao(jdbcTemplate);
    }

    @Test
    void 모든_예약을_조회한다() {
        List<Reservation> allReservations = reservationDao.findAll();

        assertThat(allReservations).hasSize(6);
    }

    @Test
    void 예약을_추가한다() {
        Reservation reservation = new Reservation("드라고", LocalDate.now().plusDays(1),
            new ReservationTime(1L, LocalTime.of(10, 0)),
            new Theme(1L, "", "", ""));

        Reservation saved = reservationDao.save(reservation);

        Reservation expected = new Reservation(7L, "드라고", LocalDate.now().plusDays(1),
            new ReservationTime(1L, LocalTime.of(10, 0)),
            new Theme(1L, "", "", ""));
        assertThat(saved).isEqualTo(expected);
    }

    @Test
    void 예약을_삭제한다() {
        reservationDao.deleteById(1L);

        List<Reservation> all = reservationDao.findAll();
        assertThat(all).hasSize(5);
    }

    @Test
    void id로_예약을_조회한다() {
        Optional<Reservation> findReservation = reservationDao.findById(1L);

        assertThat(findReservation.get())
            .isEqualTo(new Reservation(1L, "드라고", LocalDate.now().plusDays(1),
                new ReservationTime(1L, LocalTime.of(10, 0)),
                new Theme(1L, "", "", "")));
    }

    @Test
    void 같은_time_id를_가진_예약이_존재하는지_확인한다() {
        assertAll(
            () -> assertThat(reservationDao.isExistByTimeId(1L)).isTrue(),
            () -> assertThat(reservationDao.isExistByTimeId(10L)).isFalse()
        );
    }

    @Test
    void date와_time_id가_존재하는지_확인한다() {
        assertAll(
            () -> assertThat(reservationDao.isExist(LocalDate.of(2025, 4, 24), 1L)).isTrue(),
            () -> assertThat(reservationDao.isExist(LocalDate.of(2026, 4, 24), 1L)).isFalse()
        );
    }

    @Test
    void date와_theme_id가_일치하는_예약을_조회한다() {
        List<Reservation> findReservation = reservationDao.findByDateAndThemeId(LocalDate.of(2025, 4, 25), 2L);

        List<Reservation> expected = List.of(new Reservation(2L, "", LocalDate.of(2025, 4, 25),
            new ReservationTime(2L, LocalTime.of(12, 0)),
            new Theme(2L, "", "", "")));
        assertThat(findReservation).isEqualTo(expected);
    }
}