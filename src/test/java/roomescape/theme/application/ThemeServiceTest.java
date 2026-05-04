package roomescape.theme.application;

import java.time.LocalTime;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import roomescape.theme.domain.exception.ThemeNotFoundException;
import roomescape.theme.presentation.dto.ThemeRequest;
import roomescape.theme.presentation.dto.ThemeResponse;

@Transactional
@SpringBootTest
class ThemeServiceTest {

    @Autowired
    private ThemeService service;

    @Test
    @DisplayName("존재하지 않는 테마를 삭제하려고 하면 에러를 반환한다.")
    void wrongTest() {
        ThemeResponse response = service.addTheme(ThemeRequest.builder()
                .name("포비")
                .description("포비가 나와요")
                .thumbnailImageUrl("https://~~~~")
                .durationTime(LocalTime.of(1, 0))
                .build());
        Assertions.assertThatThrownBy(() -> service.deleteTheme(-1L))
                .isInstanceOf(ThemeNotFoundException.class);
    }

    @Test
    @DisplayName("존재하는 테마를 삭제하려고 하면 정상 동작한다.")
    void normalTest() {
        ThemeResponse response = service.addTheme(ThemeRequest.builder()
                .name("포비")
                .description("포비가 나와요")
                .thumbnailImageUrl("https://~~~~")
                .durationTime(LocalTime.of(1, 0))
                .build());
        Assertions.assertThatCode(() -> service.deleteTheme(response.id()))
                .doesNotThrowAnyException();
    }
}
