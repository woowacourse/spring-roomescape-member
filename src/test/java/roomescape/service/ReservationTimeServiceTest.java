package roomescape.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import roomescape.exception.exception.DataNotFoundException;
import roomescape.exception.exception.DeletionNotAllowedException;
import roomescape.repository.RoomescapeRepository;
import roomescape.repository.RoomescapeTimeRepository;

@ExtendWith(MockitoExtension.class)
public class ReservationTimeServiceTest {

    @Mock
    RoomescapeRepository roomescapeRepository;
    @Mock
    RoomescapeTimeRepository roomescapeTimeRepository;
    @InjectMocks
    ReservationTimeService timeService;

    @DisplayName("예약시간과 연결된 예약이 존재하는 경우 예약시간을 삭제할 수 없다.")
    @Test
    void removeReservationTimeWithExistsReservation() {
        // given
        long existId = 1L;
        when(roomescapeRepository.existsByTimeId(existId)).thenReturn(true);

        // when & then
        assertThatThrownBy(() -> timeService.removeReservationTime(existId))
                .isInstanceOf(DeletionNotAllowedException.class)
                .hasMessage("[ERROR] 예약이 연결된 시간은 삭제할 수 없습니다. 관련 예약을 먼저 삭제해주세요.");
    }

    @DisplayName("존재하지 않는 예약시간을 삭제하려는 경우 예외를 던진다")
    @Test
    void removeReservationTime() {
        //given
        long notExistId = 999;
        when(roomescapeTimeRepository.deleteById(notExistId)).thenReturn(false);

        //when & then
        assertThatThrownBy(() -> timeService.removeReservationTime(notExistId))
                .isInstanceOf(DataNotFoundException.class)
                .hasMessage("[ERROR] 예약 시간 999번에 해당하는 시간이 없습니다.");
    }

}
