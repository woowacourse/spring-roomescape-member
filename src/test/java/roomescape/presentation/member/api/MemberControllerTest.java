package roomescape.presentation.member.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.RestAssured;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.server.LocalServerPort;
import roomescape.persistence.fakerepository.FakeMemberRepository;
import roomescape.presentation.AbstractControllerTest;
import roomescape.presentation.auth.dto.MemberRequestDto;
import roomescape.presentation.member.dto.MemberResponseDto;

class MemberControllerTest extends AbstractControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private FakeMemberRepository memberRepository;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @AfterEach
    void tearDown() {
        memberRepository.clear();
    }

    @DisplayName("가입된 사용자 목록을 조회한다.")
    @Test
    void getMembers() {
        // given
        String email = "test@email.com";
        String password = "password";
        String name = "test";
        MemberRequestDto memberRequestDto = new MemberRequestDto(email, password, name);
        RestAssured
                .given()
                .log().all()
                .contentType("application/json")
                .body(memberRequestDto)
                .when()
                .post("/members")
                .then()
                .log().all()
                .statusCode(201);

        // when
        var members = RestAssured
                .given()
                .log().all()
                .contentType("application/json")
                .when()
                .get("/members")
                .then()
                .log().all()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getList(".", MemberResponseDto.class);

        // then
        assertAll(
                () -> assertThat(members).isNotEmpty(),
                () -> assertThat(members.get(0).name()).isEqualTo(name)
        );
    }
}
