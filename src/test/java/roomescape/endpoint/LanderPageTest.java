package roomescape.endpoint;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@TestPropertySource(properties = {"spring.config.location = classpath:application-test.yml"})
class LanderPageTest {

    @ParameterizedTest(name = "{0} ,{1}")
    @CsvSource(value = {
            "/admin:관리자 메인 페이지",
            "/admin/reservation:예약 관리 페이지",
            "/admin/time:예약 시간 관리 페이지",
            "/admin/theme:테마 관리 페이지",
            "/reservation:예약 페이지",
            "/:사용자 메인 페이지"
    }, delimiter = ':')
    void loadPage(String path, String description) {
        HttpRestTestTemplate.assertGetOk(path);
    }
}
