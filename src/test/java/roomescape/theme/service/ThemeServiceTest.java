package roomescape.theme.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import roomescape.exception.badRequest.BadRequestException;
import roomescape.exception.conflict.ThemeNameConflictException;
import roomescape.exception.notFound.ThemeNotFoundException;
import roomescape.theme.entity.Theme;
import roomescape.theme.repository.FakeThemeRepository;
import roomescape.theme.repository.ThemeRepository;
import roomescape.theme.service.dto.request.ThemeRequest;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ThemeServiceTest {

    private final ThemeRepository themeRepository = new FakeThemeRepository();
    private final ThemeService service = new ThemeService(themeRepository);

    @DisplayName("중복되는 테마 이름이 있을 경우 생성할 수 없다.")
    @Test
    void duplicateByName() {
        // given
        String name = "밍곰 테마";
        themeRepository.save(new Theme(
                1L,
                name,
                "진격의 밍곰",
                "진격의 밍곰 썸네일"
        ));

        ThemeRequest request = new ThemeRequest(
                name,
                "우테코 레벨2 탈출",
                "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"
        );

        // when & then
        assertThatThrownBy(() -> service.createTheme(request))
                .isInstanceOf(ThemeNameConflictException.class);
    }

    @DisplayName("id가 존재하지 않을 경우 삭제할 수 없다.")
    @Test
    void cannotDeleteNotExist() {
        // given

        // when & then
        assertThatThrownBy(() -> service.deleteTheme(1L))
                .isInstanceOf(ThemeNotFoundException.class);
    }

    @DisplayName("인기 테마 조회는 1개 이상 100개 이하까지만 가능하며 그 이외의 범위가 주어지면 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(ints = {0, 101})
    void invalidLimit(final int limit) {
        // given

        // when & then
        assertThatThrownBy(() -> {
            service.getPopularThemes(limit);
        }).isInstanceOf(BadRequestException.class);
    }
}
