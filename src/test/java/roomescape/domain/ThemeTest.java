package roomescape.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import roomescape.domain.exception.InvalidRequestException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static roomescape.domain.Theme.DEFAULT_THUMBNAIL;

class ThemeTest {

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("이름이 공백일 경우 예외가 발생한다.")
    void validateNull(String name) {
        assertThatThrownBy(() -> Theme.from(name, "", ""))
                .isInstanceOf(InvalidRequestException.class);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("썸네일이 공백일 경우 기본 썸네일로 대체된다.")
    void getDefaultThumbnailIfNotExists(String thumbnail) {
        // given & when
        final Theme theme = Theme.from("spring", "", thumbnail);

        // then
        assertThat(theme.getThumbnail()).isEqualTo(DEFAULT_THUMBNAIL);
    }
}
