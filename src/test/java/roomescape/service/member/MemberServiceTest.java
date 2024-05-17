package roomescape.service.member;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import io.restassured.RestAssured;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.jdbc.Sql;
import roomescape.domain.member.MemberInfo;
import roomescape.domain.member.Role;
import roomescape.dto.member.SignupRequest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = {"/test_schema.sql"})
class MemberServiceTest {

    @Autowired
    private MemberService memberService;

    @LocalServerPort
    int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @DisplayName("모든 멤버를 조회한다.")
    @Test
    void findAll() {
        // given
        memberService.insertMember(new SignupRequest("email1@email.com", "password1", "name1"));
        memberService.insertMember(new SignupRequest("email2@email.com", "password2", "name2"));

        // when
        List<MemberInfo> members = memberService.findAll();

        // then
        assertThat(members).hasSize(2);
        assertThat(members).extracting("name").containsExactly("name1", "name2");
    }

    @DisplayName("멤버를 추가한다.")
    @Test
    void insertMember() {
        // given
        SignupRequest signupRequest = new SignupRequest("email@email.com", "password", "name");

        // when
        MemberInfo member = memberService.insertMember(signupRequest);

        // then
        assertThat(member).isNotNull();
        assertThat(member.getId()).isEqualTo(1L);
        assertThat(member.getName()).isEqualTo("name");
        assertThat(member.getRole()).isEqualTo(Role.USER);
    }

    @DisplayName("이미 존재하는 이메일로 멤버를 추가하면 예외가 발생된다.")
    @Test
    void insertMemberWithExistEmail() {
        // given
        String email = "email@email.com";
        memberService.insertMember(new SignupRequest(email, "password", "name"));

        // when & then
        assertThatThrownBy(() -> memberService.insertMember(new SignupRequest(email, "password1", "name1")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미 존재하는 이메일입니다.");
    }

    @DisplayName("이메일과 비밀번호가 같으면 추가할 수 없다.")
    @Test
    void insertMemberWithSameEmailAndPassword() {
        // given
        String email = "email@email.com";
        String password = email;

        // when & then
        assertThatThrownBy(() -> memberService.insertMember(new SignupRequest(email, password, "name")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이메일과 비밀번호는 같을 수 없습니다.");
    }
}
