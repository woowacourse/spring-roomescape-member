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
import roomescape.domain.Duration;
import roomescape.domain.Reservation;
import roomescape.test.util.TestDatabaseUtils;

@JdbcTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ReservationRepositoryTest {

    private static boolean persistTestSuccessful = false;
    private static boolean findAllTestSuccessful = false;

    private static final UUID DEFAULT_RESERVATION_ID = UUID.fromString("33333333-3333-3333-3333-333333333301");
    private static final UUID NOT_EXIST_ID = UUID.fromString("ffffffff-ffff-ffff-ffff-ffffffffffff");
    private static final String DEFAULT_RESERVATION_NAME = "name";
    private static final LocalDate DEFAULT_RESERVATION_DATE = LocalDate.of(2025, 1, 1);

    private static final UUID DEFAULT_TIME_ID = UUID.fromString("11111111-1111-1111-1111-111111111101");
    private static final LocalTime DEFAULT_TIME_START_AT = LocalTime.of(1, 1);

    private static final UUID DEFAULT_THEME_ID = UUID.fromString("22222222-2222-2222-2222-222222222201");
    private static final String DEFAULT_THEME_NAME = "themeName";
    private static final String DEFAULT_THEME_DESCRIPTION = "themeDescription";
    private static final String DEFAULT_THEME_URL = "themeUrl";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private ReservationRepository reservationRepository;

    @BeforeEach
    void setUp() {
        TestDatabaseUtils.clearTables(jdbcTemplate);
        this.reservationRepository = new ReservationRepository(jdbcTemplate);

        persistTime(
                DEFAULT_TIME_ID,
                DEFAULT_TIME_START_AT
        );
        persistTheme(
                DEFAULT_THEME_ID,
                DEFAULT_THEME_NAME,
                DEFAULT_THEME_DESCRIPTION,
                DEFAULT_THEME_URL
        );
    }

    @Order(1)
    @Test
    void 새로운_예약_정보를_저장하고_저장한_정보를_반환한다() {
        // given
        Reservation transientReservation = new Reservation(
                DEFAULT_RESERVATION_ID,
                DEFAULT_RESERVATION_NAME,
                DEFAULT_RESERVATION_DATE,
                DEFAULT_TIME_ID,
                DEFAULT_THEME_ID
        );

        // when
        Reservation persistedReservation = reservationRepository.persist(transientReservation);

        // then
        String selectSql = "SELECT id, name, date, time_id, theme_id"
                + " FROM reservation r";
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
        Reservation reservation = new Reservation(
                DEFAULT_RESERVATION_ID,
                DEFAULT_RESERVATION_NAME,
                DEFAULT_RESERVATION_DATE,
                DEFAULT_TIME_ID,
                DEFAULT_THEME_ID
        );

        Reservation persistedReservation = reservationRepository.persist(reservation);

        // when
        List<Reservation> foundReservations = reservationRepository.findAll();

        // then
        assertThat(foundReservations).containsExactly(persistedReservation);

        findAllTestSuccessful = true;
    }

    @Nested
    class 저장_조회_의존_테스트 {

        @BeforeEach
        void skipIfDependentTestFailed() {
            skipIfPersistTestFailed();
            skipIfFindAllTestFailed();
        }

        @Test
        void 기간_내_예약_정보를_조회한다() {
            // given
            LocalDate startDate = LocalDate.of(2000, 1, 1);
            LocalDate endDate = LocalDate.of(3000, 1, 1);

            Reservation outOfDurationReservation = new Reservation(
                    UUID.randomUUID(),
                    "outReservationName",
                    LocalDate.of(1000, 1, 1),
                    DEFAULT_TIME_ID,
                    DEFAULT_THEME_ID
            );
            reservationRepository.persist(outOfDurationReservation);

            Reservation withinDurationReservation = new Reservation(
                    UUID.randomUUID(),
                    "withinReservationName",
                    LocalDate.of(2500, 1, 1),
                    DEFAULT_TIME_ID,
                    DEFAULT_THEME_ID
            );
            Reservation expectedReservation = reservationRepository.persist(withinDurationReservation);

            // when
            List<Reservation> foundReservations = reservationRepository.findBetweenDuration(
                    new Duration(startDate, endDate)
            );

            // then
            assertThat(foundReservations).containsExactly(expectedReservation);
        }

        @Nested
        class 날짜와_테마_식별자를_기반으로_예약_정보를_조회한다 {

            @Test
            void 일치하는_예약을_반환한다() {
                // given
                LocalDate targetDate = LocalDate.of(2000, 1, 1);

                Reservation sameDateReservation = new Reservation(
                        DEFAULT_RESERVATION_ID,
                        DEFAULT_RESERVATION_NAME,
                        targetDate,
                        DEFAULT_TIME_ID,
                        DEFAULT_THEME_ID
                );
                Reservation persistedReservation = reservationRepository.persist(sameDateReservation);

                // when
                List<Reservation> foundReservations = reservationRepository.findByDateAndThemeId(
                        targetDate,
                        DEFAULT_THEME_ID
                );

                // then
                assertThat(foundReservations).containsExactly(persistedReservation);
            }

            @Test
            void 일치하는_예약이_없다면_빈_리스트를_반환한다() {
                // given
                LocalDate targetDate = LocalDate.of(2000, 1, 1);

                Reservation differentDateReservation = new Reservation(
                        DEFAULT_RESERVATION_ID,
                        DEFAULT_RESERVATION_NAME,
                        LocalDate.of(1000, 1, 1),
                        DEFAULT_TIME_ID,
                        DEFAULT_THEME_ID
                );
                reservationRepository.persist(differentDateReservation);

                // when
                List<Reservation> foundReservations = reservationRepository.findByDateAndThemeId(
                        targetDate,
                        DEFAULT_THEME_ID
                );

                // then
                assertThat(foundReservations).isEmpty();
            }
        }

        @Nested
        class 예약_정보를_제거한다 {

            @Test
            void ID_기반으로_예약을_제거한다() {
                // given
                Reservation reservation = new Reservation(
                        DEFAULT_RESERVATION_ID,
                        DEFAULT_RESERVATION_NAME,
                        DEFAULT_RESERVATION_DATE,
                        DEFAULT_TIME_ID,
                        DEFAULT_THEME_ID
                );

                Reservation persistedReservation = reservationRepository.persist(reservation);

                // when
                reservationRepository.delete(persistedReservation.id());

                // then
                List<Reservation> reservations = reservationRepository.findAll();

                assertThat(reservations).hasSize(0);
            }

            @Test
            void 레코드가_제거됐다면_true를_반환한다() {
                // given
                Reservation reservation = new Reservation(
                        DEFAULT_RESERVATION_ID,
                        DEFAULT_RESERVATION_NAME,
                        DEFAULT_RESERVATION_DATE,
                        DEFAULT_TIME_ID,
                        DEFAULT_THEME_ID
                );

                Reservation persistedReservation = reservationRepository.persist(reservation);

                // when
                boolean deleted = reservationRepository.delete(persistedReservation.id());

                // then
                assertThat(deleted).isTrue();
            }

            @Test
            void 아무_레코드도_제거되지_않았다면_false를_반환한다() {
                boolean deleted = reservationRepository.delete(NOT_EXIST_ID);

                assertThat(deleted).isFalse();
            }
        }
    }

    private void persistTime(UUID id, LocalTime startAt) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("reservation_time");

        simpleJdbcInsert.execute(Map.of(
                "id", id.toString(),
                "start_at", startAt
        ));
    }

    private void persistTheme(
            UUID id,
            String name,
            String description,
            String imageUrl
    ) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("theme");

        simpleJdbcInsert.execute(Map.of(
                "id", id.toString(),
                "name", name,
                "description", description,
                "image_url", imageUrl
        ));
    }

    private RowMapper<Reservation> reservationRowMapper() {
        return (resultSet, rowNum) -> new Reservation(
                UUID.fromString(resultSet.getString("id")),
                resultSet.getString("name"),
                resultSet.getObject("date", LocalDate.class),
                UUID.fromString(resultSet.getString("time_id")),
                UUID.fromString(resultSet.getString("theme_id"))
        );
    }

    private static void skipIfPersistTestFailed() {
        Assumptions.assumeTrue(persistTestSuccessful, "저장 기능 테스트에 실패하여 다른 테스트를 수행하지 않습니다.");
    }

    private static void skipIfFindAllTestFailed() {
        Assumptions.assumeTrue(findAllTestSuccessful, "조회 기능 테스트에 실패하여 다른 테스트를 수행하지 않습니다.");
    }
}
