//package roomescape.reservation.service;
//
//import static org.assertj.core.api.Assertions.assertThatThrownBy;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.when;
//
//import java.time.LocalDate;
//import java.time.LocalTime;
//import java.util.Optional;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import roomescape.exception.ConflictException;
//import roomescape.exception.NotFoundException;
//import roomescape.reservation.dao.ReservationDao;
//import roomescape.reservation.domain.Reservation;
//import roomescape.reservation.dto.ReservationCreateRequest;
//import roomescape.reservationtime.domain.ReservationTime;
//import roomescape.reservationtime.service.ReservationTimeService;
//import roomescape.theme.domain.Theme;
//import roomescape.theme.service.ThemeService;
//
//@ExtendWith(MockitoExtension.class)
//class ReservationServiceTest {
//
//    @Mock
//    ReservationDao reservationDao;
//
//    @Mock
//    ReservationTimeService reservationTimeService;
//
//    @Mock
//    ThemeService themeService;
//
//    @InjectMocks
//    ReservationService reservationService;
//
//    @DisplayName("날짜와 시간이 중복되는 예약을 생성한다면 예외가 발생한다.")
//    @Test
//    void createThrowExceptionIfAlreadyExistDateAndTimeTest() {
//
//        // given
//        ReservationTime time = ReservationTime.load(1L, LocalTime.now().plusHours(1));
//        Theme theme = Theme.load(1L, "test", "테마1", "설명1");
//        ReservationCreateRequest request = new ReservationCreateRequest("체체", LocalDate.now(), 1L, 1L);
//
//        when(reservationTimeService.findById(1L)).thenReturn(time);
//        when(themeService.findById(1L)).thenReturn(theme);
//        when(reservationDao.findByThemeAndDateAndTime(any(Reservation.class))).thenReturn(Optional.of(Reservation.load(
//                1L, "체체", LocalDate.now(), time, theme)));
//
//        // when & then
//        assertThatThrownBy(() -> reservationService.create(request))
//                .isInstanceOf(ConflictException.class)
//                .hasMessage("이미 존재하는 예약입니다.");
//    }
//
//    @DisplayName("존재하지 않는 예약을 삭제 시 예외가 발생한다.")
//    @Test
//    void deleteTest_NoSuchElementException() {
//
//        // given
//        long id = 1L;
//        when(reservationDao.existById(id)).thenReturn(false);
//
//        // when & then
//        assertThatThrownBy(() -> reservationService.delete(id))
//                .isInstanceOf(NotFoundException.class)
//                .hasMessage("예약이 존재하지 않습니다.");
//    }
//}
