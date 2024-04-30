package roomescape.time.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.response.ResponseCode;
import roomescape.time.dto.ReservationTimeRequestDto;
import roomescape.time.dto.ReservationTimeResponseDto;
import roomescape.time.service.ReservationTimeService;

import java.util.List;

@RestController
@RequestMapping("/times")
public class ReservationTimeController {

    private final ReservationTimeService reservationTimeService;

    public ReservationTimeController(final ReservationTimeService reservationTimeService) {
        this.reservationTimeService = reservationTimeService;
    }

    @PostMapping
    public ReservationTimeResponseDto save(@RequestBody final ReservationTimeRequestDto request) {
        return reservationTimeService.save(request);
    }

    @GetMapping
    public List<ReservationTimeResponseDto> findAll() {
        return reservationTimeService.findAll();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") final long id) {
        final ResponseCode deletedStatus = reservationTimeService.deleteById(id);
        return ResponseEntity.status(deletedStatus.getHttpStatus()).build();
    }
}
