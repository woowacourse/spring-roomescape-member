package roomescape.reservation.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalTime;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import roomescape.reservation.controller.dto.CreateReservationRequest;
import roomescape.reservation.controller.dto.ReservationResponse;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.reservation.repository.dto.CreateReservationParams;
import roomescape.theme.domain.Theme;
import roomescape.theme.repository.ThemeRepository;
import roomescape.time.domain.ReservationTime;
import roomescape.time.repository.ReservationTimeRepository;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    @Mock
    ReservationRepository reservationRepository;

    @Mock
    ThemeRepository themeRepository;

    @Mock
    ReservationTimeRepository reservationTimeRepository;

    @InjectMocks
    ReservationService reservationService;

    @Nested
    @DisplayName("예약 삭제")
    class deleteById {

        @Test
        void 삭제_요청_시_이미_삭제된_예약인_경우_예외가_발생한다() {
            // given
            Long id = 1L;

            when(reservationRepository.findById(id)).thenThrow(new IllegalArgumentException());

            // when & then
            Assertions.assertThatThrownBy(() -> reservationService.cancelReservation(id))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 과거_예약을_삭제하는_경우_거부한다() {
            // given
            LocalDate pastDate = LocalDate.now().minusDays(1);
            Reservation pastReservation = new Reservation(1L, "userA", pastDate,
                    new ReservationTime(1L, LocalTime.now()),
                    new Theme(1L, "themeA", "hello", "/image/..."));

            when(reservationRepository.findById(any(Long.class))).thenReturn(pastReservation);

            // when & then
            Assertions.assertThatThrownBy(() -> reservationService.cancelReservation(any(Long.class)))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 식별자에_해당하는_예약을_삭제한다() {
            // given
            LocalDate date = LocalDate.now().plusDays(1);
            Reservation reservation = new Reservation(1L, "userA", date,
                    new ReservationTime(1L, LocalTime.now()),
                    new Theme(1L, "themeA", "hello", "/image/..."));

            when(reservationRepository.findById(any(Long.class))).thenReturn(reservation);
            when(reservationRepository.deleteById(any(Long.class))).thenReturn(1);

            // when & then
            Assertions.assertThatCode(() -> reservationService.cancelReservation(any(Long.class)))
                    .doesNotThrowAnyException();
        }
    }

    @Nested
    @DisplayName("예약 생성")
    class reserve {

        @Test
        void 예약을_생성한다() {
            // given
            CreateReservationRequest request = new CreateReservationRequest("userA", LocalDate.now(), 1L, 1L);

            when(reservationRepository.save(any(CreateReservationParams.class))).thenReturn(
                    new Reservation(1L, request.name(), request.date(),
                            new ReservationTime(1L, LocalTime.now()),
                            new Theme(1L, "themeA", "hello", "/image/...")));
            when(reservationRepository.existsByDateAndTimeIdAndThemeId(any(DuplicateReservationCondition.class)))
                    .thenReturn(false);
            when(themeRepository.existsById(any(Long.class)))
                    .thenReturn(true);
            when(reservationTimeRepository.findById(any(Long.class)))
                    .thenReturn(new ReservationTime(1L, LocalTime.now().plusMinutes(5)));

            // when
            ReservationResponse reservationResponse = reservationService.reserve(request);

            // then
            Assertions.assertThat(reservationResponse.id()).isEqualTo(1L);
        }

        @Test
        void 이미_예약이_존재하는_경우_예약을_거부한다() {
            //given
            CreateReservationRequest request = new CreateReservationRequest("userA", LocalDate.now(), 1L, 1L);

            when(reservationRepository.existsByDateAndTimeIdAndThemeId(any(DuplicateReservationCondition.class)))
                    .thenReturn(true);
            when(themeRepository.existsById(any(Long.class)))
                    .thenReturn(true);
            when(reservationTimeRepository.findById(any(Long.class)))
                    .thenReturn(new ReservationTime(1L, LocalTime.now().plusMinutes(5)));

            //when & then
            Assertions.assertThatThrownBy(() -> reservationService.reserve(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 예약_시간이_과거인_경우_예약을_거부한다() {
            //given
            CreateReservationRequest request = new CreateReservationRequest("userA", LocalDate.now(), 1L, 1L);

            when(themeRepository.existsById(any(Long.class)))
                    .thenReturn(true);
            when(reservationTimeRepository.findById(any(Long.class)))
                    .thenReturn(new ReservationTime(1L, LocalTime.now().minusMinutes(5)));

            //when & then
            Assertions.assertThatThrownBy(() -> reservationService.reserve(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
