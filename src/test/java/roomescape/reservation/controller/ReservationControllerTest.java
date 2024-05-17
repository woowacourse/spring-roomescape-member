package roomescape.reservation.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import java.time.LocalDate;
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
import roomescape.member.model.Member;
import roomescape.member.model.Role;
import roomescape.reservation.dto.request.CreateReservationUserRequest;
import roomescape.util.JwtTokenHelper;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Sql(scripts = {"/delete-data.sql", "/data.sql"})
class ReservationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JwtTokenHelper jwtTokenHelper;

    @Test
    void createReservation() throws Exception {
        final String token = jwtTokenHelper.createToken(new Member(1L, "name", "email", "pw", Role.USER));

        mockMvc.perform(MockMvcRequestBuilders.post("/reservations")
                        .content(objectMapper.writeValueAsString(new CreateReservationUserRequest(
                                LocalDate.of(3000, 1, 1), 1L, 1L)))
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(new Cookie("token", token)))
                .andExpect(status().isCreated())
                .andExpect(header().stringValues("Location", "/reservations/4"));
    }

    @Test
    void getReservations() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/reservations")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[0].id").value(1))
                .andExpect(jsonPath("$.[0].member.name").value("마크"))
                .andExpect(jsonPath("$.[0].date").value("2025-01-01"))
                .andExpect(jsonPath("$.[0].time.id").value(1))
                .andExpect(jsonPath("$.[0].time.startAt").value("10:00"))

                .andExpect(jsonPath("$.[1].id").value(2))
                .andExpect(jsonPath("$.[1].member.name").value("러너덕"))
                .andExpect(jsonPath("$.[1].date").value("2025-01-02"))
                .andExpect(jsonPath("$.[1].time.id").value(2))
                .andExpect(jsonPath("$.[1].time.startAt").value("12:00"))
                .andExpect(status().isOk());
    }

    @Test
    void getReservation() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/reservations/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.member.id").value(1))
                .andExpect(jsonPath("$.date").value("2025-01-01"))
                .andExpect(jsonPath("$.time.id").value(1))
                .andExpect(jsonPath("$.time.startAt").value("10:00"))
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
                .andExpect(jsonPath("$.[1].alreadyBooked").value(false))
                .andExpect(status().isOk());
    }

    @Test
    void deleteReservation() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/reservations/1"))
                .andExpect(status().isNoContent());
    }
}
