package roomescape.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MemberTest {

    @Test
    void checkPasswordEquals() {
        // given
        Member member = new Member(1L, "제프리", "jeffrey@gmail.com", "1234!@#$", MemberRole.USER);
        String password = "1234!@#$";
        String wrongPassword = "1234@";

        // when
        boolean checkPassword = member.checkPassword(password);
        boolean checkWrongPassword = member.checkPassword(wrongPassword);

        // then
        assertThat(checkPassword).isTrue();
        assertThat(checkWrongPassword).isFalse();
    }

    @DisplayName("이름의 사이즈가 최댓값 이내로 입력되었는지 검증한다.")
    @Test
    void validateNameSize() {
        // given
        String validName = "가나다라마";
        String invalidName = "가나다라마바";

        // when // then
        Member member = new Member(1L, validName, "jeffrey@gmail.com", "1234!@#$", MemberRole.USER);
        assertThat(member.getName()).isEqualTo("가나다라마");
        assertThatThrownBy(() -> new Member(2L, invalidName, "jeffrey@gmail.com", "1234!@#$", MemberRole.USER))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 이름은 5글자 이내로 입력해 주세요. 현재 길이는 6글자 입니다.");
    }

    @DisplayName("이름이 한국어로만 입력되었는지 검증한다.")
    @Test
    void validateNameIsKorean() {
        //given
        String validName = "제프리";
        String invalidName = "Jeffrey";
        LocalDate date = LocalDate.of(2025, 4, 18);

        // when // then
        Member member = new Member(1L, validName, "jeffrey@gmail.com", "1234!@#$", MemberRole.USER);
        assertThat(member.getName()).isEqualTo("제프리");
        assertThatThrownBy(() -> new Member(2L, invalidName, "jeffrey@gmail.com", "1234!@#$", MemberRole.USER))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 이름은 한국어로만 입력해 주세요.");
    }
}
