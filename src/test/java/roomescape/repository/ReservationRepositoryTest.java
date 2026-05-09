package roomescape.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
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
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ReservationRepositoryTest {

    private static boolean persistTestSuccessful = false;
    private static boolean findAllTestSuccessful = false;

    private static final UUID NOT_EXIST_ID = UUID.fromString("ffffffff-ffff-ffff-ffff-ffffffffffff");

    private static final UUID SAMPLE_TIME_ID = UUID.fromString("11111111-1111-1111-1111-111111111101");
    private static final UUID SAMPLE_THEME_ID = UUID.fromString("22222222-2222-2222-2222-222222222201");

    private static final String DEFAULT_NAME = "name";
    private static final LocalDate DEFAULT_DATE = LocalDate.of(2025, 1, 1);
    private static final LocalTime DEFAULT_START_AT = LocalTime.of(1, 1);
    private static final Theme DEFAULT_THEME = new Theme(
            SAMPLE_THEME_ID,
            "themeName",
            "themeDescription",
            "themeUrl"
    );

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private ReservationRepository reservationRepository;

    @BeforeEach
    void setUp() {
        TestDatabaseUtils.clearTables(jdbcTemplate);
        this.reservationRepository = new ReservationRepository(jdbcTemplate);
    }

    @Order(1)
    @Test
    void 새로운_예약_정보를_저장하고_저장한_정보를_반환한다() {
        // given
        ReservationTime time = persistTime(SAMPLE_TIME_ID, DEFAULT_START_AT);
        Theme theme = persistTheme(DEFAULT_THEME);
        UUID reservationId = UUID.fromString("33333333-3333-3333-3333-333333333301");

        Reservation transientReservation = new Reservation(
                reservationId,
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

        persistTestSuccessful = true;
    }

    @Order(2)
    @Test
    void 저장된_모든_예약을_조회한다() {
        skipIfPersistTestFailed();

        // given
        ReservationTime time = persistTime(SAMPLE_TIME_ID, DEFAULT_START_AT);
        Theme theme = persistTheme(DEFAULT_THEME);
        UUID reservationId = UUID.fromString("33333333-3333-3333-3333-333333333302");

        Reservation reservation = new Reservation(
                reservationId,
                DEFAULT_NAME,
                DEFAULT_DATE,
                time,
                theme
        );

        Reservation persistedReservation = reservationRepository.persist(reservation);

        // when
        List<Reservation> foundReservations = reservationRepository.findAll();

        // then
        assertThat(foundReservations).containsExactly(persistedReservation);

        findAllTestSuccessful = true;
    }

    @Nested
    class 예약_정보를_제거한다 {

        @BeforeEach
        void skipIfDependentTestFailed() {
            skipIfPersistTestFailed();
            skipIfFindAllTestFailed();
        }

        @Test
        void ID_기반으로_예약을_제거한다() {
            // given
            ReservationTime time = persistTime(SAMPLE_TIME_ID, DEFAULT_START_AT);
            Theme theme = persistTheme(DEFAULT_THEME);
            UUID reservationId = UUID.fromString("33333333-3333-3333-3333-333333333303");

            Reservation reservation = new Reservation(
                    reservationId,
                    DEFAULT_NAME,
                    DEFAULT_DATE,
                    time,
                    theme
            );

            Reservation persistedReservation = reservationRepository.persist(reservation);

            // when
            reservationRepository.delete(persistedReservation.getId());

            // then
            List<Reservation> reservations = reservationRepository.findAll();

            assertThat(reservations).hasSize(0);
        }

        @Test
        void 레코드가_제거됐다면_true를_반환한다() {
            // given
            ReservationTime time = persistTime(SAMPLE_TIME_ID, DEFAULT_START_AT);
            Theme theme = persistTheme(DEFAULT_THEME);
            UUID reservationId = UUID.fromString("33333333-3333-3333-3333-333333333304");

            Reservation reservation = new Reservation(
                    reservationId,
                    DEFAULT_NAME,
                    DEFAULT_DATE,
                    time,
                    theme
            );

            Reservation persistedReservation = reservationRepository.persist(reservation);

            // when
            boolean deleted = reservationRepository.delete(persistedReservation.getId());

            // then
            assertThat(deleted).isTrue();
        }

        @Test
        void 아무_레코드도_제거되지_않았다면_false를_반환한다() {
            boolean deleted = reservationRepository.delete(NOT_EXIST_ID);

            assertThat(deleted).isFalse();
        }
    }

    private ReservationTime persistTime(UUID id, LocalTime startAt) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("reservation_time");
        simpleJdbcInsert.execute(Map.of(
                "id", id.toString(),
                "start_at", startAt
        ));

        return new ReservationTime(id, startAt);
    }

    private Theme persistTheme(Theme theme) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("theme");
        simpleJdbcInsert.execute(Map.of(
                "id", theme.getId().toString(),
                "name", theme.getName(),
                "description", theme.getDescription(),
                "image_url", theme.getImageUrl()
        ));

        return theme;
    }

    private RowMapper<Reservation> reservationRowMapper() {
        return (resultSet, rowNum) -> {
            UUID timeId = UUID.fromString(resultSet.getString("time_id"));
            LocalTime startAt = resultSet.getObject("time_start_at", LocalTime.class);

            UUID themeId = UUID.fromString(resultSet.getString("theme_id"));
            String themeName = resultSet.getString("theme_name");
            String description = resultSet.getString("theme_description");
            String imageUrl = resultSet.getString("theme_image_url");

            return new Reservation(
                    UUID.fromString(resultSet.getString("reservation_id")),
                    resultSet.getString("member_name"),
                    resultSet.getObject("reservation_date", LocalDate.class),
                    new ReservationTime(timeId, startAt),
                    new Theme(themeId, themeName, description, imageUrl)
            );
        };
    }

    private static void skipIfPersistTestFailed() {
        Assumptions.assumeTrue(persistTestSuccessful, "저장 기능 테스트에 실패하여 다른 테스트를 수행하지 않습니다.");
    }

    private static void skipIfFindAllTestFailed() {
        Assumptions.assumeTrue(findAllTestSuccessful, "조회 기능 테스트에 실패하여 다른 테스트를 수행하지 않습니다.");
    }
}
