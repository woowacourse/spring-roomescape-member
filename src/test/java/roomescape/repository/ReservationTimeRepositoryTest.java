package roomescape.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import roomescape.domain.ReservationTime;
import roomescape.domain.ReservationTimeRepository;

@SpringBootTest(webEnvironment = WebEnvironment.NONE)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
class ReservationTimeRepositoryTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ReservationTimeRepository reservationTimeRepository;

    @BeforeEach
    void init() {
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES (?), (?)", "08:00", "07:00");
    }

    @DisplayName("예약 시간 저장")
    @Test
    void save() {
        final ReservationTime reservationTime = new ReservationTime( LocalTime.parse("10:00"));
        final ReservationTime savedReservationTime = reservationTimeRepository.save(reservationTime);
        assertAll(
                () -> assertThat(savedReservationTime.getId()).isEqualTo(3L),
                () -> assertThat(savedReservationTime.getStartAt()).isEqualTo(LocalTime.parse("10:00"))
        );
    }

    @DisplayName("존재하는 예약 시간 조회")
    @Test
    void findExistById() {
        final ReservationTime reservationTime = reservationTimeRepository.findById(1L).orElseThrow();
        assertAll(
                () -> assertThat(reservationTime.getId()).isEqualTo(1L),
                () -> assertThat(reservationTime.getStartAt()).isEqualTo(LocalTime.parse("08:00"))
        );
    }

    @DisplayName("존재하지 않는 예약 시간 조회")
    @Test
    void findEmptyById() {
        final Optional<ReservationTime> reservationTime = reservationTimeRepository.findById(4L);
        assertTrue(reservationTime.isEmpty());
    }

    @DisplayName("예약 시간 목록 조회")
    @Test
    void findAll() {
        final List<ReservationTime> reservationTimes = reservationTimeRepository.findAll();
        assertThat(reservationTimes.size()).isEqualTo(2);
    }

    @DisplayName("존재하는 예약 시간 삭제")
    @Test
    void deleteExistById() {
        assertThatCode(() -> reservationTimeRepository.deleteById(1L))
                .doesNotThrowAnyException();
    }

    @DisplayName("특정 시간 존재 여부 확인")
    @Test
    void existByStartAtTrue() {
        final boolean existByStartAt = reservationTimeRepository.existByStartAt(LocalTime.parse("08:00"));

        assertThat(existByStartAt).isTrue();
    }

    @DisplayName("특정 시간 존재 여부 확인")
    @Test
    void existByStartAtFalse() {
        final boolean existByStartAt = reservationTimeRepository.existByStartAt(LocalTime.parse("06:00"));

        assertThat(existByStartAt).isFalse();
    }
}
