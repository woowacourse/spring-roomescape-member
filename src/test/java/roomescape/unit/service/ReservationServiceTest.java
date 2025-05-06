package roomescape.unit.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static roomescape.common.Constant.FIXED_CLOCK;
import static roomescape.integration.fixture.ReservationDateFixture.예약날짜_오늘;

import java.time.LocalTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import roomescape.domain.reservation.Reservation;
import roomescape.domain.theme.Theme;
import roomescape.domain.theme.ThemeDescription;
import roomescape.domain.theme.ThemeName;
import roomescape.domain.theme.ThemeThumbnail;
import roomescape.domain.time.ReservationTime;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;
import roomescape.repository.ThemeRepository;
import roomescape.service.ReservationService;
import roomescape.service.request.ReservationCreateRequest;
import roomescape.service.response.ReservationResponse;

public class ReservationServiceTest {

    private ReservationRepository reservationRepository = mock(ReservationRepository.class);;
    private ReservationTimeRepository reservationTimeRepository = mock(ReservationTimeRepository.class);
    private ThemeRepository themeRepository = mock(ThemeRepository.class);
    private ReservationService service = new ReservationService(
            reservationRepository,
            reservationTimeRepository,
            themeRepository,
            FIXED_CLOCK
    );

    private final LocalTime time = LocalTime.of(10, 0);

    @Test
    void 모든_예약을_조회한다() {
        // given
        ReservationTime reservationTime = new ReservationTime(1L, time);
        Theme theme = new Theme(
                1L,
                new ThemeName("공포"),
                new ThemeDescription("무섭다"),
                new ThemeThumbnail("thumb.jpg")
        );
        Reservation reservation = new Reservation(1L, "홍길동", 예약날짜_오늘.date(), reservationTime, theme);
        when(reservationRepository.findAll()).thenReturn(List.of(reservation));

        // when
        List<ReservationResponse> all = service.findAllReservations();

        // then
        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(all).hasSize(1);
        ReservationResponse response = all.get(0);
        softly.assertThat(response.name()).isEqualTo("홍길동");
        softly.assertThat(response.date()).isEqualTo(예약날짜_오늘.date());
        softly.assertThat(response.time().startAt()).isEqualTo(time);
        softly.assertThat(response.theme().id()).isEqualTo(theme.getId());
        softly.assertThat(response.theme().name()).isEqualTo(theme.getName().name());
        softly.assertThat(response.theme().description()).isEqualTo(theme.getDescription().description());
        softly.assertThat(response.theme().thumbnail()).isEqualTo(theme.getThumbnail().thumbnail());
        softly.assertAll();
    }

    @Test
    void 예약을_생성할_수_있다() {
        ReservationCreateRequest request = new ReservationCreateRequest("홍길동", 예약날짜_오늘.date(), 1L, 1L);
        ReservationTime reservationTime = new ReservationTime(1L, time);
        Theme theme = new Theme(
                1L,
                new ThemeName("공포"),
                new ThemeDescription("무섭다"),
                new ThemeThumbnail("thumb.jpg")
        );
        when(reservationTimeRepository.findById(1L)).thenReturn(Optional.of(reservationTime));
        when(reservationRepository.existSameDateTime(any(), anyLong())).thenReturn(false);
        when(themeRepository.findById(1L)).thenReturn(Optional.of(theme));
        when(reservationRepository.save(any(), any(), any())).thenReturn(
                new Reservation(1L, "홍길동", 예약날짜_오늘.date(), reservationTime, theme)
        );

        ReservationResponse response = service.createReservation(request);

        assertThat(response.name()).isEqualTo("홍길동");
    }

    @Test
    void 예약시간이_없으면_예외가_발생한다() {
        ReservationCreateRequest request = new ReservationCreateRequest("홍길동", 예약날짜_오늘.date(), 999L, 1L);
        when(reservationTimeRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.createReservation(request))
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    void 이미_예약된_시간이면_예외가_발생한다() {
        ReservationCreateRequest request = new ReservationCreateRequest("홍길동", 예약날짜_오늘.date(), 1L, 1L);
        ReservationTime reservationTime = new ReservationTime(1L, time);

        when(reservationTimeRepository.findById(1L)).thenReturn(Optional.of(reservationTime));
        when(reservationRepository.existSameDateTime(any(), eq(1L))).thenReturn(true);

        assertThatThrownBy(() -> service.createReservation(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 테마가_없으면_예외가_발생한다() {
        ReservationCreateRequest request = new ReservationCreateRequest("홍길동", 예약날짜_오늘.date(), 1L, 1L);
        ReservationTime reservationTime = new ReservationTime(1L, time);

        when(reservationTimeRepository.findById(1L)).thenReturn(Optional.of(reservationTime));
        when(reservationRepository.existSameDateTime(any(), eq(1L))).thenReturn(false);
        when(themeRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.createReservation(request))
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    void 예약을_삭제할_수_있다() {
        Reservation reservation = mock(Reservation.class);
        when(reservation.getId()).thenReturn(1L);
        when(reservationRepository.findById(1L)).thenReturn(Optional.of(reservation));

        service.deleteReservationById(1L);

        verify(reservationRepository).deleteById(1L);
    }

    @Test
    void 삭제할_예약이_없으면_예외() {
        when(reservationRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.deleteReservationById(1L))
                .isInstanceOf(NoSuchElementException.class);
    }
}
