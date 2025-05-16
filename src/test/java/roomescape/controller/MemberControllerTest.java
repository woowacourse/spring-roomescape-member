package roomescape.controller;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class MemberControllerTest {
//    private Map<String, String> getPostUserParams(String name, String email, String password) {
//        Map<String, String> params = new HashMap<>();
//        params.put("name", name);
//        params.put("email", email);
//        params.put("password", password);
//        params.put("role", "user");
//        return params;
//    }
//
//    @Test
//    void Test_Member_Post() {
//        Map<String, String> params = getPostUserParams("띠용", "asd@asd.com", "asd123");
//        RestAssured.given().log().all()
//                .contentType(ContentType.JSON)
//                .body(params)
//                .when().post("/members")
//                .then().log().all()
//                .statusCode(201);
//        params = getPostUserParams("벡터", "vec@vec.com", "vec123");
//        RestAssured.given().log().all()
//                .contentType(ContentType.JSON)
//                .body(params)
//                .when().post("/members")
//                .then().log().all()
//                .statusCode(201);
//    }
}
