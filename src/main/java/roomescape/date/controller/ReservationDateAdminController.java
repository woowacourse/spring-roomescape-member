package roomescape.date.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import roomescape.date.domain.ReservationDate;
import roomescape.date.dto.request.ReservationDateSaveDto;
import roomescape.date.dto.request.ReservationDateStatusUpdateDto;
import roomescape.date.dto.response.ReservationDateDetailDto;
import roomescape.date.service.ReservationDateService;

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
    public ResponseEntity<ReservationDateDetailDto> create(
            @Validated @RequestBody ReservationDateSaveDto dto
    ) {
        ReservationDate reservationDate = reservationDateService.register(dto.date());
        ReservationDateDetailDto responseData = ReservationDateDetailDto.from(reservationDate);
        return ResponseEntity.ok(responseData);
    }

    @DeleteMapping("/dates/{id}")
    public ResponseEntity<ReservationDateDetailDto> delete(@PathVariable Long id) {
        ReservationDate reservationDate = reservationDateService.deregister(id);
        ReservationDateDetailDto responseData = ReservationDateDetailDto.from(reservationDate);
        return ResponseEntity.ok(responseData);
    }

    @PatchMapping("/dates/{id}/status")
    public ResponseEntity<ReservationDateDetailDto> updateStatus(
            @PathVariable Long id, @RequestBody ReservationDateStatusUpdateDto dto) {
        ReservationDate reservationDate = reservationDateService.updateStatus(id, dto.isActive());
        ReservationDateDetailDto responseData = ReservationDateDetailDto.from(reservationDate);
        return ResponseEntity.ok(responseData);
    }

}
