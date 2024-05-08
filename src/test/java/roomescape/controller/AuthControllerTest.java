package roomescape.controller;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import roomescape.controller.request.LoginRequest;
import roomescape.model.Member;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class AuthControllerTest {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert memberInsertActor;

    @Autowired
    public AuthControllerTest(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.memberInsertActor = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("member")
                .usingGeneratedKeyColumns("id");
    }

    @BeforeEach
    void setUp() {
        initDatabase();
        insertMember("에버", "treeboss@gmail.com", "treeboss123!");
        insertMember("우테코", "wtc@gmail.com", "wtc123!");
    }

    private void initDatabase() {
        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY FALSE");
        jdbcTemplate.execute("TRUNCATE TABLE member RESTART IDENTITY");
    }

    private void insertMember(String name, String email, String password) {
        Map<String, Object> parameters = new HashMap<>(3);
        parameters.put("name", name);
        parameters.put("email", email);
        parameters.put("password", password);
        memberInsertActor.execute(parameters);
    }

    @DisplayName("로그인을 성공할 경우 사용자 정보를 바탕으로 토큰을 생성하여 헤더에 담아 반환한다.")
    @Test
    void should_return_token_through_header_when_login_success() {
        Member member = new Member(1L, "에버", "treeboss@gmail.com", "treeboss123!");
        String expected = RestAssured
                .given().log().all()
                .body(new LoginRequest(member.getEmail(), member.getPassword()))
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when().post("/login")
                .then().log().all()
                .extract().header("Set-Cookie");
        String secretKey = "Yn2kjibddFAWtnPJ2AFlL8WXmohJMCvigQggaEypa5E=";
        String actual = Jwts.builder()
                .subject(String.valueOf(member.getId()))
                .claim("name", member.getName())
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .compact();
        assertThat(actual).isEqualTo(expected);
    }
}
