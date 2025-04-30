package roomescape.dto;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.dto.theme.ThemeRequestDto;

class ThemeRequestDtoTest {

    @DisplayName("테마명은 null 일 수 없다.")
    @Test
    void test1() {
        assertThatThrownBy(() -> new ThemeRequestDto(null, "s","s"))
                        .isInstanceOf(IllegalArgumentException.class);

    }

    @DisplayName("테마 설명은 null 일 수 없다.")
    @Test
    void test2() {
        assertThatThrownBy(() -> new ThemeRequestDto("ㄴ", null,"s"))
                .isInstanceOf(IllegalArgumentException.class);

    }

    @DisplayName("썸네일 이미지는 null 일 수 없다.")
    @Test
    void test3() {
        assertThatThrownBy(() -> new ThemeRequestDto("ㅇ", "s",null))
                .isInstanceOf(IllegalArgumentException.class);

    }

    @DisplayName("테마명은 공백 일 수 없다.")
    @Test
    void test4() {
        assertThatThrownBy(() -> new ThemeRequestDto("", "s","s"))
                .isInstanceOf(IllegalArgumentException.class);

    }

    @DisplayName("테마 설명은 공백 일 수 없다.")
    @Test
    void test5() {
        assertThatThrownBy(() -> new ThemeRequestDto("ㅇ", "","s"))
                .isInstanceOf(IllegalArgumentException.class);

    }

    @DisplayName("썸내일 이미지는 공백 일 수 없다.")
    @Test
    void test6() {
        assertThatThrownBy(() -> new ThemeRequestDto("ㅇ", "s",""))
                .isInstanceOf(IllegalArgumentException.class);

    }
}
