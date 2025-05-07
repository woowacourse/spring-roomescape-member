package roomescape.domain.auth.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AuthServiceTest {

    @DisplayName("회원을 등록한다.")
    @Test
    void registerTest1() {

    }

    @DisplayName("존재하는 이메일이면 AlreadyInUseException을 반환한다.")
    @Test
    void register_throwsException() {
        // given

        // when & then
        //        assertThatThrownBy(() -> {
        //
        //        }).isInstanceOf(AlreadyInUseException.class);
    }

    @DisplayName("토큰을 검증하여 로그인 정보를 반환한다.")
    void checkTest1() {

    }

    @DisplayName("존재하지 않는 id의 경우 예외를 반환한다.")
    @Test
    void check_throwsException() {
        // given

        // when & then
        //        assertThatThrownBy(() -> {
        //
        //        }).isInstanceOf(UserNotFoundForTokenException.class);
    }

}
