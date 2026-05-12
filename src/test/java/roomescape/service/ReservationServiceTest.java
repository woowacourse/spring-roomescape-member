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

        Reservation saved = reservationRepository.save(Reservation.createNew("쿠다", LocalDate.now().plusDays(1), time));

        assertThat(saved.getId()).isNotNull();
        assertThat(reservationRepository.findAll()).hasSize(1);
    }


    @Test
    @DisplayName("예약 정상 삭제")
    void deleteById_success() {
        // given
        Theme theme = Theme.of(1L, "미술관의 밤", "설명", "thumb");
        ReservationTime time = reservationTimeRepository.save(
                ReservationTime.createNew(LocalTime.of(10, 0), theme)
        );

        Reservation reservation = reservationRepository.save(
                Reservation.createNew("쿠다", LocalDate.now().plusDays(1), time));

        // when
        reservationService.deleteById(reservation.getId());

        // then
        assertThat(reservationRepository.findAll()).isEmpty();
    }

    @Test
    @DisplayName("이름이 다른 예약 삭제 예외")
    void deleteById_whenNameMismatch_throws() {
        // given
        Theme theme = Theme.of(1L, "미술관의 밤", "설명", "thumb");
        ReservationTime time = reservationTimeRepository.save(
                ReservationTime.createNew(LocalTime.of(10, 0), theme)
        );

        Reservation reservation = reservationRepository.save(
                Reservation.createNew("쿠다", LocalDate.now().plusDays(1), time));

        // when & then
        assertThatThrownBy(() -> reservationService.deleteById(reservation.getId(), "피케이"))
                .isInstanceOf(ReservationBadRequestException.class);
    }

    @Test
    @DisplayName("예약 수정 성공")
    void update_success() {
        // given
        Theme theme = Theme.of(1L, "미술관의 밤", "설명", "thumb");
        ReservationTime time1 = reservationTimeRepository.save(
                ReservationTime.createNew(LocalTime.of(10, 0), theme)
        );
        ReservationTime time2 = reservationTimeRepository.save(
                ReservationTime.createNew(LocalTime.of(11, 0), theme)
        );

        Reservation reservation = reservationRepository.save(
                Reservation.createNew("쿠다", LocalDate.now().plusDays(1), time1));

        // when
        reservationService.update(reservation.getId(), "쿠다", LocalDate.now().plusDays(2), time2.getId());

        // then
        Reservation updated = reservationRepository.findById(reservation.getId()).orElseThrow();
        assertThat(updated.getDate()).isEqualTo(LocalDate.now().plusDays(2));
        assertThat(updated.getTime().getId()).isEqualTo(time2.getId());
    }

    @Test
    @DisplayName("예약 수정 시 이름이 다른 경우 예외")
    void update_whenNameMismatch_throws() {
        // given
        Theme theme = Theme.of(1L, "미술관의 밤", "설명", "thumb");
        ReservationTime time = reservationTimeRepository.save(
                ReservationTime.createNew(LocalTime.of(10, 0), theme)
        );

        Reservation reservation = reservationRepository.save(
                Reservation.createNew("쿠다", LocalDate.now().plusDays(1), time));

        // when & then
        assertThatThrownBy(
                () -> reservationService.update(reservation.getId(), "피케이", LocalDate.now().plusDays(2), time.getId()))
                .isInstanceOf(ReservationBadRequestException.class);
    }

    @Test
    @DisplayName("예약 수정 시 과거 날짜/시간 예외")
    void update_whenPastDateTime_throws() {
        // given
        Theme theme = Theme.of(1L, "미술관의 밤", "설명", "thumb");
        ReservationTime time = reservationTimeRepository.save(
                ReservationTime.createNew(LocalTime.of(10, 0), theme)
        );

        Reservation reservation = reservationRepository.save(
                Reservation.createNew("쿠다", LocalDate.now().plusDays(1), time));

        // when & then
        assertThatThrownBy(() -> reservationService.update(reservation.getId(), "피케이", LocalDate.now().minusDays(10),
                time.getId()))
                .isInstanceOf(ReservationBadRequestException.class);
    }

    @Test
    @DisplayName("예약 수정 시 같은 날짜/시간 중복 예외")
    void update_whenDuplicateDateTime_throws() {
        // given
        Theme theme = Theme.of(1L, "미술관의 밤", "설명", "thumb");
        ReservationTime time1 = reservationTimeRepository.save(
                ReservationTime.createNew(LocalTime.of(10, 0), theme)
        );
        ReservationTime time2 = reservationTimeRepository.save(
                ReservationTime.createNew(LocalTime.of(11, 0), theme)
        );

        Reservation reservation1 = reservationRepository.save(
                Reservation.createNew("쿠다", LocalDate.now().plusDays(1), time1));
        Reservation reservation2 = reservationRepository.save(
                Reservation.createNew("피케이", LocalDate.now().plusDays(1), time2));

        // when & then
        assertThatThrownBy(() -> reservationService.update(reservation1.getId(), "쿠다", reservation2.getDate(),
                time2.getId())).isInstanceOf(ReservationDuplicateException.class);
    }


}
