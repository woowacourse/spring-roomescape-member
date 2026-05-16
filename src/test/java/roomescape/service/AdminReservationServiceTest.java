package roomescape.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;
import roomescape.repository.ThemeRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AdminReservationServiceTest {

    private final ReservationRepository reservationRepository = mock();
    private final ReservationTimeRepository reservationTimeRepository = mock();
    private final ThemeRepository themeRepository = mock();
    private final AdminReservationService service = new AdminReservationService(
            reservationRepository,
            reservationTimeRepository,
            themeRepository);

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

    @ParameterizedTest
    @MethodSource("adminReservationDateTimes")
    void 관리자_예약_생성_테스트(LocalDate date, LocalTime startAt) {
        // given
        Long id = 1L;
        Long timeId = 1L;
        Long themeId = 1L;
        String name = "브라운";
        ReservationTime time = new ReservationTime(timeId, startAt);
        Theme theme = new Theme(themeId, "테스트 테마", "테마 설명", "썸네일 주소");
        Reservation reservation = new Reservation(id, name, date, time, theme);

        when(reservationTimeRepository.findBy(timeId))
                .thenReturn(Optional.of(time));
        when(reservationRepository.existsWith(date, timeId, themeId))
                .thenReturn(false);
        when(themeRepository.findBy(themeId))
                .thenReturn(Optional.of(theme));
        when(reservationRepository.insert(any(Reservation.class)))
                .thenReturn(id);
        when(reservationRepository.findById(id))
                .thenReturn(Optional.of(reservation));

        // when
        Reservation result = service.create(name, date, timeId, themeId);

        // then
        assertAll(
                () -> assertThat(result.getId()).isEqualTo(id),
                () -> assertThat(result.getName()).isEqualTo(name),
                () -> assertThat(result.getDate()).isEqualTo(date),
                () -> assertThat(result.getTime()).isEqualTo(time),
                () -> assertThat(result.getTheme()).isEqualTo(theme));

        verify(reservationTimeRepository).findBy(timeId);
        verify(reservationRepository).existsWith(date, timeId, themeId);
        verify(themeRepository).findBy(themeId);
        verify(reservationRepository).insert(any(Reservation.class));
        verify(reservationRepository).findById(id);
    }

    private static Stream<Arguments> adminReservationDateTimes() {
        return Stream.of(
                Arguments.of(LocalDate.now().minusDays(1), LocalTime.parse("08:00")),
                Arguments.of(LocalDate.now(), LocalTime.MIDNIGHT),
                Arguments.of(LocalDate.now().plusDays(1), LocalTime.parse("08:00"))
        );
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
