package roomescape.business.model.entity;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import roomescape.business.model.vo.UserRole;
import roomescape.exception.business.NameContainsNumberException;
import roomescape.exception.business.UserNameLengthExceedException;

import static org.assertj.core.api.Assertions.*;

class UserTest {

    private static final String VALID_NAME = "dompoo";
    private static final String VALID_EMAIL = "test@example.com";
    private static final String VALID_PASSWORD = "password123!";
    private static final String VALID_ID = "1";

    @Nested
    class 사용자_생성_테스트 {

        @Test
        void 유효한_정보로_사용자를_생성할_수_있다() {
            // when
            User user = User.create(VALID_NAME, VALID_EMAIL, VALID_PASSWORD);

            // then
            assertThat(user).isNotNull();
            assertThat(user.name()).isEqualTo(VALID_NAME);
            assertThat(user.email()).isEqualTo(VALID_EMAIL);
            assertThat(user.password()).isEqualTo(VALID_PASSWORD);
            assertThat(user.role()).isEqualTo(UserRole.USER.name());
        }

        @Test
        void 저장_후_사용자_객체를_생성할_수_있다() {
            // when
            User user = User.restore(VALID_ID, UserRole.ADMIN.name(), VALID_NAME, VALID_EMAIL, VALID_PASSWORD);

            // then
            assertThat(user).isNotNull();
            assertThat(user.id()).isEqualTo(VALID_ID);
            assertThat(user.name()).isEqualTo(VALID_NAME);
            assertThat(user.email()).isEqualTo(VALID_EMAIL);
            assertThat(user.password()).isEqualTo(VALID_PASSWORD);
            assertThat(user.role()).isEqualTo(UserRole.ADMIN.name());
        }
    }

    @Nested
    class 이름_유효성_검증_테스트 {

        @Test
        void 이름이_10자_초과이면_예외가_발생한다() {
            // when, then
            final String tooLongName = "이름이너무길어서예외가발생합니다";

            assertThatThrownBy(() -> User.create(tooLongName, VALID_EMAIL, VALID_PASSWORD))
                    .isInstanceOf(UserNameLengthExceedException.class);
        }

        @ParameterizedTest
        @ValueSource(strings = {"홍길동1", "user123", "tester99"})
        void 이름에_숫자가_포함되면_예외가_발생한다(String nameWithNumber) {
            // when, then
            assertThatThrownBy(() -> User.create(nameWithNumber, VALID_EMAIL, VALID_PASSWORD))
                    .isInstanceOf(NameContainsNumberException.class);
        }

        @Test
        void 이름은_정확히_10자까지_허용된다() {
            // when
            final String exactlyTenChars = "열자까지만됩니다";
            User user = User.create(exactlyTenChars, VALID_EMAIL, VALID_PASSWORD);

            // then
            assertThat(user.name()).isEqualTo(exactlyTenChars);
        }
    }

    @Nested
    class 비밀번호_검증_테스트 {

        @Test
        void 올바른_비밀번호로_검증에_성공한다() {
            // given
            User user = User.create(VALID_NAME, VALID_EMAIL, VALID_PASSWORD);

            // when
            boolean result = user.isPasswordCorrect(VALID_PASSWORD);

            // then
            assertThat(result).isTrue();
        }

        @Test
        void 잘못된_비밀번호로_검증에_실패한다() {
            // given
            User user = User.create(VALID_NAME, VALID_EMAIL, VALID_PASSWORD);

            // when
            boolean result = user.isPasswordCorrect("wrong_password");

            // then
            assertThat(result).isFalse();
        }
    }

    @Nested
    class 사용자_역할_테스트 {

        @Test
        void 생성된_사용자의_기본_역할은_USER이다() {
            // when
            User user = User.create(VALID_NAME, VALID_EMAIL, VALID_PASSWORD);

            // then
            assertThat(user.role()).isEqualTo(UserRole.USER.name());
        }

        @Test
        void afterSave_메서드로_ADMIN_역할의_사용자를_생성할_수_있다() {
            // when
            User adminUser = User.restore(VALID_ID, UserRole.ADMIN.name(), VALID_NAME, VALID_EMAIL, VALID_PASSWORD);

            // then
            assertThat(adminUser.role()).isEqualTo(UserRole.ADMIN.name());
        }
    }

    @Nested
    class 사용자_정보_접근_테스트 {

        @Test
        void 사용자_정보에_접근할_수_있다() {
            // given
            User user = User.create(VALID_NAME, VALID_EMAIL, VALID_PASSWORD);

            // when, then
            assertThat(user.name()).isEqualTo(VALID_NAME);
            assertThat(user.email()).isEqualTo(VALID_EMAIL);
            assertThat(user.password()).isEqualTo(VALID_PASSWORD);
        }
    }
}
