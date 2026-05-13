package roomescape.dto.theme;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class AddThemeRequestTest {
    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("모든 필드가 입력되면 검증 통과")
    void success() {
        AddThemeRequest request = new AddThemeRequest("공포의 테마", "정말 무서워요", "https://image.com/1.png");
        Set<ConstraintViolation<AddThemeRequest>> violations = validator.validate(request);

        assertThat(violations).isEmpty();
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "   "})
    @DisplayName("이름이 비어있거나 공백이면 검증 실패")
    void invalidName(String name) {
        AddThemeRequest request = new AddThemeRequest(name, "설명", "이미지");
        Set<ConstraintViolation<AddThemeRequest>> violations = validator.validate(request);

        assertThat(violations).isNotEmpty();
        assertThat(violations).extracting("message")
                .contains("테마 이름은 반드시 포함되어야 합니다.");
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "   "})
    @DisplayName("설명이 비어있거나 공백이면 검증 실패")
    void invalidDescription(String description) {
        AddThemeRequest request = new AddThemeRequest("이름", description, "이미지");
        Set<ConstraintViolation<AddThemeRequest>> violations = validator.validate(request);

        assertThat(violations).isNotEmpty();
        assertThat(violations).extracting("message")
                .contains("테마 설명은 반드시 포함되어야 합니다.");
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "   "})
    @DisplayName("이미지 URL이 비어있거나 공백이면 검증 실패")
    void invalidImageUrl(String imageUrl) {
        AddThemeRequest request = new AddThemeRequest("이름", "설명", imageUrl);
        Set<ConstraintViolation<AddThemeRequest>> violations = validator.validate(request);

        assertThat(violations).isNotEmpty();
        assertThat(violations).extracting("message")
                .contains("썸네일 이미지는 반드시 포함되어야 합니다.");
    }
}