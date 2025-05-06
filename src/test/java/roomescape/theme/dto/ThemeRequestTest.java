package roomescape.theme.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class ThemeRequestTest {
    @Test
    @DisplayName("모든 값이 유효하면 테마 요청 객체가 정상적으로 생성된다")
    void test7() {
        ThemeRequest request = new ThemeRequest("이름", "설명", "썸네일");

        assertThat(request.name()).isEqualTo("이름");
        assertThat(request.description()).isEqualTo("설명");
        assertThat(request.thumbnail()).isEqualTo("썸네일");
    }

    @Test
    @DisplayName("이름이 null이면 예외가 발생한다")
    void test1() {
        assertThatThrownBy(() -> new ThemeRequest(null, "설명", "썸네일"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 이름을 입력해주세요.");
    }

    @Test
    @DisplayName("이름이 빈 문자열이면 예외가 발생한다")
    void test2() {
        assertThatThrownBy(() -> new ThemeRequest("   ", "설명", "썸네일"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 이름을 입력해주세요.");
    }

    @Test
    @DisplayName("설명이 null이면 예외가 발생한다")
    void test3() {
        assertThatThrownBy(() -> new ThemeRequest("이름", null, "썸네일"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 설명을 입력해주세요.");
    }

    @Test
    @DisplayName("설명이 빈 문자열이면 예외가 발생한다")
    void test4() {
        assertThatThrownBy(() -> new ThemeRequest("이름", "  ", "썸네일"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 설명을 입력해주세요.");
    }

    @Test
    @DisplayName("썸네일이 null이면 예외가 발생한다")
    void test5() {
        assertThatThrownBy(() -> new ThemeRequest("이름", "설명", null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 썸네일을 입력해주세요.");
    }

    @Test
    @DisplayName("썸네일이 빈 문자열이면 예외가 발생한다")
    void test6() {
        assertThatThrownBy(() -> new ThemeRequest("이름", "설명", " "))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 썸네일을 입력해주세요.");
    }
}
