package roomescape.base;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.auth.provider.JwtTokenProvider;
import roomescape.member.domain.Member;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class BaseTest {

    @LocalServerPort
    protected int port;

    @Autowired
    protected JwtTokenProvider jwtTokenProvider;

    protected Member admin;
    protected Member member;
    protected String adminToken;
    protected String memberToken;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;

        admin = new Member(1L, "한태웅", "taewoong@example.com", "123", "ADMIN");
        member = new Member(1L, "김철수", "chulsoo@example.com", "123", "USER");

        adminToken = jwtTokenProvider.createToken(admin);
        memberToken = jwtTokenProvider.createToken(member);
    }
}
