package roomescape.reservationtime.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
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
import roomescape.reservationtime.dto.request.CreateReservationTimeRequest;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
class ReservationTimeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Nested
    class createReservationTime {

        @Test
        @DisplayName("예약 시간 생성 요청 시 201 상태와 Location 헤더에 생성된 리소스의 위치를 반환한다.")
        void createReservationTime() throws Exception {
            mockMvc.perform(MockMvcRequestBuilders.post("/times")
                            .content(objectMapper.writeValueAsString(new CreateReservationTimeRequest(LocalTime.of(10, 00))))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpectAll(
                            status().isCreated(),
                            header().stringValues("Location", "/times/7")
                    );
        }

        @ParameterizedTest
        @NullSource
        @DisplayName("예약 시간 생성 요청 시 예약 시간이 공백인 경우 경우 404를 반환한다.")
        void createReservationTime_WhenTimeInNull(LocalTime startAt) throws Exception {
            mockMvc.perform(MockMvcRequestBuilders.post("/times")
                            .content(objectMapper.writeValueAsString(new CreateReservationTimeRequest(startAt)))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpectAll(
                            status().isBadRequest(),
                            jsonPath("$").value("예약 시간은 공백 문자가 불가능합니다.")
                    );
        }
    }

    @Test
    @DisplayName("예약 시간 조회 요청 성공 시 200과 해당 정보를 반환한다.")
    void getReservationTimes() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/times")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(jsonPath("$[0].id").value(1),
                        jsonPath("$[0].startAt").value("10:00"),
                        jsonPath("$[1].id").value(2),
                        jsonPath("$[1].startAt").value("12:00"),
                        jsonPath("$[2].id").value(3),
                        jsonPath("$[2].startAt").value("14:00"),
                        status().isOk()
                );
    }

    @Nested
    class getReservationTime {

        @Test
        @DisplayName("예약 단건 조회 요청 성공 시 200과 해당 정보를 반환한다.")
        void getReservationTime() throws Exception {
            mockMvc.perform(MockMvcRequestBuilders.get("/times/1")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpectAll(
                            jsonPath("$.id").value(1),
                            jsonPath("$.startAt").value("10:00"),
                            status().isOk()
                    );
        }

        @Test
        @DisplayName("예약 시간 단건 조회 요청 시 존재하지 않는 시간인 경우 404를 반환한다.")
        void getReservationTime_WhenReservationTimeNotExists() throws Exception {
            mockMvc.perform(MockMvcRequestBuilders.get("/times/1000")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpectAll(
                            status().isNotFound(),
                            jsonPath("$").value("조회하려는 예약 시간이 존재하지 않습니다.")
                    );
        }
    }

    @Nested
    class deleteReservationTime {

        @Test
        @DisplayName("예약 시간 삭제 요청 성공 시 204 상태 코드를 반환한다.")
        void deleteReservationTime() throws Exception {
            mockMvc.perform(MockMvcRequestBuilders.delete("/times/6"))
                    .andExpect(status().isNoContent());
        }

        @Test
        @DisplayName("예약 시간 삭제 요청 시 해당 시간이 존재하지 않는 경우 404 상태 코드를 반환한다.")
        void deleteReservationTime_isNotFound() throws Exception {
            mockMvc.perform(MockMvcRequestBuilders.delete("/times/500"))
                    .andExpectAll(
                            status().isNotFound(),
                            jsonPath("$").value("삭제하려는 예약 시간이 존재하지 않습니다.")
                    );
        }

        @Test
        @DisplayName("예약 시간 삭제 요청 시 해당 시간인 예약이 존재할 경우 409 상태 코드를 반환한다.")
        void deleteReservationTime_isConflict() throws Exception {
            mockMvc.perform(MockMvcRequestBuilders.delete("/times/3"))
                    .andExpectAll(
                            status().isConflict(),
                            jsonPath("$").value("삭제하려는 시간을 사용 중인 예약이 존재합니다.")
                    );
        }
    }
}
