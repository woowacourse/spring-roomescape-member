package roomescape.theme.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ThemeTest {
    private static final String DEFAULT_THUMBNAIL_URL = "https://images.unsplash.com/photo-1505693416388-ac5ce068fe85?auto=format&fit=crop&w=800&q=80";
    private final String name = "공포";
    private final String description = "테마 설명";
    private final String emptyThumbnailUrl = "";

    @Test
    @DisplayName("테마 이름이 null이면 예외가 발생한다.")
    void create_null_name() {
        // given
        String nullName = null;

        // when & then
        assertThatThrownBy(() -> Theme.create(nullName, description, emptyThumbnailUrl))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("테마 이름은 필수입니다.");
    }

    @Test
    @DisplayName("테마 이름이 비어있으면 예외가 발생한다.")
    void create_empty_name() {
        // given
        String emptyName = "";

        // when & then
        assertThatThrownBy(() -> Theme.create(emptyName, description, emptyThumbnailUrl))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("테마 이름은 필수입니다.");
    }

    @Test
    @DisplayName("테마 설명이 null이면 예외가 발생한다.")
    void create_null_description() {
        // given
        String nullDescription = null;

        // when & then
        assertThatThrownBy(() -> Theme.create(name, nullDescription, emptyThumbnailUrl))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("테마 설명은 필수입니다.");
    }

    @Test
    @DisplayName("테마 설명이 비어있으면 예외가 발생한다.")
    void create_empty_description() {
        // given
        String emptyDescription = "";

        // when & then
        assertThatThrownBy(() -> Theme.create(name, emptyDescription, emptyThumbnailUrl))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("테마 설명은 필수입니다.");
    }

    @Test
    @DisplayName("테마 썸네일 URL이 비어있으면 디폴트 썸네일로 변환된다.")
    void create_empty_thumbnailUrl() {
        // given & when
        Theme theme = Theme.create(name, description, emptyThumbnailUrl);

        //then
        assertThat(theme.thumbnailUrl())
                .isEqualTo(DEFAULT_THUMBNAIL_URL);
    }

    @Test
    @DisplayName("테마를 생성하면 디폴트로 비활성화 상태이다.")
    void create_default_status_false(){
        // given
        Theme theme = Theme.create(name, description, emptyThumbnailUrl);

        // when & then
        assertThat(theme.isActive()).isFalse();
    }

    @Test
    @DisplayName("테마를 활성화 상태로 바꾼다.")
    void updateStatus_false_to_true(){
        // given
        Theme theme = Theme.create(name, description, emptyThumbnailUrl);

        // when
        theme.updateStatus(true);

        // then
        assertThat(theme.isActive()).isTrue();
    }

    @Test
    @DisplayName("테마를 비활성화 상태로 바꾼다.")
    void updateStatus_true_to_false(){
        // given
        Theme theme = Theme.create(name, description, emptyThumbnailUrl);
        theme.updateStatus(true);

        // when
        theme.updateStatus(false);

        // then
        assertThat(theme.isActive()).isFalse();
    }
}
