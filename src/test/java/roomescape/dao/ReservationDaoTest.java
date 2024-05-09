package roomescape.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationFactory;
import roomescape.domain.ReservationTheme;
import roomescape.domain.ReservationTime;

@JdbcTest
@Import({ReservationDao.class, ReservationTimeDao.class, ReservationThemeDao.class, ReservationFactory.class})
@Sql(scripts = {"/test_schema.sql"})
public class ReservationDaoTest {

    @Autowired
    private ReservationDao reservationDao;

    @Autowired
    private ReservationTimeDao timeDao;

    @Autowired
    private ReservationThemeDao themeDao;

    @Autowired
    private ReservationFactory reservationFactory;

    @BeforeEach
    void setUp() {
        timeDao.insert(new ReservationTime(null, LocalTime.parse("10:00")));
        themeDao.insert(new ReservationTheme(null, "theme1", "desc1", "thumb1"));
    }

    @DisplayName("예약을 추가한다.")
    @Test
    void insert() {
        // given
        Reservation inserted = reservationDao.insert(reservationFactory.createForAdd(
                "name",
                LocalDate.parse("2025-01-01"),
                new ReservationTime(1L, LocalTime.parse("10:00")),
                new ReservationTheme(1L, "theme_name", "desc", "thumb"))
        );

        // when
        Long insertedReservationId = inserted.getId();

        // then
        assertThat(insertedReservationId).isEqualTo(1L);
    }

    @DisplayName("모든 예약을 조회한다.")
    @Test
    void findAll() {
        // given
        reservationDao.insert(reservationFactory.createForAdd(
                "name",
                LocalDate.parse("2025-01-01"),
                new ReservationTime(1L, LocalTime.parse("10:00")),
                new ReservationTheme(1L, "theme_name", "desc", "thumb"))
        );

        // when
        List<Reservation> reservations = reservationDao.findAll();

        // then
        assertThat(reservations.size()).isEqualTo(1);
    }

    @DisplayName("ID를 이용하여 예약을 삭제한다.")
    @Test
    void deleteById() {
        // given
        Reservation inserted = reservationDao.insert(reservationFactory.createForAdd(
                "name",
                LocalDate.parse("2025-01-01"),
                new ReservationTime(1L, LocalTime.parse("10:00")),
                new ReservationTheme(1L, "theme_name", "desc", "thumb"))
        );

        // when
        int sizeBeforeDelete = reservationDao.findAll().size();
        reservationDao.deleteById(inserted.getId());
        int sizeAfterDelete = reservationDao.findAll().size();

        // then
        assertThat(sizeBeforeDelete).isEqualTo(1);
        assertThat(sizeAfterDelete).isEqualTo(0);
    }

    @DisplayName("특정 시간에 대한 예약이 존재하는지 확인한다.")
    @Test
    void hasReservationForTimeId() {
        // given
        Reservation inserted = reservationDao.insert(reservationFactory.createForAdd(
                "name",
                LocalDate.parse("2025-01-01"),
                new ReservationTime(1L, LocalTime.parse("10:00")),
                new ReservationTheme(1L, "theme_name", "desc", "thumb"))
        );

        // when
        Boolean isReservationExist = reservationDao.hasReservationForTimeId(inserted.getTime().getId());

        // then
        assertThat(isReservationExist).isTrue();
    }

    @DisplayName("특정 테마에 대한 예약이 존재하는지 확인한다.")
    @Test
    void hasReservationForThemeId() {
        // given
        Reservation inserted = reservationDao.insert(reservationFactory.createForAdd(
                "name",
                LocalDate.parse("2025-01-01"),
                new ReservationTime(1L, LocalTime.parse("10:00")),
                new ReservationTheme(1L, "theme_name", "desc", "thumb"))
        );

        // when
        Boolean isReservationExist = reservationDao.hasReservationForTimeId(inserted.getTheme().getId());

        // then
        assertThat(isReservationExist).isTrue();
    }

    @DisplayName("특정 날짜, 시간, 테마에 대한 예약이 존재하는지 확인한다.")
    @Test
    void hasSameReservation() {
        // given
        Reservation inserted = reservationDao.insert(reservationFactory.createForAdd(
                "name",
                LocalDate.parse("2025-01-01"),
                new ReservationTime(1L, LocalTime.parse("10:00")),
                new ReservationTheme(1L, "theme_name", "desc", "thumb"))
        );

        // when
        Boolean isReservationExist = reservationDao.hasSameReservation(inserted.getDate().toString(),
                inserted.getTime().getId(),
                inserted.getTheme().getId());

        // then
        assertThat(isReservationExist).isTrue();
    }
}
