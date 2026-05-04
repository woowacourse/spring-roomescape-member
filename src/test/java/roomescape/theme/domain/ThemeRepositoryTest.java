package roomescape.theme.domain;


import java.time.LocalTime;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;

@SpringBootTest
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
class ThemeRepositoryTest {

    @Autowired
    private ThemeRepository themeRepository;

    @Test
    @DisplayName("관리자가 테마를 추가하면 정상적으로 저장된다.")
    void normalTest() {
        Theme theme = Theme.builder()
                .name("포비")
                .description("포비가 나와요")
                .durationTime(LocalTime.of(1, 0))
                .thumbnailImageUrl("http://~~~")
                .build();

        Theme savedTheme = themeRepository.save(theme);

        Assertions.assertThat(savedTheme.getId()).isNotNull();
    }

    @Test
    @DisplayName("관리자가 테마를 삭제하면 정상적으로 삭제된다.")
    void deleteTest() {
        Theme theme = Theme.builder()
                .name("포비")
                .description("포비가 나와요")
                .durationTime(LocalTime.of(1, 0))
                .thumbnailImageUrl("http://~~~")
                .build();

        Theme savedTheme = themeRepository.save(theme);

        Assertions.assertThatCode(() -> themeRepository.delete(savedTheme.getId()))
                .doesNotThrowAnyException();
    }
}
