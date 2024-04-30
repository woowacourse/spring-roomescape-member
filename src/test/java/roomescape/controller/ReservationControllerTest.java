package roomescape.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import roomescape.controller.request.ReservationRequest;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.fake.InMemoryReservationRepository;
import roomescape.fake.InMemoryReservationTimeRepository;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(value = {InMemoryReservationRepository.class,
        InMemoryReservationTimeRepository.class})
@WebMvcTest(ReservationController.class)
class ReservationControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ReservationTimeRepository reservationTimeRepository;

    @DisplayName("예약 목록 조회 요청 시, 200 OK를 응답한다")
    @Test
    void findAll() throws Exception {
        this.mvc.perform(get("/reservations"))
                .andExpect(status().isOk());
    }

    @DisplayName("예약 추가 요청 시, 201 Created를 응답한다")
    @Test
    void add() throws Exception {
        reservationTimeRepository.save(new ReservationTime(LocalTime.of(9, 0)));

        String requestBody = objectMapper.writeValueAsString(new ReservationRequest(
                "비밥",
                LocalDate.now().plusDays(1),
                1L));

        this.mvc.perform(post("/reservations")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @DisplayName("존재하는 예약에 대한 삭제 요청 시, 204 No Content를 응답한다")
    @Test
    void delete_() throws Exception {
        Reservation savedReservation = reservationRepository.save(new Reservation(
                "비밥",
                LocalDate.now().plusDays(1),
                LocalTime.of(9, 0)));

        this.mvc.perform(delete("/reservations/" + savedReservation.getId()))
                .andExpect(status().isNoContent());
    }

    @DisplayName("존재하지 않는 예약에 대한 삭제 요청 시, 204 No Content를 응답한다")
    @Test
    void delete_notExist() throws Exception {
        Reservation savedReservation = reservationRepository.save(new Reservation(
                "비밥",
                LocalDate.now().plusDays(1),
                LocalTime.of(9, 0)));

        this.mvc.perform(delete("/reservations/" + (1 + savedReservation.getId())))
                .andExpect(status().isNoContent());
    }
}
