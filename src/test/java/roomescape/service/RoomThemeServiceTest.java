package roomescape.service;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import roomescape.exception.custom.BusinessRuleViolationException;
import roomescape.exception.custom.ExistedDuplicateValueException;
import roomescape.exception.custom.NotFoundValueException;
import roomescape.service.dto.request.RoomThemeCreation;
import roomescape.service.dto.response.RoomThemeResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
@Sql(scripts = "/schema.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/reservation-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class RoomThemeServiceTest {

    @Autowired
    RoomThemeService roomThemeService;

    @Test
    @DisplayName("테마를 추가한다")
    void addTheme() {
        //given
        RoomThemeCreation creation = new RoomThemeCreation("addTheme", "description", "thumbnail");
        //when //then
        assertThatCode(() -> roomThemeService.addTheme(creation))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("같은 테마가 존재하면 예외를 던진다")
    void throwExceptionWhenExistSameTheme() {
        //given
        RoomThemeCreation creation = new RoomThemeCreation("duplicate", "description", "thumbnail");
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
        List<RoomThemeResult> allThemes = roomThemeService.findAllThemes();

        //then
        assertThat(allThemes).hasSize(2);
    }

    @Test
    @DisplayName("테마를 삭제한다")
    void deleteTheme() {
        //given
        RoomThemeResult theme = roomThemeService.addTheme(new RoomThemeCreation("delete", "description", "thumbnail"));
        long deleteId = theme.id();

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
