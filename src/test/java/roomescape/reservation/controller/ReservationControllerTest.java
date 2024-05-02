package roomescape.reservation.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
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
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
class ReservationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createReservation() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/reservations")
                        .content(objectMapper.writeValueAsString(new CreateReservationRequest(
                                LocalDate.of(3000, 1, 1), "포비", 1L, 1L)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(header().stringValues("Location", "/reservations/14"));
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
