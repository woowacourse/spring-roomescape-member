package roomescape.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.restassured.RestAssured;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.model.ReservationTime;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ReservationTimeRepositoryTest {

    @LocalServerPort
    int port;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ReservationTimeRepository reservationTimeRepository;

    @BeforeEach
    void init() {
        RestAssured.port = port;
        jdbcTemplate.update("INSERT INTO reservation_times (start_at) VALUES (?), (?)", "08:00", "07:00");
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
        final boolean isDeleted = reservationTimeRepository.deleteById(1L);
        assertTrue(isDeleted);
    }

    @DisplayName("존재하지 않는 예약 시간 삭제")
    @Test
    void deleteEmptyById() {
        final boolean isDeleted = reservationTimeRepository.deleteById(100L);
        assertFalse(isDeleted);
    }
}
