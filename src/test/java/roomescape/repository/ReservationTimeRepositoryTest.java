package roomescape.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
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
import roomescape.domain.ReservationTime;
import roomescape.test.util.TestDatabaseUtils;

@JdbcTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ReservationTimeRepositoryTest {

    private static boolean persistTestSuccessful = false;
    private static boolean findAllTestSuccessful = false;

    private static final UUID NOT_EXIST_ID = UUID.fromString("ffffffff-ffff-ffff-ffff-ffffffffffff");

    private static final LocalTime DEFAULT_START_AT = LocalTime.of(1, 1);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private ReservationTimeRepository timeRepository;

    @BeforeEach
    void setUp() {
        TestDatabaseUtils.clearTables(jdbcTemplate);
        this.timeRepository = new ReservationTimeRepository(jdbcTemplate);
    }

    @Order(1)
    @Test
    void 새로운_시간_정보를_저장하고_저장된_정보를_반환한다() {
        // given
        UUID id = UUID.fromString("11111111-1111-1111-1111-111111111101");
        ReservationTime transientTime = new ReservationTime(id, DEFAULT_START_AT);

        // when
        ReservationTime persistedTime = timeRepository.persist(transientTime);

        // then
        String selectSql = "SELECT id, start_at"
                + " FROM reservation_time";
        List<ReservationTime> foundTimes = jdbcTemplate.query(selectSql, reservationTimeRowMapper());

        assertThat(foundTimes).hasSize(1);
        assertThat(foundTimes.getFirst()).isEqualTo(persistedTime);

        persistTestSuccessful = true;
    }

    @Order(2)
    @Test
    void 저장된_모든_예약_시간을_조회한다() {
        skipIfPersistTestFailed();

        // given
        UUID id = UUID.fromString("11111111-1111-1111-1111-111111111102");
        ReservationTime transientTime = new ReservationTime(id, DEFAULT_START_AT);
        ReservationTime persistedTime = timeRepository.persist(transientTime);

        // when
        List<ReservationTime> reservationTimes = timeRepository.findAll();

        // then
        assertThat(reservationTimes).containsExactly(persistedTime);

        findAllTestSuccessful = true;
    }

    @Nested
    class 저장_조회_의존_테스트 {

        @BeforeEach
        void skipIfDependentTestFailed() {
            skipIfPersistTestFailed();
            skipIfFindAllTestFailed();
        }

        @Nested
        class 예약_시간을_ID_기준으로_조회한다 {

            @Test
            void 예약_시간을_ID_기준으로_조회한다() {
                // given
                UUID id = UUID.fromString("11111111-1111-1111-1111-111111111103");
                ReservationTime transientTime = new ReservationTime(id, DEFAULT_START_AT);
                ReservationTime persistedTime = timeRepository.persist(transientTime);

                // when
                Optional<ReservationTime> foundTime = timeRepository.findById(persistedTime.getId());

                // then
                assertThat(foundTime).hasValue(persistedTime);
            }

            @Test
            void ID로_레코드가_조회되지_않는다면_빈_Optional을_반환한다() {
                // when
                Optional<ReservationTime> reservationTime = timeRepository.findById(NOT_EXIST_ID);

                // then
                assertThat(reservationTime).isEmpty();
            }
        }

        @Nested
        class 예약_시간_정보를_제거한다 {

            @Test
            void ID_기반으로_예약_시간을_제거한다() {
                // given
                UUID id = UUID.fromString("11111111-1111-1111-1111-111111111104");
                ReservationTime transientTime = new ReservationTime(id, DEFAULT_START_AT);
                ReservationTime persistedTime = timeRepository.persist(transientTime);

                // when
                timeRepository.delete(persistedTime.getId());

                // then
                List<ReservationTime> foundTimes = timeRepository.findAll();

                assertThat(foundTimes).doesNotContain(persistedTime);
            }

            @Test
            void 레코드가_제거됐다면_true를_반환한다() {
                // given
                UUID id = UUID.fromString("11111111-1111-1111-1111-111111111105");
                ReservationTime transientTime = new ReservationTime(id, DEFAULT_START_AT);
                ReservationTime persistedTime = timeRepository.persist(transientTime);

                // when
                boolean deleted = timeRepository.delete(persistedTime.getId());

                // then
                assertThat(deleted).isTrue();
            }

            @Test
            void 아무_레코드도_제거되지_않았다면_false를_반환한다() {
                boolean deleted = timeRepository.delete(NOT_EXIST_ID);

                assertThat(deleted).isFalse();
            }
        }
    }

    private RowMapper<ReservationTime> reservationTimeRowMapper() {
        return (resultSet, rowNum) -> {
            UUID id = UUID.fromString(resultSet.getString("id"));
            LocalTime startAt = resultSet.getObject("start_at", LocalTime.class);

            return new ReservationTime(id, startAt);
        };
    }

    private static void skipIfPersistTestFailed() {
        Assumptions.assumeTrue(persistTestSuccessful, "저장 기능 테스트에 실패하여 다른 테스트를 수행하지 않습니다.");
    }

    private static void skipIfFindAllTestFailed() {
        Assumptions.assumeTrue(findAllTestSuccessful, "조회 기능 테스트에 실패하여 다른 테스트를 수행하지 않습니다.");
    }
}
