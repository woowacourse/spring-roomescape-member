package roomescape.member.controller;

import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import roomescape.config.WebMvcControllerTestConfig;
import roomescape.member.dto.MemberSignUpRequest;
import roomescape.member.service.MemberSignUpService;

@WebMvcTest(MemberSignUpApiController.class)
@Import(WebMvcControllerTestConfig.class)
class MemberSignUpApiControllerTest {

    @MockBean
    private MemberSignUpService memberSignUpService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @DisplayName("회원가입에 성공하면 201 응답과 Location 헤더에 리소스 저장 경로를 받는다.")
    @Test
    void signup() throws Exception {
        MemberSignUpRequest memberSignUpRequest = new MemberSignUpRequest("카키", "kaki@email.com", "1234");

        doReturn(1L).when(memberSignUpService)
                .save(memberSignUpRequest);

        mockMvc.perform(post("/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(memberSignUpRequest))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/members/1"));
    }
}
