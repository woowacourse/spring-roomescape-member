package roomescape.time.service;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import roomescape.reservation.domain.Reservation;
import roomescape.time.domain.Time;

//@ExtendWith(MockitoExtension.class)
//class TimeServiceTest {
//
//    @Mock
//    private Repository<Time> reservationTimeRepository;
//
//    @Mock
//    private Repository<Reservation> reservationRepository;
//
//    private final TimeService timeService = new TimeService(reservationTimeRepository, reservationRepository);
//
////    @InjectMocks
////    private ReservationTimeService reservationTimeService;
//
//    @DisplayName("예약이 존재할 때 시간 id를 삭제할 시 예외가 발생한다")
//    @Test
//    void test1() {
//        Long id = 1L;
//        LocalTime time = LocalTime.of(10, 0);
//
//        when(reservationTimeRepository.findById(id)).thenReturn(Optional.of(new Time(id, time)));
//        when(reservationRepository.findAll()).thenReturn(List.of(new Reservation(1L, "riwon", LocalDate.now().plusDays(1), new Time(id, time))));
//
//        Assertions.assertThatThrownBy(() -> timeService.deleteById(id))
//                .isInstanceOf(IllegalArgumentException.class)
//                .hasMessage("[ERROR] 이미 예약된 시간은 삭제할 수 없습니다");
//
//        verify(reservationTimeRepository, times(1)).findById(id);
//    }
//}
