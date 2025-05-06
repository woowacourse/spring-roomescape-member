package roomescape.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import roomescape.domain.Person;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationDate;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;

@Sql(scripts = {"/test-schema.sql"})
@SpringBootTest
public class JdbcReservationDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ThemeDao themeDao;

    @Autowired
    private ReservationTimeDao reservationTimeDao;

    @Autowired
    private ReservationDao reservationDao;

    private Theme theme;
    private ReservationTime reservationTime;
    private Reservation reservation;

    @BeforeEach
    void init() {
        theme = new Theme("테마1", "공포테마입니다", "http://aaa");
        long savedThemeId = themeDao.saveTheme(theme);
        theme.setId(savedThemeId);
        reservationTime = new ReservationTime(LocalTime.of(10, 0));
        long savedReservationTimeId = reservationTimeDao.saveReservationTime(reservationTime);
        reservationTime.setId(savedReservationTimeId);
        reservation = new Reservation(
            new Person("james"),
            new ReservationDate(LocalDate.of(2025, 12, 25)),
            reservationTime,
            theme);
    }

    @AfterEach
    void remove() {
        String query1 = "delete from reservation";
        String query2 = "delete from reservation_time";
        String query3 = "delete from theme";
        jdbcTemplate.update(query1);
        jdbcTemplate.update(query2);
        jdbcTemplate.update(query3);
    }

    @DisplayName("reservation 객체가 주어졌을 때, db에 저장되며 id값을 반환해야 한다.")
    @Test
    void given_reservation_then_save_db_and_set_id() {
        long savedReservationId = reservationDao.saveReservation(reservation);
        assertThat(reservationDao.findAllReservation().get(0).getId()).isEqualTo(
            savedReservationId);
        assertThat(reservationDao.findAllReservation().size()).isEqualTo(1);
    }

    @DisplayName("db에 존재하는 모든 reservation을 가져올 수 있어야 한다.")
    @Test
    void get_all_reservation() {
        Reservation reservation1 = new Reservation(new Person("james"),
            new ReservationDate(LocalDate.of(2025, 12, 25)),
            reservationTime, theme);
        reservationDao.saveReservation(reservation1);
        Reservation reservation2 = new Reservation(new Person("james"),
            new ReservationDate(LocalDate.of(2025, 12, 26)),
            reservationTime, theme);
        reservationDao.saveReservation(reservation2);
        Reservation reservation3 = new Reservation(new Person("james"),
            new ReservationDate(LocalDate.of(2025, 12, 27)),
            reservationTime, theme);
        reservationDao.saveReservation(reservation3);

        assertThat(reservationDao.findAllReservation().size()).isEqualTo(3);
    }

    @DisplayName("reservationId가 주어졌을 때, 해당하는 데이터를 삭제해야 한다.")
    @Test
    void given_reservation_id_then_delete_data() {
        //given
        Reservation reservation = new Reservation(new Person("james"),
            new ReservationDate(LocalDate.of(2025, 12, 25)),
            reservationTime, theme);
        long savedId = reservationDao.saveReservation(reservation);

        //when
        reservationDao.deleteReservation(savedId);

        //then
        assertThat(reservationDao.findAllReservation().size()).isEqualTo(0);
    }

    @DisplayName("예약 id가 주어졌을 때, 해당하는 예약을 반환해야 한다.")
    @Test
    void given_reservation_id_then_return_reservation() {
        long savedId = reservationDao.saveReservation(reservation);
        Optional<Reservation> reservation = reservationDao.findById(savedId);
        assertThat(reservation).isNotNull();
        assertThat(reservation.get().getId()).isEqualTo(savedId);
    }

    @DisplayName("예약 id가 주어졌을 때, 존재하지 않는 id라면, 빈 값을 반환해야 한다.")
    @Test
    void given_reservation_id_then_return_null() {
        reservationDao.saveReservation(reservation);
        Optional<Reservation> reservation = reservationDao.findById(99999999999L);
        assertThat(reservation).isEmpty();
    }

    @DisplayName("주어진 id가 존재하는지 셀 수 있어야 한다.")
    @Test
    void count_reservation_by_id() {
        long savedId = reservationDao.saveReservation(reservation);
        int result = reservationDao.countAlreadyExistReservation(savedId);
        assertThat(result).isEqualTo(1);
    }

    @DisplayName("주어진 날짜+시간에 예약이 존재하는 지 셀 수 있어야 한다.")
    @Test
    void count_reservation_of_date_and_time() {
        reservationDao.saveReservation(reservation);
        int result = reservationDao.countAlreadyReservationOf(reservation.getReservationDate(),
            reservation.getTimeId());
        assertThat(result).isEqualTo(1);
    }
}
