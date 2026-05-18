package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import roomescape.controller.dto.ReservationCreateRequest;
import roomescape.controller.dto.ReservationUpdateRequest;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.exception.BusinessRuleViolationException;
import roomescape.exception.ConflictException;
import roomescape.exception.NotFoundException;
import roomescape.repository.ReservationRepository;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    private static final String FUTURE_DATE = LocalDate.now().plusDays(1)
            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    private static final String FUTURE_DATE2 = LocalDate.now().plusDays(2)
            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

    @InjectMocks
    private ReservationService reservationService;

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private ReservationTimeService reservationTimeService;

    @Mock
    private ThemeService themeService;

    @Test
    void 예약을_생성한다() {
        ReservationCreateRequest request = new ReservationCreateRequest("아이큐", FUTURE_DATE, 1L, 1L);
        ReservationTime time = ReservationTime.of(1L, "10:00");
        Theme theme = Theme.of(1L, "공포", "desc", "url");
        Reservation saved = Reservation.of(1L, "아이큐", FUTURE_DATE, time, theme);

        given(reservationTimeService.find(1L)).willReturn(time);
        given(themeService.find(1L)).willReturn(theme);
        given(reservationRepository.findByTimeAndTheme(any(), any())).willReturn(List.of());
        given(reservationRepository.save(any())).willReturn(saved);

        Reservation result = reservationService.reserve(request);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("아이큐");
    }

    @Test
    void 이름으로_예약을_조회한다() {
        ReservationTime time = ReservationTime.of(1L, "10:00");
        Theme theme = Theme.of(1L, "공포", "desc", "url");
        List<Reservation> reservations = List.of(
                Reservation.of(1L, "아이큐", FUTURE_DATE, time, theme)
        );
        given(reservationRepository.findByName("아이큐")).willReturn(reservations);

        List<Reservation> result = reservationService.findByName("아이큐");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("아이큐");
    }

    @Test
    void 예약을_변경한다() {
        ReservationTime oldTime = ReservationTime.of(1L, "10:00");
        ReservationTime newTime = ReservationTime.of(2L, "11:00");
        Theme theme = Theme.of(1L, "공포", "desc", "url");
        Reservation existing = Reservation.of(1L, "아이큐", FUTURE_DATE, oldTime, theme);
        LocalDate newDate = LocalDate.parse(FUTURE_DATE2);

        given(reservationRepository.findById(1L)).willReturn(Optional.of(existing));
        given(reservationTimeService.find(2L)).willReturn(newTime);
        given(reservationRepository.findByTimeAndTheme(any(), any())).willReturn(List.of());

        ReservationUpdateRequest request = new ReservationUpdateRequest(FUTURE_DATE2, 2L);
        Reservation result = reservationService.update(1L, request);

        assertThat(result.getTime().getId()).isEqualTo(2L);
        verify(reservationRepository).update(1L, newDate, 2L);
    }

    @Test
    void 중복_예약시_ConflictException이_발생한다() {
        ReservationCreateRequest request = new ReservationCreateRequest("아이큐", FUTURE_DATE, 1L, 1L);
        ReservationTime time = ReservationTime.of(1L, "10:00");
        Theme theme = Theme.of(1L, "공포", "desc", "url");
        Reservation existing = Reservation.of(1L, "기존유저", FUTURE_DATE, time, theme);

        given(reservationTimeService.find(1L)).willReturn(time);
        given(themeService.find(1L)).willReturn(theme);
        given(reservationRepository.findByTimeAndTheme(any(), any())).willReturn(List.of(existing));

        assertThatThrownBy(() -> reservationService.reserve(request))
                .isInstanceOf(ConflictException.class)
                .hasMessage("해당 날짜, 테마, 시간에 이미 중복된 예약이 존재합니다.");
    }

    @Test
    void 존재하지_않는_예약_취소시_NotFoundException이_발생한다() {
        given(reservationRepository.findById(999L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> reservationService.cancel(999L))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("요청한 예약을 찾을 수 없습니다.");
    }

    @Test
    void 과거_날짜_예약시_BusinessRuleViolationException이_발생한다() {
        ReservationCreateRequest request = new ReservationCreateRequest("아이큐", "2020-01-01", 1L, 1L);
        ReservationTime time = ReservationTime.of(1L, "10:00");
        Theme theme = Theme.of(1L, "공포", "desc", "url");

        given(reservationTimeService.find(1L)).willReturn(time);
        given(themeService.find(1L)).willReturn(theme);

        assertThatThrownBy(() -> reservationService.reserve(request))
                .isInstanceOf(BusinessRuleViolationException.class)
                .hasMessage("지나간 날짜·시간에는 예약할 수 없습니다.");
    }

    @Test
    void 과거_날짜로_변경시_BusinessRuleViolationException이_발생한다() {
        ReservationTime oldTime = ReservationTime.of(1L, "10:00");
        ReservationTime newTime = ReservationTime.of(2L, "10:00");
        Theme theme = Theme.of(1L, "공포", "desc", "url");
        Reservation existing = Reservation.of(1L, "아이큐", FUTURE_DATE, oldTime, theme);

        given(reservationRepository.findById(1L)).willReturn(Optional.of(existing));
        given(reservationTimeService.find(2L)).willReturn(newTime);

        ReservationUpdateRequest request = new ReservationUpdateRequest("2020-01-01", 2L);

        assertThatThrownBy(() -> reservationService.update(1L, request))
                .isInstanceOf(BusinessRuleViolationException.class)
                .hasMessage("지나간 날짜·시간에는 예약할 수 없습니다.");
    }

    @Test
    void 변경하려는_시간이_이미_차있으면_ConflictException이_발생한다() {
        ReservationTime oldTime = ReservationTime.of(1L, "10:00");
        ReservationTime newTime = ReservationTime.of(2L, "11:00");
        Theme theme = Theme.of(1L, "공포", "desc", "url");
        Reservation existing = Reservation.of(1L, "아이큐", FUTURE_DATE, oldTime, theme);
        Reservation other = Reservation.of(2L, "다른유저", FUTURE_DATE2, newTime, theme);

        given(reservationRepository.findById(1L)).willReturn(Optional.of(existing));
        given(reservationTimeService.find(2L)).willReturn(newTime);
        given(reservationRepository.findByTimeAndTheme(any(), any())).willReturn(List.of(other));

        ReservationUpdateRequest request = new ReservationUpdateRequest(FUTURE_DATE2, 2L);

        assertThatThrownBy(() -> reservationService.update(1L, request))
                .isInstanceOf(ConflictException.class)
                .hasMessage("해당 날짜, 테마, 시간에 이미 중복된 예약이 존재합니다.");
    }

    @Test
    void 존재하지_않는_테마로_예약시_NotFoundException이_발생한다() {
        ReservationCreateRequest request = new ReservationCreateRequest("아이큐", FUTURE_DATE, 1L, 999L);
        ReservationTime time = ReservationTime.of(1L, "10:00");

        given(reservationTimeService.find(1L)).willReturn(time);
        given(themeService.find(999L)).willThrow(new NotFoundException("존재하지 않는 테마입니다"));

        assertThatThrownBy(() -> reservationService.reserve(request))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("존재하지 않는 테마입니다");
    }
}
