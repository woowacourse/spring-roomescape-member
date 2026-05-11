package roomescape.reservationtime.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static roomescape.config.TestFixture.reservationTimeRequestBody;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@AutoConfigureMockMvc
@SpringBootTest
class AdminReservationTimeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void 예약_시간을_추가한다() throws Exception {
        Map<String, Object> request = reservationTimeRequestBody("11:00");

        mockMvc.perform(post("/admin/times")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.startAt").value("11:00:00"));
    }

    @Test
    void 예약_시간을_삭제한다() throws Exception {
        Map<String, Object> request = reservationTimeRequestBody("11:00");
        int id = postReservationTime(request);

        mockMvc.perform(delete("/admin/times/{id}", id))
                .andExpect(status().isNoContent());
    }

    @Test
    void 존재하지_않는_예약_시간을_삭제하면_404를_응답한다() throws Exception {
        mockMvc.perform(delete("/admin/times/{id}", 999))
                .andExpect(status().isNotFound());
    }

    @ParameterizedTest(name = "{0}은 예약 시간 형식이 아니다")
    @ValueSource(strings = {"", "abc", "25:00"})
    void 예약_시간을_추가할_때_시간_형식이_올바르지_않으면_400을_응답한다(String startAt) throws Exception {
        Map<String, Object> request = reservationTimeRequestBody(startAt);

        mockMvc.perform(post("/admin/times")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    private int postReservationTime(Map<String, Object> request) throws Exception {
        MvcResult result = mockMvc.perform(post("/admin/times")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn();

        return objectMapper.readTree(result.getResponse().getContentAsString())
                .get("id")
                .asInt();
    }

}
