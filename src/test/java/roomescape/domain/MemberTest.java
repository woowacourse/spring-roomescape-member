package roomescape.domain;

import static org.assertj.core.api.Assertions.assertThatCode;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MemberTest {

    @Test
    @DisplayName("생성 테스트")
    void create() {
        Name name = new Name("호돌");

        assertThatCode(() -> new Member(name, "email", "password", MemberRole.NORMAL))
                .doesNotThrowAnyException();
    }
}
