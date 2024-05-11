package roomescape.dto.request;

import static org.assertj.core.api.Assertions.assertThat;
import static roomescape.util.Fixture.THEME_DESCRIPTION;
import static roomescape.util.Fixture.THEME_NAME;
import static roomescape.util.Fixture.THEME_THUMBNAIL;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.Set;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class ThemeRequestDtoTest {
    private static ValidatorFactory factory;
    private static Validator validator;

    @BeforeAll
    public static void init() {
        factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @ParameterizedTest
    @DisplayName("name이 null이나 빈 값이면 검증에 실패한다")
    @NullAndEmptySource
    @ValueSource(strings = {" ", "\n"})
    void notEmptyViolationWithBlankName(String emptyName) {
        ThemeRequestDto themeRequest = new ThemeRequestDto(emptyName, THEME_DESCRIPTION, THEME_THUMBNAIL);
        Set<ConstraintViolation<ThemeRequestDto>> violations = validator.validate(themeRequest);
        assertThat(violations).hasSize(1)
                .extracting(ConstraintViolation::getMessage)
                .first()
                .isEqualTo("테마 이름은 null이나 빈 값일 수 없습니다.");
    }

    @ParameterizedTest
    @DisplayName("description이 null이나 빈 값이면 검증에 실패한다")
    @NullAndEmptySource
    @ValueSource(strings = {" ", "\n"})
    void notEmptyViolationWithBlankDescription(String emptyDescription) {
        ThemeRequestDto themeRequest = new ThemeRequestDto(THEME_NAME, emptyDescription, THEME_THUMBNAIL);
        Set<ConstraintViolation<ThemeRequestDto>> violations = validator.validate(themeRequest);
        assertThat(violations).hasSize(1)
                .extracting(ConstraintViolation::getMessage)
                .first()
                .isEqualTo("테마 설명은 null이나 빈 값일 수 없습니다.");
    }

    @ParameterizedTest
    @DisplayName("thumbnail이 null이면 검증에 실패한다")
    @NullAndEmptySource
    @ValueSource(strings = {" ", "\n"})
    void notEmptyViolationWithEmptyThumbnail(String emptyThumbnail) {
        ThemeRequestDto themeRequest = new ThemeRequestDto(THEME_NAME, THEME_DESCRIPTION, emptyThumbnail);
        Set<ConstraintViolation<ThemeRequestDto>> violations = validator.validate(themeRequest);
        assertThat(violations).hasSize(1)
                .extracting(ConstraintViolation::getMessage)
                .first()
                .isEqualTo("테마 이미지는 null이나 빈 값일 수 없습니다.");
    }
}
