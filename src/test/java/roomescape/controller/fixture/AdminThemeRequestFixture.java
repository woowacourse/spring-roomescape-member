package roomescape.controller.fixture;

import java.util.stream.Stream;
import org.junit.jupiter.params.provider.Arguments;
import roomescape.web.dto.theme.ThemeRequest;

public final class AdminThemeRequestFixture {

    private AdminThemeRequestFixture() {
    }

    public static Stream<Arguments> themeFailRequestFixture() {
        return Stream.of(
                Arguments.of(
                        new ThemeRequest(null, "설명", "http://image.png"),
                        "이름은 필수 값입니다."
                ),
                Arguments.of(
                        new ThemeRequest("", "설명", "http://image.png"),
                        "이름은 필수 값입니다."
                ),
                Arguments.of(
                        new ThemeRequest("테마", null, "http://image.png"),
                        "설명은 필수 값입니다."
                ),
                Arguments.of(
                        new ThemeRequest("테마", " ", "http://image.png"),
                        "설명은 필수 값입니다."
                ),
                Arguments.of(
                        new ThemeRequest("테마", "설명", null),
                        "썸네일 이미지는 필수 값입니다."
                ),
                Arguments.of(
                        new ThemeRequest("테마", "설명", ""),
                        "썸네일 이미지는 필수 값입니다."
                )
        );
    }
}
