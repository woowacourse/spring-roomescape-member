package roomescape.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class RoleTest {

    @ParameterizedTest
    @CsvSource(value = {"ADMIN,true", "USER,false"})
    @DisplayName("관리자인지 판단한다.")
    void isAdmin(Role role, boolean expected) {
        // when
        boolean result = role.isAdmin();

        // then
        assertThat(result).isEqualTo(expected);
    }
}
