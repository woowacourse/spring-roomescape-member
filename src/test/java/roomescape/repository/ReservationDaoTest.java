package roomescape.repository;

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
import roomescape.repository.dao.ReservationDao;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ReservationDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ReservationDao reservationDao;

    private SimpleJdbcInsert reservationTimeInsertActor;
    private SimpleJdbcInsert reservationInsertActor;
    private SimpleJdbcInsert themeInsertActor;

    @BeforeEach
    void setUp() {
        jdbcTemplate.execute("TRUNCATE TABLE reservation RESTART IDENTITY");
        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY FALSE");
        jdbcTemplate.execute("TRUNCATE TABLE reservation_time RESTART IDENTITY");
        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY TRUE");

        reservationTimeInsertActor = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("reservation_time")
                .usingGeneratedKeyColumns("id");
        reservationInsertActor = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("reservation")
                .usingGeneratedKeyColumns("id");
        themeInsertActor = new SimpleJdbcInsert(jdbcTemplate)
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
        List<Reservation> reservations = reservationDao.findAllReservations();
        assertThat(reservations).hasSize(2);
    }

    @DisplayName("조회한 예약에 예약 시간이 존재한다.")
    @Test
    void should_get_reservation_times() {
        List<Reservation> reservations = reservationDao.findAllReservations();
        assertThat(reservations.get(0).getTime().getStartAt()).isEqualTo(LocalTime.of(10, 0));
    }

    @DisplayName("예약을 추가한다")
    @Test
    void should_add_reservation() {
        ReservationTime reservationTime = new ReservationTime(1, LocalTime.of(10, 0));
        Theme theme = new Theme(1, "에버", "공포", "공포.jpg");
        Reservation reservation = new Reservation("네오", LocalDate.of(2024, 9, 1), reservationTime, theme);

        reservationDao.saveReservation(reservation);

        Integer count = jdbcTemplate.queryForObject("select count(1) from reservation", Integer.class);
        assertThat(count).isEqualTo(3);
    }

    @DisplayName("예약을 삭제한다")
    @Test
    void should_delete_reservation() {
        reservationDao.deleteReservationById(1);
        Integer count = jdbcTemplate.queryForObject("select count(1) from reservation", Integer.class);
        assertThat(count).isEqualTo(1);
    }

    @DisplayName("특정 id를 가진 데이터가 존재하면 참을 반환한다.")
    @Test
    void should_return_true_when_exist() {
        boolean isExist = reservationDao.isExistReservationById(1);
        assertThat(isExist).isTrue();
    }

    @DisplayName("특정 id를 가진 데이터가 존재하지 않으면 거짓을 반환한다.")
    @Test
    void should_return_false_when_not_exist() {
        boolean isExist = reservationDao.isExistReservationById(100000000);
        assertThat(isExist).isFalse();
    }

    @DisplayName("특정 날짜와 테마에 해당하는 시간을 조회한다.")
    @Test
    void should_get_reservation_times_when_date_and_theme_given() {
        LocalDate date = LocalDate.of(2023, 8, 5);
        List<ReservationTime> times = reservationDao.findReservationTimeBooked(date, 1);
        assertThat(times).hasSize(1);
        assertThat(times).containsExactly(new ReservationTime(1, LocalTime.of(10, 0)));
    }
}
