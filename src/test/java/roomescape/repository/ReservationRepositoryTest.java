package roomescape.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.test.util.TestDatabaseUtils;

@JdbcTest
class ReservationRepositoryTest {

    private static final long DEFAULT_ID = 1;
    private static final long NOT_EXIST_ID = 999;
    private static final String DEFAULT_NAME = "name";
    private static final LocalDate DEFAULT_DATE = LocalDate.of(2025, 1, 1);
    private static final LocalTime DEFAULT_START_AT = LocalTime.of(1, 1);
    private static final Theme DEFAULT_THEME = Theme.create("themeName", "themeDesrciption", "themeUrl");

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private ReservationRepository reservationRepository;

    @BeforeEach
    void setUp() {
        TestDatabaseUtils.clearTables(jdbcTemplate);
        this.reservationRepository = new ReservationRepository(jdbcTemplate);
    }

    @Nested
    class 예약을_저장한다 {

        @Test
        void 새로운_예약_정보를_저장한다() {
            // given
            ReservationTime time = persistTime(DEFAULT_START_AT);
            Theme theme = persistTheme(DEFAULT_THEME);
            Reservation transientReservation = Reservation.create(
                    DEFAULT_NAME,
                    DEFAULT_DATE,
                    time,
                    theme
            );

            // when
            reservationRepository.persist(transientReservation);

            // then
            String countSql = "SELECT count(*) FROM reservation";
            Integer reservationCount = jdbcTemplate.queryForObject(countSql, Integer.class);

            assertThat(reservationCount).isEqualTo(1);
        }

        @Test
        void 저장한_예약_정보를_반환한다() {
            // given
            ReservationTime time = persistTime(DEFAULT_START_AT);
            Theme theme = persistTheme(DEFAULT_THEME);
            Reservation transientReservation = Reservation.create(
                    DEFAULT_NAME,
                    DEFAULT_DATE,
                    time,
                    theme
            );

            // when
            Reservation persistedReservation = reservationRepository.persist(transientReservation);

            // then
            String selectSql = "SELECT r.id AS reservation_id,"
                    + " r.name AS member_name,"
                    + " r.date AS reservation_date,"
                    + " r.time_id,"
                    + " r.theme_id,"
                    + " rt.start_at AS time_start_at,"
                    + " t.name AS theme_name,"
                    + " t.description AS theme_description,"
                    + " t.image_url AS theme_image_url"
                    + " FROM reservation r"
                    + " INNER JOIN reservation_time rt ON r.time_id = rt.id"
                    + " INNER JOIN theme t ON r.theme_id = t.id";
            List<Reservation> foundReservations = jdbcTemplate.query(selectSql, reservationRowMapper());

            assertThat(foundReservations).hasSize(1);
            assertThat(foundReservations.getFirst()).isEqualTo(persistedReservation);
        }
    }

    @Test
    void 저장된_모든_예약을_조회한다() {
        // given
        int reservationCount = 5;
        insertReservation(reservationCount);

        // when
        List<Reservation> foundReservations = reservationRepository.findAll();

        // then
        assertThat(foundReservations).hasSize(reservationCount);
    }

    @Nested
    class 예약_정보를_제거한다 {

        @Test
        void ID_기반으로_예약을_제거한다() {
            // given
            ReservationTime time = persistTime(DEFAULT_START_AT);
            Theme theme = persistTheme(DEFAULT_THEME);
            String insertSql = "INSERT INTO reservation(id, name, date, time_id, theme_id)"
                    + " VALUES (?, ?, ?, ?, ?)";

            jdbcTemplate.update(
                    insertSql,
                    DEFAULT_ID,
                    DEFAULT_NAME,
                    DEFAULT_DATE,
                    time.getId(),
                    theme.getId()
            );

            // when
            reservationRepository.delete(DEFAULT_ID);

            // then
            String countSql = "SELECT count(*)"
                    + " FROM reservation"
                    + " WHERE id = ?";
            Integer reservationCount = jdbcTemplate.queryForObject(
                    countSql,
                    Integer.class,
                    DEFAULT_ID
            );

            assertThat(reservationCount).isEqualTo(0);
        }

        @Test
        void 레코드가_제거됐다면_true를_반환한다() {
            // given
            ReservationTime time = persistTime(DEFAULT_START_AT);
            Theme theme = persistTheme(DEFAULT_THEME);
            String insertSql = "INSERT INTO reservation(id, name, date, time_id, theme_id)"
                    + " VALUES (?, ?, ?, ?, ?)";

            jdbcTemplate.update(
                    insertSql,
                    DEFAULT_ID,
                    DEFAULT_NAME,
                    DEFAULT_DATE,
                    time.getId(),
                    theme.getId()
            );

            // when
            boolean deleted = reservationRepository.delete(DEFAULT_ID);

            // then
            assertThat(deleted).isTrue();
        }

        @Test
        void 아무_레코드도_제거되지_않았다면_false를_반환한다() {
            boolean deleted = reservationRepository.delete(NOT_EXIST_ID);

            assertThat(deleted).isFalse();
        }
    }

    private void insertReservation(int count) {
        Long timeId = persistTime(DEFAULT_START_AT)
                .getId();
        Long themeId = persistTheme(DEFAULT_THEME)
                .getId();

        LocalDate currentDate = DEFAULT_DATE;
        for (int i = 0; i < count; i++) {
            String insertSql = "INSERT INTO reservation(name, date, time_id, theme_id)"
                    + " VALUES (?, ?, ?, ?)";

            jdbcTemplate.update(
                    insertSql,
                    DEFAULT_NAME,
                    currentDate,
                    timeId,
                    themeId
            );
            currentDate = currentDate.plusDays(1);
        }
    }

    private ReservationTime persistTime(LocalTime startAt) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("reservation_time")
                .usingGeneratedKeyColumns("id");
        Number id = simpleJdbcInsert.executeAndReturnKey(Map.of(
                "start_at", startAt
        ));

        return ReservationTime.retrieve(
                id.longValue(),
                startAt
        );
    }

    private Theme persistTheme(Theme theme) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("theme")
                .usingGeneratedKeyColumns("id");
        Number id = simpleJdbcInsert.executeAndReturnKey(Map.of(
                "name", theme.getName(),
                "description", theme.getDescription(),
                "image_url", theme.getImageUrl()
        ));

        return theme.with(id.longValue());
    }

    private RowMapper<Reservation> reservationRowMapper() {
        return (resultSet, rowNum) -> {
            long timeId = resultSet.getLong("time_id");
            LocalTime startAt = resultSet.getObject("time_start_at", LocalTime.class);

            long themeId = resultSet.getLong("theme_id");
            String themeName = resultSet.getString("theme_name");
            String description = resultSet.getString("theme_description");
            String imageUrl = resultSet.getString("theme_image_url");

            return Reservation.retrieve(
                    resultSet.getLong("reservation_id"),
                    resultSet.getString("member_name"),
                    resultSet.getObject("reservation_date", LocalDate.class),
                    ReservationTime.retrieve(timeId, startAt),
                    Theme.retrieve(themeId, themeName, description, imageUrl)
            );
        };
    }
}
