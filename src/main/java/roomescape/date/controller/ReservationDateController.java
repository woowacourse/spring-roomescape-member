package roomescape.date.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.date.dto.ReservationDateDetailDto;
import roomescape.date.service.ReservationDateService;

import java.util.List;

@RestController
@RequestMapping("/member")
public class ReservationDateController {

    private final ReservationDateService reservationDateService;

    public ReservationDateController(ReservationDateService reservationDateService) {
        this.reservationDateService = reservationDateService;
    }

    @GetMapping("/dates")
    public ResponseEntity<List<ReservationDateDetailDto>> getReservationDates() {
        List<ReservationDateDetailDto> responseData = reservationDateService.readDates().stream()
                .map(ReservationDateDetailDto::from)
                .toList();
        return ResponseEntity.ok(responseData);
    }

}
