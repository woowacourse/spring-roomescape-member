package roomescape.date.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.date.domain.ReservationDate;
import roomescape.date.dto.ReservationDateDetailDto;
import roomescape.date.dto.ReservationDateSaveDto;
import roomescape.date.service.ReservationDateService;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class ReservationDateAdminController {

    private final ReservationDateService reservationDateService;

    public ReservationDateAdminController(ReservationDateService reservationDateService) {
        this.reservationDateService = reservationDateService;
    }

    @GetMapping("/dates")
    public ResponseEntity<List<ReservationDateDetailDto>> getReservationDates() {
        List<ReservationDateDetailDto> responseData = reservationDateService.readDates().stream()
                .map(ReservationDateDetailDto::from)
                .toList();
        return ResponseEntity.ok(responseData);
    }

    @PostMapping("/dates")
    public ResponseEntity<ReservationDateDetailDto> createReservationDate(
            @RequestBody ReservationDateSaveDto dto
    ) {
        ReservationDate reservationDate = reservationDateService.register(dto.date());
        ReservationDateDetailDto responseData = ReservationDateDetailDto.from(reservationDate);
        return ResponseEntity.ok(responseData);
    }

    @DeleteMapping("/dates/{id}")
    public ResponseEntity<ReservationDateDetailDto> deleteReservationDate(@PathVariable Long id) {
        ReservationDate reservationDate = reservationDateService.deregister(id);
        ReservationDateDetailDto responseData = ReservationDateDetailDto.from(reservationDate);
        return ResponseEntity.ok(responseData);
    }

}
