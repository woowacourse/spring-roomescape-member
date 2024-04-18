package roomescape.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalTime;
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
import roomescape.dto.ReservationAddRequest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ReservationDaoImplTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private ReservationDaoImpl reservationDaoImpl;

    @BeforeEach
    void setUp() {
        jdbcTemplate.update("insert into reservation_time values(1,'10:00')");
        jdbcTemplate.update("insert into reservation (name, date, time_id) values('브라운','2020-12-12',1)");
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
        Reservation expectedReservation = new Reservation(1L, "브라운", LocalDate.of(2020, 12, 12), reservationTime);

        Reservation actualReservation = reservationDaoImpl.findById(1L).get();

        assertThat(actualReservation).isEqualTo(expectedReservation);
    }

    @DisplayName("예약을 추가하고 예약정보를 반환합니다.")
    @Test
    void should_insert() {
        ReservationTime reservationTime = new ReservationTime(1L, LocalTime.of(10, 0));
        Reservation expectedReservation = new Reservation(2L, "도도", LocalDate.of(2020, 11, 11), reservationTime);

        Reservation actualReservation = reservationDaoImpl.insert(
                new ReservationAddRequest(LocalDate.of(2020, 11, 11), "도도", 1L));

        assertThat(actualReservation).isEqualTo(expectedReservation);
    }

    @DisplayName("원하는 ID의 예약을 삭제할 수 있습니다.")
    @Test
    void should_deleteById() {
        int expectedCount = 0;

        reservationDaoImpl.deleteById(1L);
        int actualCount = jdbcTemplate.queryForObject("select count(*) from reservation where id = 1", Integer.class);

        assertThat(actualCount).isEqualTo(expectedCount);
    }
}
