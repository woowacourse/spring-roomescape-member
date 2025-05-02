package roomescape.reservation.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.reservation.repository.FakeThemeRepository;
import roomescape.reservation.repository.ThemeRepository;
import roomescape.reservation.dto.request.ThemeRequest;
import roomescape.reservation.entity.Theme;
import roomescape.global.error.exception.ConflictException;
import roomescape.global.error.exception.NotFoundException;

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
                .isInstanceOf(ConflictException.class);
    }

    @DisplayName("id가 존재하지 않을 경우 삭제할 수 없다.")
    @Test
    void cannotDeleteNotExist() {
        // given

        // when & then
        assertThatThrownBy(() -> service.deleteTheme(1L))
                .isInstanceOf(NotFoundException.class);
    }
}
