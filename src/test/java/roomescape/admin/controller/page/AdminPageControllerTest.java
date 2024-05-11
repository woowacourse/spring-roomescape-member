package roomescape.admin.controller.page;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import jakarta.servlet.http.Cookie;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import roomescape.auth.dto.LoginRequestDto;
import roomescape.auth.service.AuthService;
import roomescape.member.domain.Role;
import roomescape.member.dto.MemberRequestDto;
import roomescape.member.service.MemberService;


@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
@AutoConfigureMockMvc
@Sql(scripts = {"/schema.sql"})
public class AdminPageControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private AuthService authService;
    @Autowired
    private MemberService memberService;
    private String token;

    @BeforeEach
    void setUp() throws Exception {
        MemberRequestDto memberRequestDto = new MemberRequestDto("hotea@hotea.com", "1234", "hotea", Role.ADMIN);
        memberService.save(memberRequestDto);
        token = authService.login(new LoginRequestDto("1234", "hotea@hotea.com"));
    }


    @Test
    void admin() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/admin")
                        .cookie(new Cookie("token", token)))
                .andExpect(status().isOk());
    }

    @Test
    void reservation() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/admin/reservation")
                        .cookie(new Cookie("token", token)))
                .andExpect(status().isOk());
    }

    @Test
    void time() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/admin/time")
                        .cookie(new Cookie("token", token)))
                .andExpect(status().isOk());
    }

    @Test
    void theme() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/admin/theme")
                        .cookie(new Cookie("token", token)))
                .andExpect(status().isOk());
    }
}
