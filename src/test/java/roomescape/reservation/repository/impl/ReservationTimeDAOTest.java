package roomescape.reservation.repository.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalTime;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import roomescape.common.exception.AlreadyInUseException;
import roomescape.common.exception.EntityNotFoundException;
import roomescape.config.TestConfig;
import roomescape.domain.reservation.entity.ReservationTime;
import roomescape.domain.reservation.repository.ReservationTimeRepository;
import roomescape.domain.reservation.repository.impl.ReservationTimeDAO;
import roomescape.utils.JdbcTemplateUtils;

class ReservationTimeDAOTest {

    private JdbcTemplate jdbcTemplate;
    private ReservationTimeRepository reservationTimeRepository;

    @BeforeEach
    void init() {
        jdbcTemplate = TestConfig.getJdbcTemplate();
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
        reservationTimeRepository = new ReservationTimeDAO(namedParameterJdbcTemplate, TestConfig.getDataSource());
    }

    @DisplayName("id에 따라 예약 시간을 반환한다.")
    @Test
    void test1() {
        // given
        Long id = 1L;
        LocalTime now = LocalTime.now();
        saveReservationTime(id, now);

        // when
        ReservationTime result = reservationTimeRepository.findById(id).get();

        // then
        assertThat(result.getId()).isEqualTo(id);
        assertThat(result.getStartAt()).isEqualTo(now);
    }

    @DisplayName("해당 id가 없다면 예외를 반환한다.")
    @Test
    void test2() {
        assertThatThrownBy(() -> reservationTimeRepository.deleteById(1L))
                .isInstanceOf(EntityNotFoundException.class);

    }

    @DisplayName("해당 ID가 DB에 없다면 예외를 반환한다.")
    @Test
    void test7() {
        assertThatThrownBy(() -> reservationTimeRepository.deleteById(1L))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @AfterEach
    void cleanUp() {
        JdbcTemplateUtils.deleteAllTables(jdbcTemplate);
    }

    private void saveReservationTime(Long id, LocalTime startAt) {
        String sql = "insert into reservation_time (id, start_at) values (?, ?)";
        jdbcTemplate.update(sql, id, startAt);
    }

    @DisplayName("예약 시간을 삭제한다.")
    @Nested
    class delete {

        @DisplayName("해당 ID를 삭제한다.")
        @Test
        void test1() {
            // given
            Long id = 1L;
            LocalTime now = LocalTime.of(9, 0);
            String sql = "insert into reservation_time(id, start_at) values(?, ?)";
            jdbcTemplate.update(sql, id, now);

            // when
            assertThatCode(() -> reservationTimeRepository.deleteById(id))
                    .doesNotThrowAnyException();
        }

        @DisplayName("Reservation 테이블에서 사용 중이라면 AlreadyUseException 예외를 반환한다.")
        @Test
        void test2() {
            // given
            Long id = 1L;
            LocalDate now = LocalDate.of(2024, 12, 8);
            String sql = "insert into reservation_time(id, start_at) values(?, ?)";
            jdbcTemplate.update(sql, id, now);

            jdbcTemplate.update("INSERT INTO reservation(name, date, time_id) VALUES (?, ?, ?)", "꾹", now,
                    id);

            // when
            assertThatThrownBy(() -> reservationTimeRepository.deleteById(id))
                    .isInstanceOf(AlreadyInUseException.class);
        }
    }

    @DisplayName("예약 시간을 저장한다.")
    @Nested
    class save {

        @DisplayName("성공 테스트")
        @Test
        void test1() {
            // given
            LocalTime now = LocalTime.now();
            ReservationTime time = ReservationTime.withoutId(now);

            // when
            ReservationTime saved = reservationTimeRepository.save(time);

            // then
            assertThat(saved.getId()).isNotNull();
            assertThat(saved.getStartAt()).isEqualTo(now);
        }

        @DisplayName("ID가 있다면 해당 ID로 데이터를 업데이트한다.")
        @Test
        void test5() {
            // given
            Long id = 1L;
            LocalTime now = LocalTime.of(9, 0);
            String sql = "insert into reservation_time(id, start_at) values(?, ?)";
            jdbcTemplate.update(sql, id, now);

            // when
            LocalTime changeTime = LocalTime.of(10, 0);
            ReservationTime reservationTime = new ReservationTime(id, changeTime);
            ReservationTime result = reservationTimeRepository.save(reservationTime);

            // then

            SoftAssertions softly = new SoftAssertions();

            softly.assertThat(result.getId()).isEqualTo(id);
            softly.assertThat(result.getStartAt()).isEqualTo(changeTime);

            softly.assertAll();
        }

        @DisplayName("DB에 해당 ID가 없고, 객체에 ID가 존재하는데 저장 시 예외를 반환한다.")
        @Test
        void test6() {
            // given
            Long id = 1L;
            LocalTime now = LocalTime.of(9, 0);
            ReservationTime reservationTime = new ReservationTime(id, now);

            // when & then
            assertThatThrownBy(() -> reservationTimeRepository.save(reservationTime))
                    .isInstanceOf(EntityNotFoundException.class);
        }
    }
}
