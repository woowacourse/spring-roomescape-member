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
import roomescape.repository.RoomescapeThemeRepository;

@ExtendWith(MockitoExtension.class)
public class ReservationThemeServiceTest {

    @Mock
    RoomescapeRepository roomescapeRepository;
    @Mock
    RoomescapeThemeRepository themeRepository;
    @InjectMocks
    ReservationThemeService themeService;

    @DisplayName("예약테마와 연결된 예약이 존재하는 경우 예약테마를 삭제할 수 없다.")
    @Test
    void removeReservationThemeWithExistsReservation() {
        // given
        long existId = 1L;
        when(roomescapeRepository.existsByThemeId(existId)).thenReturn(true);

        // when & then
        assertThatThrownBy(() -> themeService.removeReservationTheme(existId))
                .isInstanceOf(DeletionNotAllowedException.class)
                .hasMessage("[ERROR] 예약이 연결된 테마는 삭제할 수 없습니다. 관련 예약을 먼저 삭제해주세요.");
    }

    @DisplayName("존재하지 않는 예약테마를 삭제하려는 경우 예외를 던진다")
    @Test
    void removeReservationTheme() {
        //given
        long notExistId = 999;
        when(themeRepository.deleteById(notExistId)).thenReturn(false);

        //when & then
        assertThatThrownBy(() -> themeService.removeReservationTheme(notExistId))
                .isInstanceOf(DataNotFoundException.class)
                .hasMessage("[ERROR] 예약 테마 999번에 해당하는 테마가 없습니다.");
    }

}
