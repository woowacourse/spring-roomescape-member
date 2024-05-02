package roomescape.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static roomescape.exception.ExceptionType.DESCRIPTION_EMPTY;
import static roomescape.exception.ExceptionType.NAME_EMPTY;
import static roomescape.exception.ExceptionType.THUMBNAIL_EMPTY;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.exception.RoomescapeException;

class ThemeTest {

    @DisplayName("객체 생성 테스트")
    @Test
    void constructTest() {
        assertAll(
                () -> assertThatThrownBy(() -> new Theme(null, "description", "thumbnail"))
                        .isInstanceOf(RoomescapeException.class)
                        .hasMessage(NAME_EMPTY.getMessage()),

                () -> assertThatThrownBy(() -> new Theme("name", null, "thumbnail"))
                        .isInstanceOf(RoomescapeException.class)
                        .hasMessage(DESCRIPTION_EMPTY.getMessage()),

                () -> assertThatThrownBy(() -> new Theme("name", "description", null))
                        .isInstanceOf(RoomescapeException.class)
                        .hasMessage(THUMBNAIL_EMPTY.getMessage()),

                () -> assertThatCode(() -> new Theme("name", "description", "thumbnail"))
                        .doesNotThrowAnyException(),

                () -> assertThatCode(() -> new Theme(null, "name", "description", "thumbnail"))
                        .doesNotThrowAnyException(),

                () -> assertThatCode(() -> new Theme(1L, "name", "description", "thumbnail"))
                        .doesNotThrowAnyException()
        );
    }

    @DisplayName("동등성 테스트")
    @Test
    void equalsTest() {
        assertAll(
                () -> assertThat(new Theme(1L, "name", "description", "thumbnail"))
                        .isEqualTo(new Theme(1L, "otherName", "otherDescription", "otherThumbnail")),

                () -> assertThat(new Theme(1L, "sameName", "sameDescription", "sameThumbnail"))
                        .isNotEqualTo(new Theme(2L, "sameName", "sameDescription", "sameThumbnail")),

                () -> assertThat(new Theme(1L, "sameName", "sameDescription", "sameThumbnail"))
                        .isNotEqualTo(new Theme(null, "sameName", "sameDescription", "sameThumbnail"))
        );
    }
}
