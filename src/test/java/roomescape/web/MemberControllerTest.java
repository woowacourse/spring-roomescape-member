package roomescape.web;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import roomescape.web.api.resolver.AdminAuthValidateInterceptor;
import roomescape.web.api.resolver.MemberAuthValidateInterceptor;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class MemberControllerTest extends ControllerTest {

    @MockBean
    private MemberAuthValidateInterceptor memberAuthValidateInterceptor;

    @MockBean
    private AdminAuthValidateInterceptor adminAuthValidateInterceptor;

    @Autowired
    private MockMvc mockMvc;

    @DisplayName("모든 사용자를 조회한다")
    @Sql("/test-data/members.sql")
    @Test
    void when_findAllMembers_then_returnMembers() throws Exception {
        // given
        doReturn(true).when(memberAuthValidateInterceptor).preHandle(any(), any(), any());
        doReturn(true).when(adminAuthValidateInterceptor).preHandle(any(), any(), any());

        // when, then
        mockMvc.perform(get("/members"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }
}
