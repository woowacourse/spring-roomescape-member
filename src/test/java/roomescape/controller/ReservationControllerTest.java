package roomescape.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ReservationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("예약 등록 시 예외가 발생할 때 400에러를 반환한다.")
    void test1() throws Exception {
        Map<String, String> params = new HashMap<>();
        params.put("name", "브라운");
        params.put("date", "ㅇㅇㅇㅇ");  // 잘못된 날짜 형식
        params.put("timeId", "1");
        params.put("themeId", "1");

        // JSON 객체로 변환 후 요청
        mockMvc.perform(post("/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(params))) // body에 데이터 담기
                .andExpect(status().isBadRequest()); // 400 응답 코드 예상
    }
}
