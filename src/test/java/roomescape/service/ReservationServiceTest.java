package roomescape.service;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.dto.reservation.ReservationCreateRequest;
import roomescape.dto.reservation.ReservationResponse;
import roomescape.exception.BadRequestException;
import roomescape.repository.reservation.ReservationRepository;
import roomescape.repository.reservationtime.ReservationTimeRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
@DisplayName("예약 서비스")
class ReservationServiceTest {

    private ReservationService reservationService;
    @Mock
    private ReservationTimeRepository reservationTimeRepository;
    @Mock
    private ReservationRepository reservationRepository;
    private Long id;
    private String name;
    private LocalTime startAt;
    private LocalDate date;
    private Reservation reservationFixture;

    @BeforeEach
    void setUp() {
        this.reservationService = new ReservationService(reservationRepository, reservationTimeRepository);
        this.id = 1L;
        this.name = "클로버";
        this.startAt = LocalTime.of(10, 10);
        this.date = LocalDate.of(2024, 11, 16);
        this.reservationFixture = new Reservation(id, name, date, new ReservationTime(id, startAt));
    }

    @DisplayName("예약 서비스는 예약들을 조회한다.")
    @Test
    void readReservations() {
        // given
        Mockito.when(reservationRepository.findAll())
                .thenReturn(List.of(reservationFixture));

        // when
        List<ReservationResponse> reservations = reservationService.readReservations();

        // then
        assertThat(reservations.size()).isEqualTo(1);
    }

    @DisplayName("예약 서비스는 id에 맞는 예약을 조회한다.")
    @Test
    void readReservation() {
        // given
        Mockito.when(reservationRepository.findById(id))
                .thenReturn(Optional.of(reservationFixture));

        // when
        ReservationResponse reservation = reservationService.readReservation(id);

        // then
        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(reservation.date()).isEqualTo(date);
        softAssertions.assertThat(reservation.name()).isEqualTo(name);
        softAssertions.assertAll();
    }

    @DisplayName("예약 서비스는 예약을 생성한다.")
    @Test
    void createReservation() {
        // given
        Mockito.when(reservationTimeRepository.findById(id))
                .thenReturn(Optional.of(new ReservationTime(id, startAt)));
        ReservationCreateRequest request = new ReservationCreateRequest(name, date, 1L);
        Mockito.when(reservationRepository.save(any()))
                .thenReturn(reservationFixture);

        // when
        ReservationResponse reservation = reservationService.createReservation(request);

        // then
        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(reservation.date()).isEqualTo(date);
        softAssertions.assertThat(reservation.name()).isEqualTo(name);
        softAssertions.assertThat(reservation.time().getStartAt()).isEqualTo(startAt);
        softAssertions.assertAll();
    }

    @DisplayName("예약 서비스는 지난 시점의 예약이 요청되면 예외가 발생한다.")
    @Test
    void validateRequestedTime() {
        // given
        Mockito.when(reservationTimeRepository.findById(id))
                .thenReturn(Optional.of(new ReservationTime(id, startAt)));

        LocalDate date = LocalDate.MIN;
        ReservationCreateRequest request = new ReservationCreateRequest(name, date, id);

        // when & then
        assertThatThrownBy(() -> reservationService.createReservation(request))
                .isInstanceOf(BadRequestException.class);
    }

    @DisplayName("예약 서비스는 id에 맞는 예약을 삭제한다.")
    @Test
    void deleteReservation() {
        // given
        Mockito.doNothing().when(reservationRepository).deleteById(id);

        // when & then
        assertThatCode(() -> reservationService.deleteReservation(id))
                .doesNotThrowAnyException();
    }
}
