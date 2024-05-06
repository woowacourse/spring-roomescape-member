package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationRepository;
import roomescape.domain.ReservationTime;
import roomescape.domain.ReservationTimeRepository;
import roomescape.domain.Theme;
import roomescape.exception.ReservationBusinessException;
import roomescape.repository.ThemeJdbcRepository;
import roomescape.service.dto.ReservationResponse;
import roomescape.service.dto.ReservationSaveRequest;

@SpringBootTest
@Transactional
class ReservationServiceTest {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ReservationTimeRepository reservationTimeRepository;

    @Autowired
    private ThemeJdbcRepository themeJdbcRepository;

    @DisplayName("예약 저장")
    @Test
    void saveReservation() {
        final ReservationTime time = reservationTimeRepository.save(new ReservationTime(LocalTime.parse("10:00")));
        final Theme theme = themeJdbcRepository.save(new Theme("이름", "설명", "썸네일"));

        final ReservationSaveRequest reservationSaveRequest = new ReservationSaveRequest("고구마",
                LocalDate.parse("2025-11-11"), time.getId(), theme.getId());
        final ReservationResponse reservationResponse = reservationService.saveReservation(reservationSaveRequest);

        assertAll(
                () -> assertThat(reservationResponse.name()).isEqualTo("고구마"),
                () -> assertThat(reservationResponse.date()).isEqualTo(LocalDate.parse("2025-11-11")),
                () -> assertThat(reservationResponse.time().id()).isEqualTo(time.getId()),
                () -> assertThat(reservationResponse.time().startAt()).isEqualTo(time.getStartAt()),
                () -> assertThat(reservationResponse.theme().id()).isEqualTo(theme.getId()),
                () -> assertThat(reservationResponse.theme().name()).isEqualTo(theme.getName()),
                () -> assertThat(reservationResponse.theme().description()).isEqualTo(theme.getDescription()),
                () -> assertThat(reservationResponse.theme().thumbnail()).isEqualTo(theme.getThumbnail())
        );
    }

    @DisplayName("존재하지 않는 예약 시간으로 예약 저장")
    @Test
    void timeForSaveReservationNotFound() {
        final ReservationSaveRequest reservationSaveRequest = new ReservationSaveRequest("고구마",
                LocalDate.parse("2025-11-11"), 2L, 1L);
        assertThatThrownBy(() -> {
            reservationService.saveReservation(reservationSaveRequest);
        }).isInstanceOf(ReservationBusinessException.class);
    }

    @DisplayName("예약 삭제")
    @Test
    void deleteReservation() {
        final ReservationTime time = reservationTimeRepository.save(new ReservationTime(LocalTime.parse("10:00")));
        final Theme theme = themeJdbcRepository.save(new Theme("이름", "설명", "썸네일"));
        final Reservation savedReservation = reservationRepository.save(
                new Reservation("감자", LocalDate.parse("2025-05-13"), time, theme));

        reservationService.deleteReservation(savedReservation.getId());
        assertThat(reservationService.getReservations().size()).isEqualTo(0);
    }

    @DisplayName("존재하지 않는 예약 삭제")
    @Test
    void deleteReservationNotFound() {
        assertThatThrownBy(() -> {
            reservationService.deleteReservation(2L);
        }).isInstanceOf(ReservationBusinessException.class);
    }

    @DisplayName("중복된 예약 저장")
    @Test
    void saveDuplicatedReservation() {
        final ReservationTime time = reservationTimeRepository.save(new ReservationTime(LocalTime.parse("10:00")));
        final Theme theme = themeJdbcRepository.save(new Theme("이름", "설명", "썸네일"));
        final Reservation savedReservation = reservationRepository.save(
                new Reservation("감자", LocalDate.parse("2025-05-13"), time, theme));

        assertThatThrownBy(() -> {
            reservationService.saveReservation(
                    new ReservationSaveRequest("호롤로", LocalDate.parse("2025-05-13"), time.getId(), theme.getId()));
        }).isInstanceOf(ReservationBusinessException.class);
    }
}
