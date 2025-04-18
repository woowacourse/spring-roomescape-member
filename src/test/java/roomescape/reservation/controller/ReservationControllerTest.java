package roomescape.reservation.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import roomescape.reservation.dto.ReservationRequestDto;
import roomescape.reservation.entity.Reservation;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.reservation.repository.ReservationRepositoryImpl;

class ReservationControllerTest {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private ReservationRepository reservationRepository;
    private MockMvc mockMvc;

    @BeforeEach
    void init() {
        reservationRepository = new ReservationRepositoryImpl();
        ReservationController reservationController = new ReservationController(reservationRepository);
        mockMvc = MockMvcBuilders.standaloneSetup(reservationController).build();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @DisplayName("모든 예약 정보를 가져온다.")
    @Test
    void test1() throws Exception {
        // given
        LocalDateTime now = LocalDateTime.now();

        List<Reservation> reservations = List.of(
                new Reservation(1, "꾹", now),
                new Reservation(2, "꾹", now),
                new Reservation(3, "꾹", now)
        );

        for (Reservation reservation : reservations) {
            reservationRepository.save(reservation);
        }

        // when & then
        mockMvc.perform(get("/reservations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$[0].name").value("꾹"));
    }

    @DisplayName("예약 정보를 추가한다.")
    @ParameterizedTest
    @CsvSource(value = {"꾹,2025-04-17,10:00"}, delimiter = ',')
    void test2(String name, String date, String time) throws Exception {
        // given
        LocalDate localDate = LocalDate.parse(date);
        LocalTime localTime = LocalTime.parse(time);

        ReservationRequestDto requestDto = new ReservationRequestDto(name, localDate, localTime);
        String json = objectMapper.writeValueAsString(requestDto);

        // when & then
        mockMvc.perform(post("/reservations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
        ).andExpect(status().isOk());
    }

    @DisplayName("예약 정보를 삭제한다.")
    @Test
    void test3() throws Exception {
        // given
        long id = 1L;
        Reservation reservation = new Reservation(id, "꾹", LocalDateTime.now());
        reservationRepository.save(reservation);

        // when & then

        mockMvc.perform(delete("/reservations/{id}", id))
                .andExpect(status().isNoContent());
    }

    @DisplayName("삭제할 예약 정보가 없다면 404 에러 코드를 반환한다")
    @Test
    void test4() throws Exception {
        // given
        long id = 1L;

        // when & then
        mockMvc.perform(delete("/reservations/{id}", id))
                .andExpect(status().isNotFound());
    }

}
