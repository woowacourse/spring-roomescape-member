package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.exception.ReservationBadRequestException;
import roomescape.reservation.exception.ReservationDuplicateException;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.reservation.service.ReservationService;
import roomescape.reservationtime.domain.ReservationTime;
import roomescape.reservationtime.exception.ReservationTimeNotFoundException;
import roomescape.reservationtime.repository.ReservationTimeRepository;
import roomescape.service.stub.FakeReservationRepository;
import roomescape.service.stub.FakeReservationTimeRepository;
import roomescape.theme.domain.Theme;

class ReservationServiceTest {

    private ReservationService reservationService;
    private ReservationRepository reservationRepository;
    private ReservationTimeRepository reservationTimeRepository;

    @BeforeEach
    void setUp() {
        reservationRepository = new FakeReservationRepository();
        reservationTimeRepository = new FakeReservationTimeRepository();
        reservationService = new ReservationService(reservationRepository, reservationTimeRepository);
    }

    @Test
    @DisplayName("없는 예약 시간 ID 예약 예외")
    void save_whenTimeNotExists_throws() {
        assertThatThrownBy(() -> reservationService.save("쿠다", LocalDate.now().plusDays(1), 999L))
                .isInstanceOf(ReservationTimeNotFoundException.class);
    }

    @Test
    @DisplayName("과거 날짜/시간 예약 예외")
    void save_whenPastDateTime_throws() {
        Theme theme = Theme.of(1L, "미술관의 밤", "설명", "thumb");
        ReservationTime time = reservationTimeRepository.save(
                ReservationTime.createNew(LocalTime.of(10, 0), theme)
        );

        assertThatThrownBy(() -> reservationService.save("쿠다", LocalDate.now().minusDays(1), time.getId()))
                .isInstanceOf(ReservationBadRequestException.class);
    }

    @Test
    @DisplayName("같은 날짜/시간 중복 예약 예외 발생")
    void save_whenDuplicateDateTime_throws() {
        Theme theme = Theme.of(1L, "미술관의 밤", "설명", "thumb");
        ReservationTime time = reservationTimeRepository.save(
                ReservationTime.createNew(LocalTime.of(10, 0), theme)
        );
        LocalDate date = LocalDate.now().plusDays(1);

        reservationRepository.save(Reservation.createNew("기존예약", date, time));

        assertThatThrownBy(() -> reservationService.save("신규예약", date, time.getId()))
                .isInstanceOf(ReservationDuplicateException.class);
    }

    @Test
    @DisplayName("정상 케이스 예약 저장")
    void save_success() {
        Theme theme = Theme.of(1L, "미술관의 밤", "설명", "thumb");
        ReservationTime time = reservationTimeRepository.save(
                ReservationTime.createNew(LocalTime.of(10, 0), theme)
        );

        Reservation saved = reservationService.save("쿠다", LocalDate.now().plusDays(1), time.getId());

        assertThat(saved.getId()).isNotNull();
        assertThat(reservationRepository.findAll()).hasSize(1);
    }

}
