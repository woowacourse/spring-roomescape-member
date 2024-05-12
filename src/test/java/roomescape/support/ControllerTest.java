package roomescape.support;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import roomescape.application.AuthService;
import roomescape.application.ReservationService;
import roomescape.application.ReservationTimeService;
import roomescape.application.ThemeService;
import roomescape.auth.AuthenticationInfoExtractor;
import roomescape.domain.member.MemberRepository;
import roomescape.ui.controller.AuthController;
import roomescape.ui.controller.ReservationController;
import roomescape.ui.controller.ReservationTimeController;
import roomescape.ui.controller.ThemeController;

@WebMvcTest(controllers = {
        ReservationController.class,
        ReservationTimeController.class,
        ThemeController.class,
        AuthController.class
})
public class ControllerTest {
    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    protected ObjectMapper objectMapper;
    @MockBean
    private ReservationService reservationService;
    @MockBean
    private ReservationTimeService reservationTimeService;
    @MockBean
    private ThemeService themeService;
    @MockBean
    private AuthService authService;
    @MockBean
    private AuthenticationInfoExtractor authenticationInfoExtractor;
    @MockBean
    private MemberRepository memberRepository;
}
