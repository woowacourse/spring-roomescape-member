package roomescape.support;

import io.restassured.RestAssured;
import java.time.LocalTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.jdbc.Sql;
import roomescape.application.ReservationTimeService;
import roomescape.application.ThemeService;
import roomescape.application.dto.request.ReservationTimeCreationRequest;
import roomescape.application.dto.request.ThemeCreationRequest;
import roomescape.auth.TokenProvider;
import roomescape.support.extension.TableTruncateExtension;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(TableTruncateExtension.class)
@Sql("/member.sql")
public class AcceptanceTest {
    @Autowired
    private TokenProvider tokenProvider;
    @Autowired
    private ThemeService themeService;
    @Autowired
    private ReservationTimeService reservationTimeService;
    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    public String adminToken() {
        return tokenProvider.createToken("1");
    }

    public String userToken() {
        return tokenProvider.createToken("2");
    }

    public void createTheme() {
        ThemeCreationRequest request = new ThemeCreationRequest("테마1", "설명", "https://test.com");
        themeService.save(request);
    }

    public void createReservationTime() {
        ReservationTimeCreationRequest request = new ReservationTimeCreationRequest(LocalTime.parse("10:00"));
        reservationTimeService.register(request);
    }
}
