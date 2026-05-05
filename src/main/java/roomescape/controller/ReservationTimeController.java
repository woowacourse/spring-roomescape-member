package roomescape.controller;

import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import roomescape.dto.ReservationTimeRequestDTO;
import roomescape.dto.ReservationTimeResponseDTO;
import roomescape.service.ReservationTimeService;

@Controller
@RequestMapping("/times")
public class ReservationTimeController {

    private final ReservationTimeService reservationTimeService;

    public ReservationTimeController(ReservationTimeService reservationTimeService) {
        this.reservationTimeService = reservationTimeService;
    }

    @PostMapping
    @ResponseBody
    public ResponseEntity<ReservationTimeResponseDTO> add(
            @RequestBody ReservationTimeRequestDTO request) {
        ReservationTimeResponseDTO saved = reservationTimeService.addReservationTime(request);
        return ResponseEntity.created(URI.create("/time/" + saved.id())).build();
    }

    @GetMapping
    @ResponseBody
    public List<ReservationTimeResponseDTO> readReservationTime() {
        return reservationTimeService.findAllReservationTime();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservationTime(@PathVariable Long id) {
        reservationTimeService.deleteReservationTime(id);
        return ResponseEntity.ok().build();
    }
}
