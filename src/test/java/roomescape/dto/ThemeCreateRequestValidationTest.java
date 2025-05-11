package roomescape.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import roomescape.dto.request.ThemeCreateRequest;

import java.util.Set;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class ThemeCreateRequestValidationTest {

    private static ValidatorFactory factory;
    private static Validator validator;

    @BeforeAll
    public static void init() {
        factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("ThemeCreateRequest 생성 테스트")
    void generateThemeCreateRequest() {
        ThemeCreateRequest request = new ThemeCreateRequest("히스타", "설명", "img.jpg");
        Set<ConstraintViolation<ThemeCreateRequest>> violations = validator.validate(request);
        assertThat(violations.size()).isEqualTo(0);
    }


    @ParameterizedTest
    @MethodSource("getEmptyNameThemeCreateRequests")
    @DisplayName("ThemeCreateRequest Name 빈 값인 경우 예외 처리")
    void invalidThemeCreateRequestName(ThemeCreateRequest request) {
        Set<ConstraintViolation<ThemeCreateRequest>> violations = validator.validate(request);
        for (ConstraintViolation<ThemeCreateRequest> violation : violations) {
            assertThat(violation.getMessage()).isEqualTo("이름은 빈 값이 올 수 없습니다");
        }
    }

    static Stream<Arguments> getEmptyNameThemeCreateRequests() {
        return Stream.of(
                Arguments.arguments(new ThemeCreateRequest(null, "테마 설명", "테마 이미지")),
                Arguments.arguments(new ThemeCreateRequest("", "테마 설명", "테마 이미지")),
                Arguments.arguments(new ThemeCreateRequest(" ", "테마 설명", "테마 이미지"))
        );
    }
}
