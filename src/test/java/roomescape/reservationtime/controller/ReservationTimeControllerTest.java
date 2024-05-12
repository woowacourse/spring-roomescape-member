package roomescape.reservationtime.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalTime;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import roomescape.reservationtime.dto.request.CreateReservationTimeRequest;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Sql(scripts = {"/delete-data.sql", "/data.sql"})
class ReservationTimeControllerTest  {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createReservationTime() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/times")
                        .content(objectMapper.writeValueAsString(new CreateReservationTimeRequest(LocalTime.of(10, 0))))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(header().stringValues("Location", "/times/7"));
    }

    @Test
    void getReservationTimes() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/times")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].startAt").value("10:00"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].startAt").value("12:00"))
                .andExpect(jsonPath("$[2].id").value(3))
                .andExpect(jsonPath("$[2].startAt").value("14:00"))
                .andExpect(status().isOk());
    }

    @Test
    void getReservationTime() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/times/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.startAt").value("10:00"))
                .andExpect(status().isOk());
    }

    @Test
    void deleteReservationTime() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/times/5"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteReservationTime_isConflict() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/times/1"))
                .andExpect(status().isConflict());
    }
}
