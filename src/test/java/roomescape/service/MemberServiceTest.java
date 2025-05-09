package roomescape.service;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import roomescape.domain.MemberRoleType;
import roomescape.exception.custom.ExistedDuplicateValueException;
import roomescape.service.dto.request.MemberLoginCreation;
import roomescape.service.dto.request.MemberSignUpCreation;
import roomescape.service.dto.response.MemberResult;
import roomescape.service.dto.response.MemberSignUpResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
@Sql(scripts = "/schema.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/reservation-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class MemberServiceTest {

    @Autowired
    MemberService memberService;

    @Test
    @DisplayName("회원 가입을 할 수 있다")
    void register() {
        //given
        MemberSignUpCreation creation = new MemberSignUpCreation("new", "new@email.com", "1234");

        //when
        MemberSignUpResult actual = memberService.register(creation);

        //then
        assertThat(actual.name()).isEqualTo(creation.name());
    }

    @Test
    @DisplayName("이미 사용 중인 이메일은 회원 가입 할 수 없다")
    void cannotRegisterWhenExistedEmail() {
        //given
        MemberSignUpCreation creation = new MemberSignUpCreation("duplicate", "test@email.com", "1234");

        //when //then
        assertThatThrownBy(() -> memberService.register(creation))
                .isInstanceOf(ExistedDuplicateValueException.class)
                .hasMessage("이미 사용 중인 이메일입니다");
    }

    @Test
    @DisplayName("사용자의 정보를 기반으로 jwt 토큰을 발행한다")
    void publishAccessToken() {
        //given
        MemberLoginCreation creation = new MemberLoginCreation("test@email.com", "1234");

        //when
        String actual = memberService.publishAccessToken(creation);

        //then
        assertThat(actual).isNotNull();
    }

    @ParameterizedTest
    @CsvSource(value = {"MEMBER,1", "ADMIN,0"})
    @DisplayName("권한을 기준으로 전체 유저를 조회한다")
    void getAllMemberByRole(MemberRoleType role, int expectedSize) {
        //when
        List<MemberResult> actual = memberService.getAllMemberByRole(role);

        //then
        assertThat(actual).hasSize(expectedSize);
    }
}
