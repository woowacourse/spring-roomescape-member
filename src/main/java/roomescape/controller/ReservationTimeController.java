package roomescape.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import roomescape.dto.ReservationTimeResponseDTO;
import roomescape.service.ReservationTimeService;

@Controller
@RequestMapping("/times")
public class ReservationTimeController {

    private final ReservationTimeService reservationTimeService;

    public ReservationTimeController(ReservationTimeService reservationTimeService) {
        this.reservationTimeService = reservationTimeService;
    }

    @GetMapping
    @ResponseBody
    public ResponseEntity<List<ReservationTimeResponseDTO>> readReservationTime() {
        return ResponseEntity.ok(reservationTimeService.findAllReservationTime());
    }
}
