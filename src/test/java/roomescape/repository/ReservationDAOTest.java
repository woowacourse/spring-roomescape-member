package roomescape.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.test.annotation.DirtiesContext;

import roomescape.model.Reservation;
import roomescape.model.ReservationTime;
import roomescape.model.Theme;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ReservationDAOTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private DataSource dataSource;

    private SimpleJdbcInsert reservationTimeInsertActor;
    private SimpleJdbcInsert reservationInsertActor;
    private SimpleJdbcInsert themeInsertActor;

    @BeforeEach
    void setUp() {
        jdbcTemplate.execute("TRUNCATE TABLE reservation RESTART IDENTITY");
        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY FALSE");
        jdbcTemplate.execute("TRUNCATE TABLE reservation_time RESTART IDENTITY");
        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY TRUE");

        reservationTimeInsertActor = new SimpleJdbcInsert(dataSource)
                .withTableName("reservation_time")
                .usingGeneratedKeyColumns("id");
        reservationInsertActor = new SimpleJdbcInsert(dataSource)
                .withTableName("reservation")
                .usingGeneratedKeyColumns("id");
        themeInsertActor = new SimpleJdbcInsert(dataSource)
                .withTableName("theme")
                .usingGeneratedKeyColumns("id");

        insertReservationTime("10:00");
        insertReservationTime("11:00");

        insertTheme("에버", "공포", "공포.jpg");
        insertTheme("배키", "스릴러", "스릴러.jpg");

        insertReservation("브라운", "2023-08-05", "1", "1");
        insertReservation("리사", "2023-08-01", "2", "2");
    }

    private void insertReservationTime(String startAt) {
        Map<String, Object> parameters = new HashMap<>(1);
        parameters.put("start_at", startAt);
        reservationTimeInsertActor.execute(parameters);
    }

    private void insertReservation(String name, String date, String timeId, String themeId) {
        Map<String, Object> parameters = new HashMap<>(3);
        parameters.put("name", name);
        parameters.put("date", date);
        parameters.put("time_id", timeId);
        parameters.put("theme_id", themeId);
        reservationInsertActor.execute(parameters);
    }

    private void insertTheme(String name, String description, String thumbnail) {
        Map<String, Object> parameters = new HashMap<>(3);
        parameters.put("name", name);
        parameters.put("description", description);
        parameters.put("thumbnail", thumbnail);
        themeInsertActor.execute(parameters);
    }

    @DisplayName("모든 예약을 조회한다")
    @Test
    void should_get_reservation() {
        List<Reservation> reservations = reservationRepository.getAllReservations();
        assertThat(reservations).hasSize(2);
    }

    @DisplayName("조회한 예약에 예약 시간이 존재한다.")
    @Test
    void should_get_reservation_times() {
        List<Reservation> reservations = reservationRepository.getAllReservations();
        assertThat(reservations.get(0).getTime().getStartAt()).isEqualTo(LocalTime.of(10, 0));
    }

    @DisplayName("예약을 추가한다")
    @Test
    void should_add_reservation() {
        ReservationTime reservationTime = new ReservationTime(1L, LocalTime.of(10, 0));
        Theme theme = new Theme(1L, "에버", "공포", "공포.jpg");
        Reservation reservation = new Reservation("네오", LocalDate.of(2024, 9, 1), reservationTime, theme);

        reservationRepository.addReservation(reservation);

        Integer count = jdbcTemplate.queryForObject("select count(1) from reservation", Integer.class);
        assertThat(count).isEqualTo(3);
    }

    @DisplayName("예약을 삭제한다")
    @Test
    void should_delete_reservation() {
        reservationRepository.deleteReservation(1);
        Integer count = jdbcTemplate.queryForObject("select count(1) from reservation", Integer.class);
        assertThat(count).isEqualTo(1);
    }

    @DisplayName("아이디가 존재하면 참을 반환한다.")
    @Test
    void should_return_true_when_id_exist() {
        long count = reservationRepository.countReservationById(1);
        assertThat(count).isEqualTo(1);
    }

    @DisplayName("아이디가 존재하면 거짓을 반환한다.")
    @Test
    void should_return_false_when_id_not_exist() {
        long count = reservationRepository.countReservationById(100000000);
        assertThat(count).isEqualTo(0);
    }

    @DisplayName("특정 날짜와 테마에 해당하는 시간을 조회한다.")
    @Test
    void should_get_reservation_times_when_date_and_theme_given() {
        LocalDate date = LocalDate.of(2023, 8, 5);
        List<ReservationTime> times = reservationRepository.findReservationTimeByDateAndTheme(date, 1);
        assertThat(times).hasSize(1);
        assertThat(times).containsExactly(new ReservationTime(1L, LocalTime.of(10, 0)));
    }
}
