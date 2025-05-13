package roomescape.config;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.business.domain.member.MemberCredential;

class AccessTokenTest {

    @DisplayName("사용자 객체로 AccessToken을 생성한다.")
    @Test
    void create() {
        // given
        MemberCredential memberCredential = new MemberCredential(1L, "bello@email.com", "password");

        // when
        AccessToken accessToken = AccessToken.create(memberCredential);

        // then
        assertAll(
                () -> assertThat(accessToken).isNotNull(),
                () -> assertThat(accessToken.getValue()).isNotBlank()
        );
    }

    @DisplayName("AccessToken 객체를 생성한다.")
    @Test
    void createWithTokenValue() {
        // given
        String tokenValue = "tokenValue";

        // when
        AccessToken accessToken = AccessToken.of(tokenValue);

        // then
        assertAll(
                () -> assertThat(accessToken).isNotNull(),
                () -> assertThat(accessToken.getValue()).isEqualTo(tokenValue)
        );
    }

    @DisplayName("AccessToken에서 사용자 ID를 추출한다.")
    @Test
    void extractMemberId() {
        // given
        MemberCredential memberCredential = new MemberCredential(1L, "bello@email.com", "password");
        AccessToken accessToken = AccessToken.create(memberCredential);

        // when
        Long memberId = accessToken.extractMemberId();

        // then
        assertThat(memberId)
                .isEqualTo(1L);
    }
}
