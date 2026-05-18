package roomescape.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.exception.DomainException;

class ThemeTest {

    @Test
    @DisplayName("테마 이름이 공백이면 생성에 실패한다.")
    void failCreate_WhenNameIsBlank() {
        assertThatThrownBy(() -> new Theme(null, "", "설명", "https://example.com/image.jpg"))
                .isInstanceOf(DomainException.class);
    }

    @Test
    @DisplayName("테마 이름이 20자를 초과하면 생성에 실패한다.")
    void failCreate_WhenNameIsLongerThan20() {
        assertThatThrownBy(() -> new Theme(null, "a".repeat(21), "설명", "https://example.com/image.jpg"))
                .isInstanceOf(DomainException.class);
    }

    @Test
    @DisplayName("테마 설명이 공백이면 생성에 실패한다.")
    void failCreate_WhenDescriptionIsBlank() {
        assertThatThrownBy(() -> new Theme(null, "테마", "", "https://example.com/image.jpg"))
                .isInstanceOf(DomainException.class);
    }

    @Test
    @DisplayName("테마 설명이 1000자를 초과하면 생성에 실패한다.")
    void failCreate_WhenDescriptionIsLongerThan1000() {
        assertThatThrownBy(() -> new Theme(null, "테마", "a".repeat(1001), "https://example.com/image.jpg"))
                .isInstanceOf(DomainException.class);
    }

    @Test
    @DisplayName("썸네일 URL이 공백이면 생성에 실패한다.")
    void failCreate_WhenThumbnailUrlIsBlank() {
        assertThatThrownBy(() -> new Theme(null, "테마", "설명", ""))
                .isInstanceOf(DomainException.class);
    }

    @Test
    @DisplayName("썸네일 URL 형식이 올바르지 않으면 생성에 실패한다.")
    void failCreate_WhenThumbnailUrlIsInvalid() {
        assertThatThrownBy(() -> new Theme(null, "테마", "설명", "example.com/image.jpg"))
                .isInstanceOf(DomainException.class);
    }
}
