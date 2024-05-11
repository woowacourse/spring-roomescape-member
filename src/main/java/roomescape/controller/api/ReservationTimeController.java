package roomescape.controller.api;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import roomescape.domain.ReservationTime;
import roomescape.dto.request.ReservationTimeRequestDto;
import roomescape.dto.response.ReservationTimeResponseDto;
import roomescape.dto.response.ReservationTimeWithStateDto;
import roomescape.service.ReservationTimeService;

@RestController
public class ReservationTimeController {
    private final ReservationTimeService reservationTimeService;

    public ReservationTimeController(final ReservationTimeService reservationTimeService) {
        this.reservationTimeService = reservationTimeService;
    }

    @PostMapping("/times")
    public ResponseEntity<ReservationTimeResponseDto> createTime(
            @RequestBody @Valid final ReservationTimeRequestDto request
    ) {
        final ReservationTime time = reservationTimeService.create(request);
        final ReservationTimeResponseDto response = new ReservationTimeResponseDto(time);
        return ResponseEntity.created(URI.create("/times/" + response.getId())).body(response);
    }

    @GetMapping("/times")
    public ResponseEntity<List<ReservationTimeResponseDto>> findAllTimes() {
        return reservationTimeService.findAll()
                .stream()
                .map(ReservationTimeResponseDto::new)
                .collect(collectingAndThen(toList(), ResponseEntity::ok));
    }

    @GetMapping("/times/available")
    public ResponseEntity<List<ReservationTimeWithStateDto>> findAvailableTimes(
            @RequestParam("date") final String date,
            @RequestParam("theme-id") final Long themeId
    ) {
        return ResponseEntity.ok(reservationTimeService.findAllWithReservationState(date, themeId));
    }

    @DeleteMapping("/times/{id}")
    public ResponseEntity<Void> deleteTime(@PathVariable("id") final long id) {
        reservationTimeService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
