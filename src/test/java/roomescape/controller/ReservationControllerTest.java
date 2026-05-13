package roomescape.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
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
class ReservationControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("새로운 예약을 생성하면 201 상태코드와 생성된 데이터를 반환한다")
    void addReservation() throws Exception {
        Map<String, Object> params = Map.of(
                "name", "브라운",
                "date", LocalDate.now().plusDays(1).toString(),
                "timeId", 1L,
                "themeId", 1L
        );

        mockMvc.perform(post("/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(params)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value(201))
                .andExpect(jsonPath("$.data.name").value("브라운"));
    }

    @Test
    @DisplayName("새로 예약을 생성할 때, 날짜 형식이 YYYY-MM-DD가 아니면 400 에러를 반환한다")
    void addReservationWithInvalidDateFormat() throws Exception {
        Map<String, Object> params = Map.of(
                "name", "브라운",
                "date", "2026/05/13",
                "timeId", 1L,
                "themeId", 1L
        );

        mockMvc.perform(post("/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(params)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorMessage").value("날짜는 YYYY-MM-DD 형식이여야 합니다."));
    }

    @Test
    @DisplayName("새로 예약을 생성할 때 이름이 공백이면 400 에러를 반환한다")
    void addReservationWithBlankName() throws Exception {
        Map<String, Object> params = Map.of(
                "name", "  ",
                "date", LocalDate.now().plusDays(1).toString(),
                "timeId", 1L,
                "themeId", 1L
        );

        mockMvc.perform(post("/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(params)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorMessage").value("이름이 반드시 포함되어야 합니다."));
    }

    @Test
    @DisplayName("새로 예약을 생성할 때 중복된 날짜, 시간, 테마로 예약하면 400 에러를 반환한다")
    void addDuplicatedReservation() throws Exception {
        String date = LocalDate.now().plusDays(2).toString();
        createReservationAndGetId("기존예약자", LocalDate.parse(date));

        Map<String, Object> params = Map.of(
                "name", "새로운예약자",
                "date", date,
                "timeId", 1L,
                "themeId", 1L
        );

        mockMvc.perform(post("/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(params)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorMessage").value("해당 날짜, 시간, 테마의 예약이 존재하여 예약할 수 없습니다."));
    }

    @Test
    @DisplayName("새로 예약 생성할 때 존재하지 않는 테마 ID로 예약하면 404 에러를 반환한다")
    void addReservationWithInvalidTheme() throws Exception {
        Map<String, Object> params = Map.of(
                "name", "브라운",
                "date", LocalDate.now().plusDays(1).toString(),
                "timeId", 1L,
                "themeId", 9999L
        );

        mockMvc.perform(post("/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(params)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorMessage").value("존재하지 않은 테마 id입니다."));
    }

    @Test
    @DisplayName("미래 날짜의 예약을 삭제하면 204 상태코드를 반환한다")
    void deleteReservation() throws Exception {
        long id = createReservationAndGetId("삭제유저", LocalDate.now().plusDays(1));

        mockMvc.perform(delete("/reservations/" + id)
                        .header("name", "삭제유저"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("존재하지 않는 ID의 예약 삭제 시 404를 반환한다")
    void deleteNonExistentReservation() throws Exception {
        mockMvc.perform(delete("/reservations/9999")
                        .header("name", "홍길동"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorMessage").value("존재하지 않는 예약 id입니다."));
    }

    @Test
    @DisplayName("예약 삭제 시 헤더의 이름과 예약자 이름이 다르면 401을 반환한다")
    void deleteReservationUnauthorized() throws Exception {
        long id = createReservationAndGetId("홍길동", LocalDate.now().plusDays(1));

        mockMvc.perform(delete("/reservations/" + id)
                        .header("name", "테스트"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.errorMessage").value("해당 예약을 삭제할 권한이 없습니다."));
    }

    @Test
    @DisplayName("이미 지난 날짜의 예약을 삭제하려고 하면 400 에러를 반환한다")
    void deletePastReservation() throws Exception {
        mockMvc.perform(delete("/reservations/2")
                        .header("name", "홍길동"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorMessage").value("이미 지난 예약은 삭제할 수 없습니다."));
    }

    @Test
    @DisplayName("수정하려는 시간대에 이미 다른 예약이 존재하면 400 에러를 반환한다")
    void updateReservationToDuplicatedTime() throws Exception {
        String date = LocalDate.now().plusDays(3).toString();
        long myId = createReservationAndGetId("나", LocalDate.now().plusDays(1));
        createReservationAndGetId("타인", LocalDate.parse(date));

        Map<String, Object> updateParams = Map.of(
                "id", myId,
                "name", "나",
                "date", date,
                "timeId", 1L,
                "themeId", 1L
        );

        mockMvc.perform(post("/reservations/" + myId)
                        .header("name", "나")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateParams)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorMessage").value("해당 날짜, 테마, 시간으로 이미 존재하는 예약이 있습니다."));
    }

    @Test
    @DisplayName("예약 수정 시 헤더의 이름과 예약자 이름이 다르면 401을 반환한다")
    void updateReservationUnauthorized() throws Exception {
        long id = createReservationAndGetId("홍길동", LocalDate.now().plusDays(5));

        Map<String, Object> updateParams = Map.of(
                "id", id,
                "name", "홍길동",
                "date", LocalDate.now().plusDays(6).toString(),
                "timeId", 1L,
                "themeId", 1L
        );

        mockMvc.perform(post("/reservations/" + id)
                        .header("name", "임꺽정")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateParams)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.errorMessage").value("해당 예약을 수정할 권한이 없습니다."));
    }

    @Test
    @DisplayName("기존 정보와 동일하게 수정하면 400 에러를 반환한다")
    void updateReservationWithSameValue() throws Exception {
        String futureDate = LocalDate.now().plusDays(10).toString();
        long id = createReservationAndGetId("홍길동", LocalDate.parse(futureDate));

        Map<String, Object> updateParams = Map.of(
                "id", id,
                "name", "홍길동",
                "date", futureDate,
                "timeId", 1L,
                "themeId", 1L
        );

        mockMvc.perform(post("/reservations/" + id)
                        .header("name", "홍길동")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateParams)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorMessage").value("기존 정보와 동일하여 수정할 내용이 없습니다."));
    }

    @Test
    @DisplayName("요청 바디에 예약 ID가 누락되면 400 에러를 반환한다")
    void updateReservationWithoutId() throws Exception {
        Map<String, Object> params = Map.of(
                "name", "홍길동",
                "date", LocalDate.now().plusDays(1).toString(),
                "timeId", 1L,
                "themeId", 1L
        );

        mockMvc.perform(post("/reservations/1")
                        .header("name", "홍길동")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(params)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorMessage").value("예약 ID가 반드시 포함되어야 합니다."));
    }

    @Test
    @DisplayName("이름으로 예약을 조회하면 공통 응답 규격을 반환한다")
    void getReservations() throws Exception {
        mockMvc.perform(get("/reservations")
                        .param("name", "홍길동")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.data").isArray());
    }

    private long createReservationAndGetId(String name, LocalDate date) throws Exception {
        Map<String, Object> params = Map.of(
                "name", name,
                "date", date.toString(),
                "timeId", 1L,
                "themeId", 1L
        );

        MvcResult result = mockMvc.perform(post("/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(params)))
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        return objectMapper.readTree(responseBody).get("data").get("id").asLong();
    }
}