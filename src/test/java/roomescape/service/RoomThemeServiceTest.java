package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.domain.roomtheme.RoomTheme;
import roomescape.exception.custom.ExistedDuplicateValueException;
import roomescape.exception.custom.InUseException;
import roomescape.exception.custom.NotExistedValueException;
import roomescape.service.dto.CreateRoomThemeServiceRequest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class RoomThemeServiceTest {

    @Autowired
    RoomThemeService roomThemeService;

    @Test
    @DisplayName("테마를 추가한다")
    void addThemeTest() {
        // given
        final CreateRoomThemeServiceRequest creation = new CreateRoomThemeServiceRequest("test", "description",
                "thumbnail");
        // when // then
        assertThatCode(() -> roomThemeService.addTheme(creation))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("같은 테마가 존재하면 예외를 던진다")
    void addThemeTest_WhenThemeAlreadyExists() {
        // given
        final CreateRoomThemeServiceRequest creation = new CreateRoomThemeServiceRequest("test", "description",
                "thumbnail");
        roomThemeService.addTheme(creation);

        // when // then
        assertThatThrownBy(() -> roomThemeService.addTheme(creation))
                .isInstanceOf(ExistedDuplicateValueException.class)
                .hasMessageContaining("이미 존재하는 테마입니다");
    }

    @Test
    @DisplayName("존재하는 모든 테마를 조회한다")
    void findAllThemesTest() {
        // given // when
        final List<RoomTheme> allThemes = roomThemeService.findAllThemes();

        // then
        assertThat(allThemes).hasSize(3);
    }

    @Test
    @DisplayName("테마를 삭제한다")
    void deleteThemeTest() {
        // given
        roomThemeService.addTheme(new CreateRoomThemeServiceRequest("test", "description", "thumbnail"));
        final long deleteId = 3L;

        // when // then
        assertThatCode(() -> roomThemeService.deleteTheme(deleteId))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("사용중인 테마를 삭제하는 경우 예외를 던진다")
    void deleteThemeTest_WhenThemeIsUsedInReservation() {
        // given
        final long deleteId = 1L;

        // when // then
        assertThatThrownBy(() -> roomThemeService.deleteTheme(deleteId))
                .isInstanceOf(InUseException.class)
                .hasMessageContaining("사용 중인 테마입니다");
    }

    @Test
    @DisplayName("존재하지 않는 테마를 삭제하는 경우 예외를 던진다")
    void deleteThemeTest_WhenThemeDoesNotExist() {
        // given
        final long deleteId = 1000L;

        // when // then
        assertThatThrownBy(() -> roomThemeService.deleteTheme(deleteId))
                .isInstanceOf(NotExistedValueException.class)
                .hasMessageContaining("존재하지 않는 테마입니다");
    }
}
