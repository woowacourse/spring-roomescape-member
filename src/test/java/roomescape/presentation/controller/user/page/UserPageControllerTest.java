package roomescape.presentation.controller.user.page;

import io.restassured.RestAssured;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
class UserPageControllerTest {

    public static Stream<Arguments> pagePaths() {
        return Stream.of(
                Arguments.of("/"),
                Arguments.of("/login"),
                Arguments.of("/signup"),
                Arguments.of("/reservation")
        );
    }

    @ParameterizedTest
    @MethodSource("pagePaths")
    @DisplayName("일반 페이지에 인증 없이 접근할 수 있다.")
    void userAccessUserPage(String path) {
        RestAssured.given().log().all()
                .when().get(path)
                .then().log().all()
                .statusCode(200);
    }
}