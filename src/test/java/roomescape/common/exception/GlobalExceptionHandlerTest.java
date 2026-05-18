package roomescape.common.exception;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.common.exception.code.TestErrorCode;
import roomescape.exception.GlobalExceptionHandler;
import roomescape.exception.custom.BusinessException;

public class GlobalExceptionHandlerTest {
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new TestController())
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void BusinessException_발생시_올바른_예외_응답을_반환한다() throws Exception {
        mockMvc.perform(get("/test/business-error"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("TEST_BUSINESS_ERROR"))
                .andExpect(jsonPath("$.message").value("테스트용 비즈니스 예외코드 입니다."));
    }

    @Test
    void 예상하지_못한_예외_발생시_올바른_예외_응답을_반환한다() throws Exception {
        mockMvc.perform(get("/test/unexpected-error"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.code").value("INTERNAL_SERVER_ERROR"))
                .andExpect(jsonPath("$.message").value("서버 내부에서 에러가 발생하였습니다. 개발자에게 직접 문의하세요."));
    }

    @Test
    void Valid_검증_실패시_올바른_예외_응답을_반환한다() throws Exception {
        mockMvc.perform(post("/test/validation-error")
                        .contentType(APPLICATION_JSON)
                .content("""
                                {
                                  "name": ""
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("INVALID_INPUT_VALUE"))
                .andExpect(jsonPath("$.message").value("공백일 수 없습니다"));
    }

    @Test
    void ModelAttribute_검증_실패시_올바른_예외_응답을_반환한다() throws Exception {
        mockMvc.perform(get("/test/model-attribute-validation-error")
                        .queryParam("name", ""))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("INVALID_INPUT_VALUE"))
                .andExpect(jsonPath("$.message").value("공백일 수 없습니다"));
    }

    @RestController
    static class TestController {

        @GetMapping("/test/business-error")
        String throwBusinessException() {
            throw new BusinessException(TestErrorCode.TEST_BUSINESS_ERROR);
        }

        @GetMapping("/test/unexpected-error")
        String throwUnexpectedException() throws Exception {
            throw new Exception();
        }

        @PostMapping("/test/validation-error")
        String validate(@RequestBody @Valid TestRequest request) {
            return request.name();
        }

        @GetMapping("/test/model-attribute-validation-error")
        String validateModelAttribute(@Valid TestRequest request) {
            return request.name();
        }
    }

    record TestRequest(@NotBlank(message = "공백일 수 없습니다") String name) {
    }
}
