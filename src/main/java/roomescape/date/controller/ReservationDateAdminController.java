package roomescape.date.controller;

import static org.springframework.http.HttpStatus.CREATED;

import jakarta.validation.Valid;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.date.domain.ReservationDate;
import roomescape.date.dto.request.ReservationDateSaveDto;
import roomescape.date.dto.response.ReservationDateDetailDto;
import roomescape.date.service.ReservationDateService;

@Slf4j
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
            @Valid @RequestBody ReservationDateSaveDto dto
    ) {
        ReservationDate reservationDate = reservationDateService.register(dto.date());
        ReservationDateDetailDto responseData = ReservationDateDetailDto.from(reservationDate);
        return ResponseEntity.status(CREATED).body(responseData);
    }

    @DeleteMapping("/dates/{id}")
    public ResponseEntity<ReservationDateDetailDto> deleteReservationDate(@PathVariable Long id) {
        ReservationDate reservationDate = reservationDateService.deregister(id);
        ReservationDateDetailDto responseData = ReservationDateDetailDto.from(reservationDate);
        return ResponseEntity.ok(responseData);
    }
}
