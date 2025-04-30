package roomescape.reservationTime.controller;

import java.time.LocalDate;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import roomescape.reservationTime.domain.dto.AvailableReservationTimeResDto;
import roomescape.reservationTime.domain.dto.ReservationTimeReqDto;
import roomescape.reservationTime.domain.dto.ReservationTimeResDto;
import roomescape.reservationTime.service.ReservationTimeService;

@RestController
@RequestMapping("/times")
public class ReservationTimeController {

    private final ReservationTimeService service;

    public ReservationTimeController(ReservationTimeService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<ReservationTimeResDto>> findAll() {
        List<ReservationTimeResDto> resDtos = service.findAll();
        return ResponseEntity.ok(resDtos);
    }

    @GetMapping("/available-time")
    public ResponseEntity<List<AvailableReservationTimeResDto>> findAllAvailableTimes(
        @RequestParam("themeId") Long themeId, @RequestParam("date") LocalDate date) {
        List<AvailableReservationTimeResDto> availableReservationTimeResDtos = service.findAllAvailableTimes(themeId,
            date);
        return ResponseEntity.ok(availableReservationTimeResDtos);
    }

    @PostMapping
    public ResponseEntity<ReservationTimeResDto> add(@RequestBody ReservationTimeReqDto reqDto) {
        ReservationTimeResDto resDto = service.add(reqDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(resDto);
    }

    @DeleteMapping("/{reservationTimeId}")
    public ResponseEntity<Void> delete(@PathVariable("reservationTimeId") Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
