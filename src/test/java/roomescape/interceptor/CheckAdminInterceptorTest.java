package roomescape.interceptor;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CheckAdminInterceptorTest {

    @Autowired
    private RequestMappingHandlerMapping mapping;

    @LocalServerPort
    int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @DisplayName("어드민 페이지에 접속하면 어드민 체크 인터셉터가 실행된다.")
    @ParameterizedTest
    @ValueSource(strings = {"/admin", "/admin/reservation", "/admin/theme", "/admin/time"})
    void checkAdminInterceptorWorking(String requestURI) throws Exception {
        // given
        MockHttpServletRequest request = new MockHttpServletRequest("GET", requestURI);

        // when
        HandlerExecutionChain chain = mapping.getHandler(request);

        // then
        assert chain != null;
        Optional<HandlerInterceptor> checkAdminInterceptor = chain.getInterceptorList()
                .stream()
                .filter(CheckAdminInterceptor.class::isInstance)
                .findFirst();

        assertThat(checkAdminInterceptor.isPresent()).isTrue();
    }
}
