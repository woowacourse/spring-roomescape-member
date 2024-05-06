package roomescape.endpoint;

import static org.hamcrest.Matchers.notNullValue;
import static roomescape.endpoint.HttpRestTestTemplate.*;
import static roomescape.endpoint.HttpRestTestTemplate.assertDeleteBadRequest;
import static roomescape.endpoint.HttpRestTestTemplate.assertPostCreated;
import static roomescape.endpoint.RequestFixture.*;
import static roomescape.endpoint.ResponseFormatTestTemplate.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import roomescape.controller.exception.CustomExceptionResponse;
import roomescape.domain.ThemeRepository;
import roomescape.dto.AvailableReservationTimeResponse;
import roomescape.dto.ReservationRequest;
import roomescape.dto.ReservationTimeResponse;
import roomescape.dto.ThemeResponse;

import java.time.LocalDate;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@TestPropertySource(locations = "classpath:application-test.properties")
class ThemeEndPointTest {

    @Autowired
    ObjectMapper objectMapper;

    @DisplayName("[테마 목록 조회] 정상 작동")
    @Test
    void getThemes() {
        assertGetOk("/themes");
    }

    @DisplayName("[테마 추가] 정상 작동")
    @Test
    void addTheme() {
        Response response = assertPostCreated(themeRequest, "/themes");
        assertHeaderContainsValue(response, "location", "/themes/");
        assertBodyFormat(objectMapper, response, ThemeResponse.class);
    }

    @DisplayName("[테마 추가, 해당 테마 삭제] 정상 작동")
    @Test
    void deleteTheme() {
        Response response = assertPostCreated(themeRequest, "/themes");
        String createdId = response.getBody().path("id").toString();

        assertDeleteNoContent("/themes/" + createdId);
    }

    @DisplayName("[테마 추가, 해당 테마 예약 추가, 테마 삭제] 예외 발생")
    @Test
    void deleteThemeFailed() {
        Response response = assertPostCreated(themeRequest, "/themes");
        String themeId = response.getBody().path("id").toString();
        ReservationRequest reservationRequest = new ReservationRequest(
                "산초",
                LocalDate.now().plusDays(1),
                reservationTimeId,
                Long.parseLong(themeId)
        );
        assertPostCreated(reservationRequest, "/reservations");

        Response errorResponse = assertDeleteBadRequest("/themes/" + themeId);
        assertBodyFormat(objectMapper, errorResponse, CustomExceptionResponse.class);
    }

    @DisplayName("[테마 순위 조회] 정상 작동")
    @Test
    void getTopThemes() {
        Response response = assertGetOk("/themes/rankings");
        assertBodyListFormat(objectMapper, response, ThemeResponse.class);
    }
}
