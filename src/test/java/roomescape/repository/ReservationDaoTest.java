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
import roomescape.repository.dto.ReservationSavedDto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ReservationDaoTest {

    private static final int INITIAL_RESERVATION_COUNT = 2;

    private final JdbcTemplate jdbcTemplate;
    private final ReservationDao reservationDao;
    private final SimpleJdbcInsert themeInsertActor;
    private final SimpleJdbcInsert reservationTimeInsertActor;
    private final SimpleJdbcInsert reservationInsertActor;

    @Autowired
    public ReservationDaoTest(JdbcTemplate jdbcTemplate, ReservationDao reservationDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.reservationDao = reservationDao;
        this.themeInsertActor = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("theme")
                .usingGeneratedKeyColumns("id");
        this.reservationTimeInsertActor = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("reservation_time")
                .usingGeneratedKeyColumns("id");
        this.reservationInsertActor = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("reservation")
                .usingGeneratedKeyColumns("id");
    }

    @BeforeEach
    void setUp() {
        initDatabase();
        insertTheme("n1", "d1", "t1");
        insertTheme("n2", "d2", "t2");
        insertReservationTime("1:00");
        insertReservationTime("2:00");
        insertReservation("n1", "2000-01-01", 1, 1);
        insertReservation("n2", "2000-01-02", 2, 2);
    }

    private void initDatabase() {
        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY FALSE");
        jdbcTemplate.execute("TRUNCATE TABLE theme RESTART IDENTITY");
        jdbcTemplate.execute("TRUNCATE TABLE reservation_time RESTART IDENTITY");
        jdbcTemplate.execute("TRUNCATE TABLE reservation RESTART IDENTITY");
    }

    private void insertReservationTime(String startAt) {
        Map<String, Object> parameters = new HashMap<>(1);
        parameters.put("start_at", startAt);
        reservationTimeInsertActor.execute(parameters);
    }

    private void insertReservation(String name, String date, long timeId, long themeId) {
        Map<String, Object> parameters = new HashMap<>(4);
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

    @DisplayName("예약을 저장한다.")
    @Test
    void should_save_reservation() {
        Theme theme = new Theme(1, "n1", "d1", "t1");
        ReservationTime time = new ReservationTime(1, LocalTime.of(1, 0));
        Reservation reservation = new Reservation("n3", LocalDate.of(2000, 1, 3), time, theme);

        reservationDao.save(reservation);

        assertThat(reservationDao.findAll()).hasSize(INITIAL_RESERVATION_COUNT + 1);
    }

    @DisplayName("모든 예약을 조회한다.")
    @Test
    void should_find_all_reservations() {
        List<ReservationSavedDto> reservations = reservationDao.findAll();
        assertThat(reservations).hasSize(INITIAL_RESERVATION_COUNT);
    }

    @DisplayName("특정 id의 예약을 조회한다.")
    @Test
    void should_find_reservation_by_id() { // TODO: test empty optional
        ReservationSavedDto expected = new ReservationSavedDto(1, "n1", LocalDate.of(2000, 1, 1), 1L, 1L);

        Optional<ReservationSavedDto> actual = reservationDao.findById(1);

        assertThat(actual).isNotEmpty();
        assertThat(actual).hasValue(expected);
    }

    @DisplayName("특정 date 와 theme_id의 예약을 조회한다.")
    @Test
    void should_find_reservation_by_date_and_themeId() {
        ReservationSavedDto expected = new ReservationSavedDto(1, "n1", LocalDate.of(2000, 1, 1), 1L, 1L);

        List<ReservationSavedDto> actual = reservationDao.findByDateAndThemeId(LocalDate.of(2000, 1, 1), 1);

        assertThat(actual).containsExactly(expected);
    }

    @DisplayName("두 date 사이의 예약을, 테마의 개수로 내림차순 정렬하여, 특정 개수의 테마 id를 조회한다.")
    @Test
    void should_find_themeId_between_date_and_order_by_themeId_count_and_limit() {
        List<Long> themeIds = reservationDao.findThemeIdByDateAndOrderByThemeIdCountAndLimit(
                LocalDate.of(2000, 1, 1), LocalDate.of(2000, 1, 2), 10);
        assertThat(themeIds).hasSizeLessThanOrEqualTo(10);
        assertThat(themeIds).containsExactly(1L, 2L);
    }

    @DisplayName("예약을 삭제한다.")
    @Test
    void should_delete_reservation() {
        reservationDao.deleteById(1);
        assertThat(reservationDao.findAll()).hasSize(INITIAL_RESERVATION_COUNT - 1);
    }

    @DisplayName("특정 id를 가진 예약이 존재하면 참을 반환한다.")
    @Test
    void should_return_true_when_exist_id() {
        boolean isExist = reservationDao.isExistById(1);
        assertThat(isExist).isTrue();
    }

    @DisplayName("특정 id를 가진 예약이 존재하지 않으면 거짓을 반환한다.")
    @Test
    void should_return_false_when_not_exist_id() {
        boolean isExist = reservationDao.isExistById(999);
        assertThat(isExist).isFalse();
    }

    @DisplayName("특정 time_id를 가진 예약이 존재하면 참을 반환한다.")
    @Test
    void should_return_true_when_exist_timeId() {
        boolean isExist = reservationDao.isExistByTimeId(1);
        assertThat(isExist).isTrue();
    }

    @DisplayName("특정 time_id를 가진 예약이 존재하지 않으면 거짓을 반환한다.")
    @Test
    void should_return_false_when_not_exist_timeId() {
        boolean isExist = reservationDao.isExistByTimeId(999);
        assertThat(isExist).isFalse();
    }

    @DisplayName("특정 date와 time_id를 가진 예약이 존재하면 참을 반환한다.")
    @Test
    void should_return_true_when_exist_date_and_timeId() {
        boolean isExist = reservationDao.isExistByDateAndTimeId(LocalDate.of(2000, 1, 1), 1);
        assertThat(isExist).isTrue();
    }

    @DisplayName("특정 date와 time_id를 가진 예약이 존재하지 않으면 거짓을 반환한다.")
    @Test
    void should_return_false_when_not_exist_date_and_timeId() {
        boolean isExist = reservationDao.isExistByDateAndTimeId(LocalDate.of(2000, 1, 1), 999);
        assertThat(isExist).isFalse();
    }
}
