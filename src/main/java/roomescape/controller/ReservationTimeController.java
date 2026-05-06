package roomescape.controller;

import jakarta.validation.Valid;
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
import roomescape.dto.ReservationTimeAvailabilityResponseDto;
import roomescape.dto.ReservationTimeRequestDto;
import roomescape.dto.ReservationTimeResponseDto;
import roomescape.service.ReservationTimeService;

@RestController
@RequestMapping(value = "/times")
public class ReservationTimeController {
    private final ReservationTimeService reservationTimeService;

    public ReservationTimeController(ReservationTimeService reservationTimeService) {
        this.reservationTimeService = reservationTimeService;
    }

    @PostMapping
    public ResponseEntity<ReservationTimeResponseDto> create(@Valid @RequestBody ReservationTimeRequestDto requestDto) {
        ReservationTimeResponseDto responseDto = reservationTimeService.create(requestDto);
        return ResponseEntity.
                status(HttpStatus.CREATED)
                .body(responseDto);
    }

    @GetMapping
    public ResponseEntity<List<ReservationTimeResponseDto>> readAll() {
        List<ReservationTimeResponseDto> responseDtos = reservationTimeService.readAll();
        return ResponseEntity.ok(responseDtos);
    }

    @GetMapping("/available")
    public ResponseEntity<List<ReservationTimeAvailabilityResponseDto>> readAvailabilityByDateAndTheme(
            @RequestParam("date") LocalDate date, @RequestParam("themeId") Long themeId) {

        List<ReservationTimeAvailabilityResponseDto> responseDtos = reservationTimeService.readAvailabilityByDateAndTheme(
                date, themeId);

        return ResponseEntity.ok(responseDtos);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {

        reservationTimeService.delete(id);
        return ResponseEntity
                .noContent()
                .build();
    }
}
