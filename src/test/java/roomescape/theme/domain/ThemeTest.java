package roomescape.theme.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ThemeTest {
    private final String name = "공포";
    private final String description = "테마 설명";
    private final String defaultThumbnailUrl = "DEFAULT_THUMBNAIL_URL";

    @Test
    @DisplayName("테마 이름이 null이면 예외가 발생한다.")
    void create_null_name() {
        // given
        String nullName = null;

        // when & then
        assertThatThrownBy(() -> Theme.create(nullName, description, defaultThumbnailUrl))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("테마 이름은 필수입니다.");
    }

    @Test
    @DisplayName("테마 이름이 비어있으면 예외가 발생한다.")
    void create_empty_name() {
        // given
        String emptyName = "";

        // when & then
        assertThatThrownBy(() -> Theme.create(emptyName, description, defaultThumbnailUrl))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("테마 이름은 필수입니다.");
    }

    @Test
    @DisplayName("테마 설명이 null이면 예외가 발생한다.")
    void create_null_description() {
        // given
        String nullDescription = null;

        // when & then
        assertThatThrownBy(() -> Theme.create(name, nullDescription, defaultThumbnailUrl))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("테마 설명은 필수입니다.");
    }

    @Test
    @DisplayName("테마 설명이 비어있으면 예외가 발생한다.")
    void create_empty_description() {
        // given
        String emptyDescription = "";

        // when & then
        assertThatThrownBy(() -> Theme.create(name, emptyDescription, defaultThumbnailUrl))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("테마 설명은 필수입니다.");
    }

    @Test
    @DisplayName("테마 썸네일 URL이 null이면 예외가 발생한다.")
    void create_null_thumbnailUrl() {
        // given
        String nullThumbnailUrl = null;

        // when & then
        assertThatThrownBy(() -> Theme.create(name, description, nullThumbnailUrl))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("테마 썸네일 URL은 필수입니다.");
    }

    @Test
    @DisplayName("테마 썸네일 URL이 비어있으면 디폴트 썸네일로 변환된다.")
    void create_empty_thumbnailUrl() {
        // given
        String emptyThumbnailUrl = "";

        // when
        Theme theme = Theme.create(name, description, emptyThumbnailUrl);

        //then
        assertThat(defaultThumbnailUrl)
                .isEqualTo(theme.thumbnailUrl());
    }
}
