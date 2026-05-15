package roomescape.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;
import roomescape.exception.code.ReservationErrorCode;
import roomescape.exception.custom.BusinessException;
import roomescape.reservation.application.ReservationService;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationTime;
import roomescape.reservation.domain.Theme;
import roomescape.reservation.infra.ReservationRepository;
import roomescape.reservation.infra.ReservationTimeRepository;
import roomescape.reservation.infra.ThemeRepository;
import roomescape.reservation.presentation.dto.request.ReservationSaveRequest;
import roomescape.reservation.presentation.dto.response.ReservationSaveResponse;

@ExtendWith(MockitoExtension.class)
public class ReservationServiceTest {
    private static final Clock FIXED_CLOCK = Clock.fixed(
            Instant.parse("2026-05-06T03:30:00Z"),
            ZoneId.of("Asia/Seoul")
    );

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private ReservationTimeRepository reservationTimeRepository;

    @Mock
    private ThemeRepository themeRepository;

    private ReservationService reservationService;

    @BeforeEach
    void setUp() {
        reservationService = new ReservationService(
                reservationRepository,
                reservationTimeRepository,
                themeRepository,
                FIXED_CLOCK
        );
    }

    @Test
    void 미래_예약이면_정상_저장된다() {
        //given
        ReservationSaveRequest request = new ReservationSaveRequest("브라운", LocalDate.of(2026, 5, 7), 2L, 1L);
        ReservationTime time = new ReservationTime(2L, LocalTime.of(11, 0));
        Theme theme = new Theme(1L, "세기의 도둑", "설명", "thumb");
        Reservation savedReservation = new Reservation(10L, "브라운", request.date(), time, theme);

        when(reservationTimeRepository.findById(2L)).thenReturn(Optional.of(time));
        when(themeRepository.findById(1L)).thenReturn(Optional.of(theme));
        when(reservationRepository.save(any(Reservation.class))).thenReturn(savedReservation);

        //when
        ReservationSaveResponse response = reservationService.save(request);

        //then
        assertThat(response.id()).isEqualTo(10L);
        assertThat(response.themeId()).isEqualTo(1L);
        assertThat(response.name()).isEqualTo("브라운");
        assertThat(response.date()).isEqualTo(LocalDate.of(2026, 5, 7));
        assertThat(response.time().id()).isEqualTo(2L);
        assertThat(response.time().time()).isEqualTo(LocalTime.of(11, 0));
        verify(reservationRepository).save(any(Reservation.class));
    }

    @Test
    void 과거_날짜_예약이면_BusinessException이_발생한다() {
        ReservationSaveRequest request = new ReservationSaveRequest("브라운", LocalDate.of(2026, 5, 5), 2L, 1L);
        ReservationTime time = new ReservationTime(2L, LocalTime.of(11, 0));
        Theme theme = new Theme(1L, "세기의 도둑", "설명", "thumb");

        when(reservationTimeRepository.findById(2L)).thenReturn(Optional.of(time));
        when(themeRepository.findById(1L)).thenReturn(Optional.of(theme));

        assertThatThrownBy(() -> reservationService.save(request))
                .isInstanceOf(BusinessException.class)
                .extracting("errorCode")
                .isEqualTo(ReservationErrorCode.RESERVATION_DATE_TIME_EXPIRED);

        verify(reservationRepository, never()).save(any(Reservation.class));
    }

    @Test
    void 오늘_날짜라도_현재_시각보다_이전_시간이면_예외가_발생한다() {
        ReservationSaveRequest request = new ReservationSaveRequest("브라운", LocalDate.of(2026, 5, 6), 2L, 1L);
        ReservationTime time = new ReservationTime(2L, LocalTime.of(11, 0));
        Theme theme = new Theme(1L, "세기의 도둑", "설명", "thumb");

        when(reservationTimeRepository.findById(2L)).thenReturn(Optional.of(time));
        when(themeRepository.findById(1L)).thenReturn(Optional.of(theme));

        assertThatThrownBy(() -> reservationService.save(request))
                .isInstanceOf(BusinessException.class)
                .extracting("errorCode")
                .isEqualTo(ReservationErrorCode.RESERVATION_DATE_TIME_EXPIRED);

        verify(reservationRepository, never()).save(any(Reservation.class));
    }

    @Test
    void 오늘_날짜라도_현재_시각_이후_시간이면_저장된다() {
        ReservationSaveRequest request = new ReservationSaveRequest("브라운", LocalDate.of(2026, 5, 6), 4L, 1L);
        ReservationTime time = new ReservationTime(4L, LocalTime.of(13, 0));
        Theme theme = new Theme(1L, "세기의 도둑", "설명", "thumb");
        Reservation savedReservation = new Reservation(10L, "브라운", request.date(), time, theme);

        when(reservationTimeRepository.findById(4L)).thenReturn(Optional.of(time));
        when(themeRepository.findById(1L)).thenReturn(Optional.of(theme));
        when(reservationRepository.save(any(Reservation.class))).thenReturn(savedReservation);

        ReservationSaveResponse response = reservationService.save(request);

        assertThat(response.id()).isEqualTo(10L);
        assertThat(response.time().time()).isEqualTo(LocalTime.of(13, 0));
        verify(reservationRepository).save(any(Reservation.class));
    }
}
