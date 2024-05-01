package roomescape.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ReservationDaoImplTest {

    private static final LocalDate DATE_AFTER_TWO = LocalDate.now().plusDays(2);
    private static final LocalDate DATE_AFTER_THREE = LocalDate.now().plusDays(3);

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private ReservationDaoImpl reservationDaoImpl;

    @BeforeEach
    void setUp() {
        jdbcTemplate.update("insert into reservation_time values(1,'10:00')");
        jdbcTemplate.update("insert into theme(name, description, thumbnail) values(?,?,?)", "리비", "머리 쓰는 중",
                "url");
        jdbcTemplate.update("insert into reservation (name, date, time_id, theme_id) values(?,?,?,?)", "브라운",
                DATE_AFTER_TWO, 1L, 1L);
    }

    @AfterEach
    void setDown() {
        jdbcTemplate.update("delete from reservation");
    }

    @DisplayName("예약목록 모두를 불러옵니다.")
    @Test
    void should_findAll() {
        int expectedSize = 1;

        int actualSize = reservationDaoImpl.findAll().size();

        assertThat(actualSize).isEqualTo(expectedSize);
    }


    @DisplayName("원하는 ID의 예약을 불러옵니다.")
    @Test
    void should_findById() {
        ReservationTime reservationTime = new ReservationTime(1L, LocalTime.of(10, 0));
        Theme theme = new Theme(1L, "리비", "머리 쓰는 중", "url");
        Reservation expectedReservation = new Reservation(1L, "브라운", DATE_AFTER_TWO, reservationTime, theme);

        List<Reservation> all = reservationDaoImpl.findAll();
        System.out.println("all = " + all);
        assertThat(reservationDaoImpl.findById(1L)).isPresent();
    }

    @DisplayName("예약을 추가할 수 있습니다.")
    @Test
    void should_insert() {
        ReservationTime reservationTime = new ReservationTime(1L, LocalTime.of(10, 0));
        Theme theme = new Theme(1L, "리비", "머리 쓰는 중", "url");
        Reservation reservation = new Reservation(null, "도도", DATE_AFTER_THREE, reservationTime, theme);

        Reservation savedReservation = reservationDaoImpl.insert(reservation);

        assertThat(savedReservation.getId()).isNotNull();
    }

    @DisplayName("원하는 ID의 예약을 삭제할 수 있습니다.")
    @Test
    void should_deleteById() {
        int expectedCount = 0;

        reservationDaoImpl.deleteById(1L);
        int actualCount = jdbcTemplate.queryForObject("select count(*) from reservation where id = 1", Integer.class);

        assertThat(actualCount).isEqualTo(expectedCount);
    }

    @DisplayName("예약날짜와 예약 시간 ID가 동일한 경우를 알 수 있습니다.")
    @Test
    void should_return_true_when_reservation_date_and_time_id_equal() {
        assertThat(reservationDaoImpl.existByDateAndTimeId(DATE_AFTER_TWO, 1L)).isTrue();
    }

    @DisplayName("예약날짜와 예약 시간 ID가 동일하지 않은 경우를 알 수 있습니다.")
    @Test
    void should_return_false_when_reservation_date_and_time_id_not_equal() {
        assertThat(reservationDaoImpl.existByDateAndTimeId(DATE_AFTER_THREE, 1L)).isFalse();
    }
}
