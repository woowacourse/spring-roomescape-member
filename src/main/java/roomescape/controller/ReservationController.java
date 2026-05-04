package roomescape.controller;

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
import roomescape.dto.ReservationRequestDTO;
import roomescape.dto.ReservationResponseDTO;
import roomescape.service.ReservationService;

@Controller
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping
    @ResponseBody
    public List<ReservationResponseDTO> readAll() {
        return reservationService.readAllReservation();
    }

    @GetMapping("/{id}")
    @ResponseBody
    public List<ReservationResponseDTO> readByTimeId(@PathVariable Long timeId) {
        return reservationService.readReservationByTimeId(timeId);
    }

    @PostMapping
    @ResponseBody
    public ResponseEntity<ReservationResponseDTO> add(
            @RequestBody ReservationRequestDTO request) {
        ReservationResponseDTO saved = reservationService.addReservation(request);
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        reservationService.deleteReservation(id);
        return ResponseEntity.ok().build();
    }
}
