package roomescape.domain;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MemberTest {

    @DisplayName("최대 길이를 넘는 이름으로는 예약을 생성할 수 없다")
    @Test
    void cannotCreateReservationWithTooLongParameters() {
        String tooLongName = "i".repeat(256);
        String tooLongEmail = "i".repeat(256);
        String tooLongPassword = "i".repeat(256);
        String tooLongRole = "i".repeat(256);
        assertAll(
                () -> assertThatThrownBy(
                        () -> new Member(1L, tooLongName, "이메일", "비번", "역할"))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("[ERROR] 허용되지 않는 이름 길이입니다."),
                () -> assertThatThrownBy(
                        () -> new Member(1L, "이름", tooLongEmail, "비번", "역할"))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("[ERROR] 허용되지 않는 이메일 길이입니다."),
                () -> assertThatThrownBy(
                        () -> new Member(1L, "이름", "이메일", tooLongPassword, "역할"))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("[ERROR] 허용되지 않는 비밀번호 길이입니다."),
                () -> assertThatThrownBy(
                        () -> new Member(1L, "이름", "이메일", "비번", tooLongRole))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("[ERROR] 허용되지 않는 역할 길이입니다.")
        );
    }

}