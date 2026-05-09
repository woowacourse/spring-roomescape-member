package roomescape.reservation.presentation;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
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
import roomescape.reservation.application.ReservationTimeService;
import roomescape.reservation.presentation.dto.request.ReservationTimeSaveRequest;
import roomescape.reservation.presentation.dto.response.AvailableTimeFindResponse;
import roomescape.reservation.presentation.dto.response.ReservationTimeFindResponse;
import roomescape.reservation.presentation.dto.response.ReservationTimeSaveResponse;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/times")
@RequiredArgsConstructor
public class ReservationTimeController {
    private final ReservationTimeService reservationTimeService;

    @PostMapping
    public ResponseEntity<ReservationTimeSaveResponse> save(
            @RequestBody @Valid ReservationTimeSaveRequest body) {
        ReservationTimeSaveResponse response = reservationTimeService.save(body);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<ReservationTimeFindResponse>> findAll() {
        List<ReservationTimeFindResponse> responses = reservationTimeService.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(responses);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable long id) {
        reservationTimeService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/availability")
    public ResponseEntity<List<AvailableTimeFindResponse>> findTimesByDateAndThemeId(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam long themeId
    ) {
        List<AvailableTimeFindResponse> responses = reservationTimeService.findTimesByDateAndThemeId(date, themeId);
        return ResponseEntity.status(HttpStatus.OK).body(responses);
    }
}
