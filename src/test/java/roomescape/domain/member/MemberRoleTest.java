package roomescape.domain.member;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class MemberRoleTest {

    @ParameterizedTest
    @CsvSource(value = {"ADMIN,true", "USER,false"})
    @DisplayName("회원 역할이 관리자인지 확인한다.")
    void isAdmin(MemberRole given, boolean expected) {
        //when
        boolean result = given.isAdmin();

        //then
        assertThat(result).isEqualTo(expected);
    }
}
