package roomescape.service.reservation;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import roomescape.dto.reservation.ReservationRequest;
import roomescape.entity.ReservationTimeEntity;
import roomescape.entity.ThemeEntity;
import roomescape.exception.reservation.ReservationAlreadyExistsException;
import roomescape.exception.reservation.ReservationNotFoundException;
import roomescape.exception.reservationtime.ReservationTimeNotFoundException;
import roomescape.exception.theme.ThemeNotFoundException;
import roomescape.repository.reservation.ReservationRepository;
import roomescape.repository.reservationtime.ReservationTimeRepository;
import roomescape.repository.theme.ThemeRepository;

@ExtendWith(MockitoExtension.class)
class ReservationServiceImplTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private ReservationTimeRepository timeRepository;

    @Mock
    private ThemeRepository themeRepository;

    @InjectMocks
    private ReservationServiceImpl reservationService;

    @DisplayName("예약 시간이 존재하지 않으면 예약을 생성할 수 없다")
    @Test
    void timeNotFound() {
        // given
        LocalDate today = LocalDate.now();
        long timeId = 1L;
        long themeId = 1L;
        String name = "에드";
        ReservationRequest request = new ReservationRequest(name, today, timeId, themeId);

        // when
        when(timeRepository.findById(timeId)).thenReturn(Optional.empty());

        //then
        assertThatThrownBy(() -> reservationService.create(request))
                .isInstanceOf(ReservationTimeNotFoundException.class);
    }

    @DisplayName("테마가 존재하지 않으면 예약을 생성할 수 없다")
    @Test
    void themeNotFound() {
        // given
        LocalDate today = LocalDate.now();
        long timeId = 1L;
        long themeId = 1L;
        String name = "에드";
        ReservationRequest request = new ReservationRequest(name, today, timeId, themeId);

        // when
        when(timeRepository.findById(timeId)).thenReturn(
                Optional.of(new ReservationTimeEntity(timeId, LocalTime.now().plusHours(1))));

        when(themeRepository.findById(themeId)).thenReturn(Optional.empty());
        //then
        assertThatThrownBy(() -> reservationService.create(request))
                .isInstanceOf(ThemeNotFoundException.class);
    }

    @DisplayName("이미 예약이 존재하는 날짜, 시간에는 예약을 생성할 수 없다")
    @Test
    void alreadyExists() {
        // given
        LocalDate today = LocalDate.now();
        long timeId = 1L;
        long themeId = 1L;
        String name = "에드";
        ReservationRequest request = new ReservationRequest(name, today, timeId, themeId);

        // when
        when(timeRepository.findById(timeId)).thenReturn(
                Optional.of(new ReservationTimeEntity(timeId, LocalTime.now().plusHours(1))));

        when(themeRepository.findById(themeId)).thenReturn(
                Optional.of(new ThemeEntity(themeId, "test", "test", "test")));
        when(reservationRepository.existsByDateAndTime(today, timeId)).thenReturn(true);

        //then
        assertThatThrownBy(() -> reservationService.create(request))
                .isInstanceOf(ReservationAlreadyExistsException.class);
    }

    @DisplayName("존재하지 않는 예약을 삭제 시도하면 예외를 발생시킨다")
    @Test
    void delete() {
        // given
        long id = 99L;

        // when
        when(reservationRepository.deleteById(id)).thenReturn(0);

        // then
        assertThatThrownBy(() -> reservationService.deleteById(id))
                .isInstanceOf(ReservationNotFoundException.class);
    }
}
