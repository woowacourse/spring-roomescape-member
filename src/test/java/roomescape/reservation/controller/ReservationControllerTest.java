package roomescape.reservation.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import roomescape.reservation.dto.request.CreateReservationRequest;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
class ReservationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Nested
    class createReservation {

        @Test
        @DisplayName("예약 생성 요청 시 201 상태와 Location 헤더에 생성된 리소스의 위치를 반환한다.")
        void createReservation() throws Exception {
            mockMvc.perform(MockMvcRequestBuilders.post("/reservations")
                            .content(objectMapper.writeValueAsString(new CreateReservationRequest(
                                    LocalDate.of(3000, 1, 1), "포비", 1L, 1L)))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpectAll(
                            status().isCreated(),
                            header().stringValues("Location", "/reservations/14")
                    );
        }

        @ParameterizedTest
        @NullAndEmptySource
        @DisplayName("예약 생성 요청 시 예약자 명이 공백인 경우 경우 404를 반환한다.")
        void createReservation_WhenUserNameInBlank(String name) throws Exception {
            mockMvc.perform(MockMvcRequestBuilders.post("/reservations")
                            .content(objectMapper.writeValueAsString(new CreateReservationRequest(
                                    LocalDate.of(3000, 1, 1), name, 1L, 10L)))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$").value("예약자 명은 공백 문자가 불가능합니다."));
        }

        @Test
        @DisplayName("예약 생성 요청 시 예약자 명이 255자 초과인 경우 404를 반환한다.")
        void createReservation_WhenUserNameOverLength() throws Exception {
            mockMvc.perform(MockMvcRequestBuilders.post("/reservations")
                            .content(objectMapper.writeValueAsString(new CreateReservationRequest(
                                    LocalDate.of(3000, 1, 1), "a".repeat(256), 1L, 10L)))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$").value("예약자 명은 최대 255자까지 입력이 가능합니다."));
        }

        @ParameterizedTest
        @ValueSource(longs = {0, -1})
        @DisplayName("예약 생성 요청 시 예약 시간 식별자가 음수인 경우 404를 반환한다.")
        void createReservation_WhenTimeIdIsNegativeOrZero(Long timeId) throws Exception {
            mockMvc.perform(MockMvcRequestBuilders.post("/reservations")
                            .content(objectMapper.writeValueAsString(new CreateReservationRequest(
                                    LocalDate.of(3000, 1, 1), "비밥", timeId, 10L)))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$").value("예약 시간 식별자는 양수만 가능합니다."));
        }

        @ParameterizedTest
        @ValueSource(longs = {0, -1})
        @DisplayName("예약 생성 요청 시 테마 시간 식별자가 음수인 경우 404를 반환한다.")
        void createReservation_WhenThemeIdIsNegativeOrZero(Long themeId) throws Exception {
            mockMvc.perform(MockMvcRequestBuilders.post("/reservations")
                            .content(objectMapper.writeValueAsString(new CreateReservationRequest(
                                    LocalDate.of(3000, 1, 1), "비밥", 1L, themeId)))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$").value("예약 테마 식별자는 양수만 가능합니다."));
        }
    }

    @Test
    void getReservations() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/reservations")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[0].id").value(1))
                .andExpect(jsonPath("$.[0].name").value("아서"))
                .andExpect(jsonPath("$.[0].date").value("2024-04-23"))
                .andExpect(jsonPath("$.[0].time.id").value(1))
                .andExpect(jsonPath("$.[0].time.startAt").value("10:00"))

                .andExpect(jsonPath("$.[1].id").value(2))
                .andExpect(jsonPath("$.[1].name").value("몰리"))
                .andExpect(jsonPath("$.[1].date").value("2024-04-24"))
                .andExpect(jsonPath("$.[1].time.id").value(2))
                .andExpect(jsonPath("$.[1].time.startAt").value("12:00"))
                .andExpect(status().isOk());
    }

    @Test
    void getReservation() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/reservations/2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.name").value("몰리"))
                .andExpect(jsonPath("$.date").value("2024-04-24"))
                .andExpect(jsonPath("$.time.id").value(2))
                .andExpect(jsonPath("$.time.startAt").value("12:00"))
                .andExpect(status().isOk());

    }

    @Test
    void getAvailableTimes() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/reservations/times?date=2024-04-24&themeId=1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[0].id").value(1))
                .andExpect(jsonPath("$.[0].startAt").value("10:00"))
                .andExpect(jsonPath("$.[0].alreadyBooked").value(false))

                .andExpect(jsonPath("$.[1].id").value(2))
                .andExpect(jsonPath("$.[1].startAt").value("12:00"))
                .andExpect(jsonPath("$.[1].alreadyBooked").value(true))
                .andExpect(status().isOk());
    }

    @Test
    void deleteReservation() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/reservations/1"))
                .andExpect(status().isNoContent());
    }
}
