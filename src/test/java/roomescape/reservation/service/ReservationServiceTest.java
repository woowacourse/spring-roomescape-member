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
import roomescape.global.exception.policy.PastReservationNotAllowedException;
import roomescape.global.exception.policy.ReservationConflictException;
import roomescape.global.exception.policy.ReservationUpdateNotAllowedException;
import roomescape.global.exception.validation.ReservationNotFoundException;
import roomescape.global.exception.validation.ThemeNotFoundException;
import roomescape.reservation.controller.dto.CreateReservationRequest;
import roomescape.reservation.controller.dto.ReservationResponse;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.reservation.repository.dto.CreateReservationParams;
import roomescape.reservation.repository.dto.DuplicateReservationCondition;
import roomescape.reservation.service.dto.RescheduleReservationInfo;
import roomescape.theme.repository.ThemeRepository;
import roomescape.time.repository.ReservationTimeRepository;
import roomescape.util.fixture.ReservationFixture;
import roomescape.util.fixture.ReservationTimeFixture;

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

            when(reservationRepository.findById(id)).thenThrow(new ReservationNotFoundException());

            // when & then
            Assertions.assertThatThrownBy(() -> reservationService.deleteReservation(id))
                    .isInstanceOf(ReservationNotFoundException.class);
        }

        @Test
        void 과거_예약을_삭제하는_경우_거부한다() {
            // given
            LocalDate pastDate = LocalDate.now().minusDays(1);
            Reservation pastReservation = ReservationFixture.createByDate(pastDate);

            when(reservationRepository.findById(any(Long.class))).thenReturn(pastReservation);

            // when & then
            Assertions.assertThatThrownBy(() -> reservationService.deleteReservation(any(Long.class)))
                    .isInstanceOf(ReservationUpdateNotAllowedException.class);
        }

        @Test
        void 식별자에_해당하는_예약을_삭제한다() {
            // given
            LocalDate date = LocalDate.now().plusDays(1);
            Reservation reservation = ReservationFixture.createByDate(date);

            when(reservationRepository.findById(any(Long.class))).thenReturn(reservation);
            when(reservationRepository.deleteById(any(Long.class))).thenReturn(1);

            // when & then
            Assertions.assertThatCode(() -> reservationService.deleteReservation(any(Long.class)))
                    .doesNotThrowAnyException();
        }
    }

    @Nested
    @DisplayName("예약 취소")
    class cancelById {

        @Test
        void 취소_요청_시_이미_취소된_예약인_경우_예외가_발생한다() {
            // given
            Long id = 1L;
            Reservation reservation = ReservationFixture.createCancelled();

            when(reservationRepository.findById(id)).thenReturn(reservation);

            // when & then
            Assertions.assertThatThrownBy(() -> reservationService.cancelReservation(id))
                    .isInstanceOf(ReservationUpdateNotAllowedException.class);
        }

        @Test
        void 취소_요청_시_이미_삭제된_예약인_경우_예외가_발생한다() {
            // given
            Long id = 1L;

            when(reservationRepository.findById(id))
                    .thenThrow(new ReservationNotFoundException());

            // when & then
            Assertions.assertThatThrownBy(() -> reservationService.cancelReservation(id))
                    .isInstanceOf(ReservationNotFoundException.class);
        }

        @Test
        void 과거_예약을_취소하는_경우_거부한다() {
            // given
            LocalDate pastDate = LocalDate.now().minusDays(1);
            Reservation pastReservation = ReservationFixture.createByDate(pastDate);

            when(reservationRepository.findById(any(Long.class))).thenReturn(pastReservation);

            // when & then
            Assertions.assertThatThrownBy(() -> reservationService.cancelReservation(any(Long.class)))
                    .isInstanceOf(ReservationUpdateNotAllowedException.class);
        }

        @Test
        void 식별자에_해당하는_예약을_취소한다() {
            // given
            LocalDate date = LocalDate.now().plusDays(1);
            Reservation reservation = ReservationFixture.createByDate(date);

            when(reservationRepository.findById(any(Long.class))).thenReturn(reservation);
            when(reservationRepository.cancelById(any(Long.class))).thenReturn(1);

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
                    ReservationFixture.createByDate(request.date()));
            when(reservationRepository.existsByDateAndTimeIdAndThemeId(any(DuplicateReservationCondition.class)))
                    .thenReturn(false);
            when(themeRepository.existsById(any(Long.class)))
                    .thenReturn(true);
            when(reservationTimeRepository.findById(any(Long.class)))
                    .thenReturn(ReservationTimeFixture.create(LocalTime.now().plusMinutes(5)));

            // when
            ReservationResponse reservationResponse = reservationService.reserve(request);

            // then
            Assertions.assertThat(reservationResponse).isNotNull();
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
                    .thenReturn(ReservationTimeFixture.create(LocalTime.now().plusMinutes(5)));

            //when & then
            Assertions.assertThatThrownBy(() -> reservationService.reserve(request))
                    .isInstanceOf(ReservationConflictException.class);
        }

        @Test
        void 예약_시간이_과거인_경우_예약을_거부한다() {
            //given
            CreateReservationRequest request = new CreateReservationRequest("userA", LocalDate.now(), 1L, 1L);

            when(themeRepository.existsById(any(Long.class)))
                    .thenReturn(true);
            when(reservationTimeRepository.findById(any(Long.class)))
                    .thenReturn(ReservationTimeFixture.create(LocalTime.now().minusMinutes(5)));

            //when & then
            Assertions.assertThatThrownBy(() -> reservationService.reserve(request))
                    .isInstanceOf(PastReservationNotAllowedException.class);
        }

        @Test
        void 테마가_존재하지_않는_경우_예약이_실패한다() {
            //given
            Long themeId = 1L;
            CreateReservationRequest request = new CreateReservationRequest("userA", LocalDate.now(), 1L, themeId);

            when(themeRepository.existsById(themeId))
                    .thenReturn(false);

            //when & then
            Assertions.assertThatThrownBy(() -> reservationService.reserve(request))
                    .isInstanceOf(ThemeNotFoundException.class);
        }
    }

    @Nested
    @DisplayName("예약 변경")
    class reschedule {

        @Test
        void 예약을_변경한다() {
            // given
            Long id = 1L;
            LocalDate date = LocalDate.now().plusDays(1);
            Long timeId = 2L;
            RescheduleReservationInfo info = new RescheduleReservationInfo(id, date, timeId);

            Reservation reservation = ReservationFixture.createByDate(LocalDate.now());

            when(reservationRepository.findById(id)).thenReturn(reservation);
            when(reservationTimeRepository.findById(timeId)).thenReturn(ReservationTimeFixture.create(LocalTime.now().plusHours(1)));
            when(themeRepository.existsById(any(Long.class))).thenReturn(true);
            when(reservationRepository.existsByDateAndTimeIdAndThemeId(any(DuplicateReservationCondition.class)))
                    .thenReturn(false);

            // when
            ReservationResponse response = reservationService.rescheduleReservation(info);

            // then
            Assertions.assertThat(response.date()).isEqualTo(date);
        }

        @Test
        void 변경하려는_날짜_시간에_이미_예약이_존재하는_경우_변경을_거부한다() {
            // given
            Long id = 1L;
            LocalDate date = LocalDate.now().plusDays(1);
            Long timeId = 2L;
            RescheduleReservationInfo info = new RescheduleReservationInfo(id, date, timeId);

            Reservation reservation = ReservationFixture.createByDate(LocalDate.now());

            when(reservationRepository.findById(id)).thenReturn(reservation);
            when(reservationTimeRepository.findById(timeId)).thenReturn(ReservationTimeFixture.create(LocalTime.now()));
            when(themeRepository.existsById(any(Long.class))).thenReturn(true);
            when(reservationRepository.existsByDateAndTimeIdAndThemeId(any(DuplicateReservationCondition.class)))
                    .thenReturn(true);

            // when & then
            Assertions.assertThatThrownBy(() -> reservationService.rescheduleReservation(info))
                    .isInstanceOf(ReservationConflictException.class);
        }

        @Test
        void 변경하려는_예약_시간이_과거인_경우_예약을_거부한다() {
            // given
            Long id = 1L;
            LocalDate pastDate = LocalDate.now().minusDays(1);
            Long timeId = 2L;
            RescheduleReservationInfo info = new RescheduleReservationInfo(id, pastDate, timeId);

            Reservation reservation = ReservationFixture.createByDate(LocalDate.now().plusDays(1));

            when(reservationRepository.findById(id)).thenReturn(reservation);
            when(reservationTimeRepository.findById(timeId)).thenReturn(ReservationTimeFixture.create(LocalTime.now()));
            when(themeRepository.existsById(any(Long.class))).thenReturn(true);

            // when & then
            Assertions.assertThatThrownBy(() -> reservationService.rescheduleReservation(info))
                    .isInstanceOf(PastReservationNotAllowedException.class);
        }
    }
}
