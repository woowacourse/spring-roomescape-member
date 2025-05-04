package roomescape.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import roomescape.common.exception.InvalidRequestException;
import roomescape.dto.request.ThemeCreateRequest;

import java.util.function.Supplier;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ThemeCreateRequestValidationTest {
    @Test
    @DisplayName("ThemeCreateRequest 생성 테스트")
    void generateThemeCreateRequest() {
        assertThatCode(() -> new ThemeCreateRequest("히스타", "설명", "img.jpg")).doesNotThrowAnyException();
    }


    @ParameterizedTest
    @MethodSource("getInvalidThemeCreateRequestName")
    @DisplayName("ThemeCreateRequest 필수 값 검증 테스트")
    void invalidThemeCreateRequestName(Supplier<ThemeCreateRequest> supplier) {
        assertThatThrownBy(supplier::get).isInstanceOf(InvalidRequestException.class);
    }

    static Stream<Arguments> getInvalidThemeCreateRequestName() {
        return Stream.of(
                Arguments.arguments((Supplier<ThemeCreateRequest>) () -> new ThemeCreateRequest(null, "설명", "img.jpg")),
                Arguments.arguments((Supplier<ThemeCreateRequest>) () -> new ThemeCreateRequest("", "설명", "img.jpg"))
        );
    }
}
