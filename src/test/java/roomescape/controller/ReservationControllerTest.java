package roomescape.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ReservationControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private String setupDate;

    @BeforeEach
    void setUp() {
        setupDate = LocalDate.now().plusDays(1).toString();
        jdbcTemplate.update("MERGE INTO reservation_time (id, start_at) KEY(id) VALUES (1, '10:00')");
        jdbcTemplate.update("MERGE INTO theme (id, name, description, image_url) KEY(id) VALUES (1, '테마', '설명', 'url')");
        jdbcTemplate.update("MERGE INTO reservation (id, name, date, time_id, theme_id) KEY(id) VALUES (2, '홍길동', ?, 1, 1)", setupDate);
    }

    @Test
    @DisplayName("예약 목록을 조회하면 200 상태코드와 목록을 반환한다")
    void getReservations() throws Exception {
        mockMvc.perform(get("/reservations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    @DisplayName("이름 필터를 사용하여 예약 목록을 조회한다")
    void getReservationsByName() throws Exception {
        mockMvc.perform(get("/reservations")
                        .param("name", "홍길동"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("홍길동"));
    }

    @Test
    @DisplayName("새로운 예약을 생성하면 201 상태코드와 생성된 데이터를 반환한다")
    void addReservation() throws Exception {
        String newReservationDate = LocalDate.now().plusDays(2).toString();

        Map<String, Object> params = Map.of(
                "name", "브라운",
                "date", newReservationDate,
                "timeId", 1L,
                "themeId", 1L
        );

        mockMvc.perform(post("/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(params)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("브라운"));
    }

    @Test
    @DisplayName("새로 예약을 생성할 때 중복된 날짜, 시간, 테마로 예약하면 409 에러를 반환한다")
    void addDuplicatedReservation() throws Exception {
        Map<String, Object> params = Map.of(
                "name", "새로운예약자",
                "date", setupDate,
                "timeId", 1L,
                "themeId", 1L
        );

        mockMvc.perform(post("/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(params)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.errorMessage").value("해당 날짜, 시간, 테마의 예약이 존재하여 예약할 수 없습니다."));
    }

    @Test
    @DisplayName("성공적으로 예약을 삭제하면 204 No Content를 반환한다")
    void deleteReservation() throws Exception {
        String encodedName = URLEncoder.encode("홍길동", StandardCharsets.UTF_8);

        mockMvc.perform(delete("/reservations/2")
                        .header("name", encodedName))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("존재하지 않는 ID의 예약 삭제 시 404를 반환한다")
    void deleteNonExistentReservation() throws Exception {
        mockMvc.perform(delete("/reservations/9999")
                        .header("name", "홍길동"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("예약 삭제 시 헤더의 이름과 예약자 이름이 다르면 401을 반환한다")
    void deleteReservationUnauthorized() throws Exception {
        mockMvc.perform(delete("/reservations/2")
                        .header("name", "다른사람"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.errorMessage").value("해당 예약을 삭제할 권한이 없습니다."));
    }

    @Test
    @DisplayName("예약을 성공적으로 수정하면 204 No Content를 반환한다")
    void updateReservation() throws Exception {
        String nextDate = LocalDate.now().plusDays(10).toString();
        Map<String, Object> updateParams = Map.of(
                "id", 2L,
                "name", "홍길동",
                "date", nextDate,
                "timeId", 1L,
                "themeId", 1L
        );

        String encodedName = URLEncoder.encode("홍길동", StandardCharsets.UTF_8);

        mockMvc.perform(post("/reservations/2")
                        .header("name", encodedName)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateParams)))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("수정하려는 시간대에 이미 다른 예약이 존재하면 409 에러를 반환한다")
    void updateReservationToDuplicatedTime() throws Exception {
        Map<String, Object> updateParams = Map.of(
                "id", 2L,
                "name", "홍길동",
                "date", setupDate,
                "timeId", 1L,
                "themeId", 1L
        );

        mockMvc.perform(post("/reservations/2")
                        .header("name", "홍길동")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateParams)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.errorMessage").value("해당 날짜, 시간, 테마의 예약이 존재하여 예약할 수 없습니다."));
    }

    @Test
    @DisplayName("예약 수정 시 헤더의 이름과 예약자 이름이 다르면 401을 반환한다")
    void updateReservationUnauthorized() throws Exception {
        Map<String, Object> updateParams = Map.of(
                "id", 2L,
                "name", "홍길동",
                "date", LocalDate.now().plusDays(3).toString(),
                "timeId", 1L,
                "themeId", 1L
        );

        mockMvc.perform(post("/reservations/2")
                        .header("name", "임꺽정")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateParams)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.errorMessage").value("해당 예약을 수정할 권한이 없습니다."));
    }
}