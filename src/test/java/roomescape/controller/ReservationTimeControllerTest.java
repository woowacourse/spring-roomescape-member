package roomescape.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ReservationTimeControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("새로운 예약 시간을 생성하면 201 상태코드와 생성된 데이터를 반환한다")
    void addReservationTime() throws Exception {
        Map<String, String> params = Map.of("startAt", "15:30");

        mockMvc.perform(post("/times")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(params)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value(201))
                .andExpect(jsonPath("$.data.startAt").value("15:30"));
    }

    @Test
    @DisplayName("새로운 예약 시간 생성에서 시작 시간 형식이 HH:mm이 아니면 400 에러를 반환한다")
    void addReservationTimeWithInvalidFormat() throws Exception {
        Map<String, String> params = Map.of("startAt", "3:30 PM");

        mockMvc.perform(post("/times")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(params)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorMessage").value("시작 시간은 24시 형식의 HH:mm 여야 합니다."));
    }

    @Test
    @DisplayName("새로운 예약 시간 생성에서 시작 시간이 누락되면 400 에러를 반환한다")
    void addReservationTimeWithBlank() throws Exception {
        Map<String, String> params = Map.of("startAt", "");

        mockMvc.perform(post("/times")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(params)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorMessage").value("시작 시간은 반드시 포함되어야 합니다."));
    }

    @Test
    @DisplayName("예약 시간을 삭제하면 204 상태코드를 반환한다")
    void deleteReservationTime() throws Exception {
        long id = createTimeAndGetId("10:00");

        mockMvc.perform(delete("/times/" + id))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("예약에 이미 사용 중인 시간을 삭제하려고 하면 409 에러를 반환한다")
    void deleteReservationTimeInUse() throws Exception {
        mockMvc.perform(delete("/times/1"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.errorMessage").value("해당 시간을 참조하는 예약 데이터가 존재하기 때문에 삭제할 수 없습니다."));
    }

    @Test
    @DisplayName("모든 예약 시간 목록을 조회하면 200 상태코드를 반환한다")
    void getReservationTimes() throws Exception {
        mockMvc.perform(get("/times"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.data").isArray());
    }

    @Test
    @DisplayName("날짜와 테마 ID로 예약 가능한 시간을 조회한다")
    void getAvailableReservationTimes() throws Exception {
        mockMvc.perform(get("/times/availability")
                        .param("date", "2026-05-13")
                        .param("themeId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].isAvailable").exists());
    }

    @Test
    @DisplayName("예약 가능 시간 조회에서 날짜 형식이 잘못되면 400 에러를 반환한다")
    void getAvailableReservationTimesWithInvalidDate() throws Exception {
        mockMvc.perform(get("/times/availability")
                        .param("date", "2026/05/13")
                        .param("themeId", "1"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorMessage").value("날짜는 YYYY-MM-DD 형식이여야 합니다."));
    }

    private long createTimeAndGetId(String startAt) throws Exception {
        Map<String, String> params = Map.of("startAt", startAt);
        MvcResult result = mockMvc.perform(post("/times")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(params)))
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        return objectMapper.readTree(responseBody).get("data").get("id").asLong();
    }
}