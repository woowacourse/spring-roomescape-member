package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import roomescape.dto.ReservationResponse;
import roomescape.dto.ReservationSaveRequest;
import roomescape.model.Reservation;
import roomescape.model.ReservationTime;
import roomescape.model.Theme;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;
import roomescape.repository.ThemeRepository;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
class ReservationServiceTest {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ReservationTimeRepository reservationTimeRepository;

    @Autowired
    private ThemeRepository themeRepository;

    @BeforeEach
    void init() {
        final ReservationTime time = reservationTimeRepository.save(new ReservationTime(LocalTime.parse("10:00")));
        final Theme theme = themeRepository.save(new Theme("이름", "설명", "썸네일"));
        reservationRepository.save(new Reservation("감자", LocalDate.parse("2025-05-13"), time, theme));
    }

    @DisplayName("예약 목록 조회")
    @Test
    void getReservations() {
        final List<ReservationResponse> reservationResponses = reservationService.getReservations();
        assertThat(reservationResponses.size()).isEqualTo(1);
    }

    @DisplayName("예약 저장")
    @Test
    void saveReservation() {
        final ReservationSaveRequest reservationSaveRequest = new ReservationSaveRequest("고구마",  LocalDate.parse("2025-11-11"), 1L, 1L);
        reservationService.saveReservation(reservationSaveRequest);
        assertThat(reservationRepository.findById(2L)).isPresent();
    }

    @DisplayName("존재하지 않는 예약 시간으로 예약 저장")
    @Test
    void timeForSaveReservationNotFound() {
        final ReservationSaveRequest reservationSaveRequest = new ReservationSaveRequest("고구마", LocalDate.parse("2025-11-11"), 2L, 1L);
        assertThatThrownBy(() -> {
            reservationService.saveReservation(reservationSaveRequest);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("예약 삭제")
    @Test
    void deleteReservation() {
        reservationService.deleteReservation(1L);
        assertThat(reservationService.getReservations().size()).isEqualTo(0);
    }

    @DisplayName("존재하지 않는 예약 삭제")
    @Test
    void deleteReservationNotFound() {
        assertThatThrownBy(() -> {
            reservationService.deleteReservation(2L);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("지나간 시간 예약 저장")
    @Test
    void saveReservationWithGoneTime() {
        assertThatThrownBy(() -> {
            reservationService.saveReservation(new ReservationSaveRequest("백호", LocalDate.parse("2023-11-11"), 1L, 1L));
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("중복된 예약 저장")
    @Test
    void saveDuplicatedReservation() {
        assertThatThrownBy(() -> {
            reservationService.saveReservation(new ReservationSaveRequest("호롤로", LocalDate.parse("2025-05-13"), 1L, 1L));
        }).isInstanceOf(IllegalArgumentException.class);
    }
}
