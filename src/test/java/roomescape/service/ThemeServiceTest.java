package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.model.Theme;
import roomescape.repository.ReservationFakeRepository;
import roomescape.repository.ThemeFakeRepository;

class ThemeServiceTest {

    private ThemeService service;

    @BeforeEach
    void setUp() {
        service = new ThemeService(new ReservationFakeRepository(), new ThemeFakeRepository());
    }

    @Test
    @DisplayName("테마를 추가할 수 있다.")
    void add() {
        // given
        var name = "레벨2 탈출";
        var description = "우테코 레벨2를 탈출하는 내용입니다.";
        var thumbnail = "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg";

        // when
        Theme created = service.add(name, description, thumbnail);

        // then
        var themes = service.allThemes();
        assertThat(themes).contains(created);
    }

    @Test
    @DisplayName("테마를 삭제할 수 있다.")
    void removeById() {
        // given
        var name = "레벨2 탈출";
        var description = "우테코 레벨2를 탈출하는 내용입니다.";
        var thumbnail = "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg";
        Theme created = service.add(name, description, thumbnail);

        // when
        boolean isRemoved = service.removeById(created.id());

        // then
        var themes = service.allThemes();
        assertAll(
            () -> assertThat(isRemoved).isTrue(),
            () -> assertThat(themes).doesNotContain(created)
        );
    }
}
