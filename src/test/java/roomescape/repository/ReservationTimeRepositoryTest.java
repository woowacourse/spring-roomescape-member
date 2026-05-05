package roomescape.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.transaction.annotation.Transactional;
import roomescape.domain.ReservationTime;
import roomescape.exception.InUseTimeException;

@JdbcTest
class ReservationTimeRepositoryTest {

    private static final long DEFAULT_ID = 1;
    private static final long NOT_EXIST_ID = 999;
    private static final LocalTime DEFAULT_START_AT = LocalTime.of(1, 1);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private ReservationTimeRepository timeRepository;

    @BeforeEach
    void setUp() {
        this.timeRepository = new ReservationTimeRepository(jdbcTemplate);
    }

    @Nested
    class 예약_시간을_저장한다 {
        @Test
        void 새로운_시간_정보를_저장한다() {
            // given
            ReservationTime time = ReservationTime.create(DEFAULT_START_AT);

            // when
            timeRepository.persist(time);

            // then
            String timeCountSql = "SELECT count(*)"
                    + " FROM reservation_time";
            Integer timeCount = jdbcTemplate.queryForObject(
                    timeCountSql,
                    Integer.class
            );

            assertThat(timeCount).isEqualTo(1);
        }
        
        @Test
        void 저장한_시간_정보를_반환한다() {
            // given
            ReservationTime transientTime = ReservationTime.create(DEFAULT_START_AT);

            // when
            ReservationTime persistedReservationTime = timeRepository.persist(transientTime);

            // then
            String selectSql = "SELECT id, start_at"
                    + " FROM reservation_time";
            List<ReservationTime> foundReservations = jdbcTemplate.query(selectSql, reservationTimeRowMapper());

            assertThat(foundReservations).hasSize(1);
            assertThat(foundReservations.getFirst()).isEqualTo(persistedReservationTime);
        }
    }

    @Test
    void 저장된_모든_예약_시간을_조회한다() {
        // given
        int insertCount = 5;
        insertReservationTimeAsAmount(insertCount);

        // when
        List<ReservationTime> reservationTimes = timeRepository.findAll();

        // then
        assertThat(reservationTimes).hasSize(insertCount);
    }

    @Nested
    class 예약_시간을_ID_기준으로_조회한다 {

        @Test
        void 예약_시간을_ID_기준으로_조회한다() {
            // given
            insertReservationTime(DEFAULT_ID, DEFAULT_START_AT);
            ReservationTime expected = ReservationTime.retrieve(DEFAULT_ID, DEFAULT_START_AT);

            // when
            Optional<ReservationTime> actual = timeRepository.findById(DEFAULT_ID);

            // then
            assertThat(actual).hasValue(expected);
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
            insertReservationTime(DEFAULT_ID, DEFAULT_START_AT);

            // when
            timeRepository.delete(DEFAULT_ID);

            // then
            String countSql = "SELECT count(*)"
                    + " FROM reservation_time"
                    + " WHERE id = ?";
            Integer timeCount = jdbcTemplate.queryForObject(
                    countSql,
                    Integer.class,
                    DEFAULT_ID
            );

            assertThat(timeCount).isEqualTo(0);
        }

        @Test
        void 레코드가_제거됐다면_true를_반환한다() {
            // given
            insertReservationTime(DEFAULT_ID, DEFAULT_START_AT);

            // when
            boolean deleted = timeRepository.delete(DEFAULT_ID);

            // then
            assertThat(deleted).isTrue();
        }

        @Test
        void 아무_레코드도_제거되지_않았다면_false를_반환한다() {
            boolean deleted = timeRepository.delete(NOT_EXIST_ID);

            assertThat(deleted).isFalse();
        }
    }

    private void insertReservationTime(
            long id,
            LocalTime startAt
    ) {
        String insertSql = "INSERT INTO reservation_time(id, start_at)"
                + " VALUES (?, ?)";
        jdbcTemplate.update(
                insertSql,
                id,
                startAt
        );
    }

    private void insertReservationTimeAsAmount(int count) {
        for (int i = 0; i < count; i++) {
            String insertSql = "INSERT INTO reservation_time(start_at)"
                    + " VALUES (?)";
            jdbcTemplate.update(insertSql, DEFAULT_START_AT);
        }
    }

    private RowMapper<ReservationTime> reservationTimeRowMapper() {
        return (resultSet, rowNum) -> {
            long id = resultSet.getLong("id");
            LocalTime startAt = resultSet.getObject("start_at", LocalTime.class);

            return ReservationTime.retrieve(id, startAt);
        };
    }
}
