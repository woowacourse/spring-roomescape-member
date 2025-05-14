package roomescape.controller;

import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.dto.request.UserReservationRequest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class UserReservationControllerTest {
    private String token;

    @BeforeEach
    void setUp() {
        ApiTestFixture.signUpUser("user@naver.com", "password", "vector");
        token = ApiTestFixture.loginAndGetToken("user@naver.com", "password");

        ApiTestFixture.createReservationTime(LocalTime.of(10, 0));
        ApiTestFixture.createTheme(
                "Ddyong",
                "살인마가 쫓아오는 느낌",
                "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"
        );
    }

    @Test
    @DisplayName("유저가 예약을 생성하면 201을 반환한다")
    void addReservationByUser() {
        UserReservationRequest request = new UserReservationRequest(
                LocalDate.of(2025, 8, 5),
                1L,
                1L
        );

        ApiTestHelper.post("/user/reservations", token, request)
                .statusCode(201);
    }
}
