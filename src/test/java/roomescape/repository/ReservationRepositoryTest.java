package roomescape.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.jdbc.Sql;
import roomescape.model.Reservation;
import roomescape.model.ReservationTime;
import roomescape.model.Theme;

@SpringBootTest(webEnvironment = WebEnvironment.NONE)
@Sql(scripts = {"/test.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class ReservationRepositoryTest {

    @Autowired
    private ReservationRepository reservationRepository;

    @DisplayName("예약 생성")
    @Test
    void save() {
        final ReservationTime reservationTime = new ReservationTime(1L, LocalTime.parse("09:00"));
        final Theme theme = new Theme(1L, "이름1", "설명1", "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg");
        final Reservation reservation = reservationRepository.save(new Reservation("생강", LocalDate.parse("2025-01-01"), reservationTime, theme));
        final Reservation savedReservation = reservationRepository.findById(reservation.getId()).get();
        assertAll(
                () -> assertThat(savedReservation.getName()).isEqualTo("생강"),
                () -> assertThat(savedReservation.getDate()).isEqualTo("2025-01-01"),
                () -> assertThat(savedReservation.getTimeId()).isEqualTo(1L),
                () -> assertThat(savedReservation.getThemeId()).isEqualTo(1L)
        );
    }

    @DisplayName("예약 목록 조회")
    @Test
    void findAll() {
        final List<Reservation> reservations = reservationRepository.findAll();
        assertThat(reservations.size()).isEqualTo(13);
    }

    @DisplayName("예약 삭제")
    @Test
    void deleteExistById() {
        reservationRepository.deleteById(1L);
        assertThat(reservationRepository.findById(1L)).isEmpty();
    }

    @DisplayName("날짜와 테마로 예약 조회")
    @Test
    void findByDateAndThemeId() {
        final List<Reservation> reservations = reservationRepository.findByDateAndThemeId(LocalDate.parse("2024-04-29"), 2L);
        assertThat(reservations).hasSize(2);
    }
}
