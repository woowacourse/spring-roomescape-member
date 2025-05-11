package roomescape.infrastructure;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import roomescape.domain.member.Member;
import roomescape.domain.member.Role;
import roomescape.infrastructure.jwt.JwtTokenProvider;

@SpringBootTest
@AutoConfigureMockMvc
class AdminRequestTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    private String userToken;
    private String adminToken;

    @BeforeEach
    void setUp() {
        Member user = new Member(1L, "사용자", Role.USER, "user@test.com", "password");
        userToken = jwtTokenProvider.createToken(user);

        Member admin = new Member(2L, "관리자", Role.ADMIN, "admin@test.com", "password");
        adminToken = jwtTokenProvider.createToken(admin);
    }

    @Test
    @DisplayName("관리자는 관리자 페이지에 접근할 수 있다")
    void adminUser_canAccessAdminPage() throws Exception {
        mockMvc.perform(get("/admin/reservation")
                        .cookie(new Cookie("token", adminToken)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("인증되지 않은 사용자가 관리자 페이지 접근 시 403 에러가 발생한다")
    void unauthenticatedUser_cannotAccessAdminPage() throws Exception {
        mockMvc.perform(get("/admin/reservation"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("일반 사용자가 관리자 페이지 접근 시 403 에러가 발생한다")
    void normalUser_cannotAccessAdminPage() throws Exception {
        mockMvc.perform(get("/admin/reservation")
                        .cookie(new Cookie("token", userToken)))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("잘못된 토큰으로 관리자 페이지 접근 시 403 에러가 발생한다")
    void invalidToken_cannotAccessAdminPage() throws Exception {
        mockMvc.perform(get("/admin/reservation")
                        .cookie(new Cookie("token", "invalid-token")))
                .andExpect(status().isForbidden());
    }
}
