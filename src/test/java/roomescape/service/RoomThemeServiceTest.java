package roomescape.service;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.controller.dto.response.RoomThemeResponse;
import roomescape.exception.custom.BusinessRuleViolationException;
import roomescape.exception.custom.ExistedDuplicateValueException;
import roomescape.exception.custom.NotFoundValueException;
import roomescape.service.dto.RoomThemeCreation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class RoomThemeServiceTest {

    @Autowired
    RoomThemeService roomThemeService;

    @Test
    @DisplayName("테마를 추가한다")
    void addTheme() {
        //given
        RoomThemeCreation creation = new RoomThemeCreation("test", "description", "thumbnail");
        //when //then
        assertThatCode(() -> roomThemeService.addTheme(creation))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("같은 테마가 존재하면 예외를 던진다")
    void throwExceptionWhenExistSameTheme() {
        //given
        RoomThemeCreation creation = new RoomThemeCreation("test", "description", "thumbnail");
        roomThemeService.addTheme(creation);

        //when //then
        assertThatThrownBy(() -> roomThemeService.addTheme(creation))
                .isInstanceOf(ExistedDuplicateValueException.class)
                .hasMessageContaining("이미 존재하는 테마입니다");
    }

    @Test
    @DisplayName("존재하는 모든 테마를 조회한다")
    void findAllThemes() {
        //given //when
        List<RoomThemeResponse> allThemes = roomThemeService.findAllThemes();

        //then
        assertThat(allThemes).hasSize(1);
    }

    @Test
    @DisplayName("테마를 삭제한다")
    void deleteTheme() {
        //given
        roomThemeService.addTheme(new RoomThemeCreation("test", "description", "thumbnail"));
        long deleteId = 2L;

        //when //then
        assertThatCode(() -> roomThemeService.deleteTheme(deleteId))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("사용중인 테마를 삭제하는 경우 예외를 던진다")
    void throwExceptionWhenDeleteUsingTheme() {
        //given
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
        long deleteId = 1000L;

        //when //then
        assertThatThrownBy(() -> roomThemeService.deleteTheme(deleteId))
                .isInstanceOf(NotFoundValueException.class)
                .hasMessageContaining("존재하지 않는 테마입니다");
    }
}
