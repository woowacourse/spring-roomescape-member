package roomescape.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationRepository;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;

@SpringBootTest(webEnvironment = WebEnvironment.NONE)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
class ReservationRepositoryTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ReservationRepository reservationRepository;

    @BeforeEach
    void init() {
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES (?), (?)", "08:00", "07:00");
        jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail) VALUES (?, ?, ?)", " 이름", "설명", "썸네일");
        jdbcTemplate.update("INSERT INTO reservation (name, date, time_id, theme_id) VALUES (?, ?, ?, ?), (?, ?, ?, ?)",
                "감자", "2024-07-07", 1L, 1L,
                "고구마", "2024-08-12", 2L, 1L);
    }

    @DisplayName("예약 생성")
    @Test
    void save() {
        final ReservationTime reservationTime = new ReservationTime(1L, LocalTime.parse("08:00"));
        final Theme theme = new Theme(1L, "이름", "설명", "썸네일");
        final Reservation reservation = new Reservation("생강", LocalDate.parse("2025-01-01"), reservationTime, theme);
        final Reservation savedReservation = reservationRepository.save(reservation);
        assertAll(
                () -> assertThat(savedReservation.getId()).isEqualTo(3L),
                () -> assertThat(savedReservation.getName()).isEqualTo("생강"),
                () -> assertThat(savedReservation.getDate()).isEqualTo("2025-01-01"),
                () -> assertThat(savedReservation.getTime()).isEqualTo(reservationTime)
        );
    }

    @DisplayName("예약 목록 조회")
    @Test
    void findAll() {
        final List<Reservation> reservations = reservationRepository.findAll();
        assertThat(reservations.size()).isEqualTo(2);
    }

    @DisplayName("존재하는 예약 삭제")
    @Test
    void deleteExistById() {
        assertThatCode(() -> reservationRepository.deleteById(1L))
                .doesNotThrowAnyException();
    }
}
