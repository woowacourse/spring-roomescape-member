package roomescape.service;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import roomescape.domain.RoomTheme;
import roomescape.exception.custom.BusinessRuleViolationException;
import roomescape.exception.custom.ExistedDuplicateValueException;
import roomescape.exception.custom.NotFoundValueException;
import roomescape.repository.ReservationRepository;
import roomescape.repository.RoomThemeRepository;
import roomescape.service.dto.request.RoomThemeCreation;
import roomescape.service.dto.response.RoomThemeResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RoomThemeServiceTest {

    @Mock
    ReservationRepository reservationRepository;

    @Mock
    RoomThemeRepository roomThemeRepository;

    @InjectMocks
    RoomThemeService roomThemeService;

    @Test
    @DisplayName("테마를 추가한다")
    void addTheme() {
        //given
        RoomThemeCreation creation = new RoomThemeCreation("addTheme", "description", "thumbnail");
        when(roomThemeRepository.existsByName(any(String.class)))
                .thenReturn(false);
        when(roomThemeRepository.insert(any(RoomTheme.class)))
                .thenReturn(1L);
        when(roomThemeRepository.findById(any(Long.class)))
                .thenReturn(Optional.of(new RoomTheme(1L, "addTheme", "description", "thumbnail")));

        //when //then
        assertThatCode(() -> roomThemeService.addTheme(creation))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("같은 테마가 존재하면 예외를 던진다")
    void throwExceptionWhenExistSameTheme() {
        //given
        when(roomThemeRepository.existsByName(any(String.class)))
                .thenReturn(true);

        RoomThemeCreation creation = new RoomThemeCreation("duplicate", "description", "thumbnail");

        //when //then
        assertThatThrownBy(() -> roomThemeService.addTheme(creation))
                .isInstanceOf(ExistedDuplicateValueException.class)
                .hasMessageContaining("이미 존재하는 테마입니다");
    }

    @Test
    @DisplayName("존재하는 모든 테마를 조회한다")
    void findAllThemes() {
        //given
        RoomTheme roomTheme = new RoomTheme(1, "test", "test", "test");
        when(roomThemeRepository.findAll())
                .thenReturn(List.of(roomTheme));

        //when
        List<RoomThemeResult> allThemes = roomThemeService.findAllThemes();

        //then
        assertThat(allThemes).hasSize(1);
    }

    @Test
    @DisplayName("테마를 삭제한다")
    void deleteTheme() {
        //given
        when(roomThemeRepository.deleteById(any(Long.class)))
                .thenReturn(true);

        //when //then
        assertThatCode(() -> roomThemeService.deleteTheme(1L))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("사용중인 테마를 삭제하는 경우 예외를 던진다")
    void throwExceptionWhenDeleteUsingTheme() {
        //given
        when(reservationRepository.existsByThemeId(any(Long.class)))
                .thenReturn(true);

        long deleteId = 1L;

        //when //then
        assertThatThrownBy(() -> roomThemeService.deleteTheme(deleteId))
                .isInstanceOf(BusinessRuleViolationException.class)
                .hasMessageContaining("사용 중인 테마입니다");
    }

    @Test
    @DisplayName("존재하지 않는 테마를 삭제하는 경우 예외를 던진다")
    void throwExceptionWhenNotExistTheme() {
        //given
        when(roomThemeRepository.deleteById(any(Long.class)))
                .thenReturn(false);
        long deleteId = 1000L;

        //when //then
        assertThatThrownBy(() -> roomThemeService.deleteTheme(deleteId))
                .isInstanceOf(NotFoundValueException.class)
                .hasMessageContaining("존재하지 않는 테마입니다");
    }
}
