package roomescape.reservationtime.controller;

import static org.hamcrest.Matchers.containsString;
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
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;
import roomescape.common.exception.DomainType;
import roomescape.common.exception.NotFoundException;

@Transactional
@AutoConfigureMockMvc
@SpringBootTest
class AdminReservationTimeControllerTest {

    private static final String START_AT = "11:00";
    private static final String START_AT_RESPONSE = "11:00:00";
    private static final int NOT_FOUND_ID = 999;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void 예약_시간을_추가한다() throws Exception {
        // given
        Map<String, Object> request = reservationTimeRequestBody(START_AT);

        // when
        ResultActions result = mockMvc.perform(post("/admin/times")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        // then
        result.andExpect(status().isCreated())
                .andExpect(jsonPath("$.startAt").value(START_AT_RESPONSE));
    }

    @Test
    void 예약_시간을_삭제한다() throws Exception {
        // given
        Map<String, Object> request = reservationTimeRequestBody(START_AT);
        int id = postReservationTime(request);

        // when
        ResultActions result = mockMvc.perform(delete("/admin/times/{id}", id));

        // then
        result.andExpect(status().isNoContent());
    }

    @Test
    void 존재하지_않는_예약_시간을_삭제하면_404를_응답한다() throws Exception {
        // when
        ResultActions result = mockMvc.perform(delete("/admin/times/{id}", NOT_FOUND_ID));

        // then
        result.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(containsString(
                        NotFoundException.clientMessage(DomainType.RESERVATION_TIME)
                )));
    }

    @ParameterizedTest(name = "{0}은 예약 시간 형식이 아니다")
    @ValueSource(strings = {"", "abc", "25:00"})
    void 예약_시간을_추가할_때_시간_형식이_올바르지_않으면_400을_응답한다(String startAt) throws Exception {
        // given
        Map<String, Object> request = reservationTimeRequestBody(startAt);

        // when
        ResultActions result = mockMvc.perform(post("/admin/times")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)));

        // then
        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").exists());
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
