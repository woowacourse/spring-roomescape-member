package roomescape.service;

import org.junit.jupiter.api.Test;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.repository.ReservationRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AdminReservationServiceTest {

    private final ReservationRepository reservationRepository = mock();
    private final ReservationCreator reservationCreator = mock();
    private final AdminReservationService service = new AdminReservationService(
            reservationRepository,
            reservationCreator);

    private final LocalDate date = LocalDate.now().plusDays(1);

    @Test
    void 전체_예약_조회_테스트() {
        // given
        ReservationTime time = new ReservationTime(1L, LocalTime.parse("08:00"));
        Theme theme = new Theme(1L, "테스트 테마", "테마 설명", "썸네일 주소");
        List<Reservation> reservations = List.of(
                new Reservation(1L, "브라운", date, time, theme),
                new Reservation(2L, "구구", date, time, theme));
        when(reservationRepository.findAll())
                .thenReturn(reservations);

        // when
        List<Reservation> result = service.findAll();

        // then
        assertThat(result).isEqualTo(reservations);
        verify(reservationRepository).findAll();
    }

    @Test
    void 관리자_예약_생성_테스트() {
        // given
        Long timeId = 1L;
        Long themeId = 1L;
        String name = "브라운";
        ReservationTime time = new ReservationTime(timeId, LocalTime.parse("08:00"));
        Theme theme = new Theme(themeId, "테스트 테마", "테마 설명", "썸네일 주소");
        Reservation reservation = new Reservation(1L, name, date, time, theme);

        when(reservationCreator.create(name, date, timeId, themeId))
                .thenReturn(reservation);

        // when
        Reservation result = service.create(name, date, timeId, themeId);

        // then
        assertThat(result).isEqualTo(reservation);
        verify(reservationCreator).create(name, date, timeId, themeId);
    }

    @Test
    void 예약_삭제_테스트() {
        // given
        Long id = 1L;

        // when
        service.delete(id);

        // then
        verify(reservationRepository).delete(id);
    }
}
