package roomescape.date.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.date.dto.response.ReservationDateDetailDto;
import roomescape.date.service.ReservationDateService;

@RestController
@RequestMapping("/member")
public class ReservationDateController {

    private final ReservationDateService reservationDateService;

    public ReservationDateController(ReservationDateService reservationDateService) {
        this.reservationDateService = reservationDateService;
    }

    @GetMapping("/dates")
    public ResponseEntity<List<ReservationDateDetailDto>> getReservationDates() {
        List<ReservationDateDetailDto> responseData = reservationDateService.readDatesAfterToday().stream()
                .map(ReservationDateDetailDto::from)
                .toList();
        return ResponseEntity.ok(responseData);
    }

}
