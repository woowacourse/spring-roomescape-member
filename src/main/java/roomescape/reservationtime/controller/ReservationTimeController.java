package roomescape.reservationtime.controller;

import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import roomescape.reservationtime.controller.dto.ReservationTimeResponse;
import roomescape.reservationtime.domain.ReservationTime;
import roomescape.reservationtime.service.ReservationTimeService;

@RestController
@RequestMapping("/themes/{themeId}")
@RequiredArgsConstructor
public class ReservationTimeController {

    private final ReservationTimeService reservationTimeService;

    @GetMapping("/available-times")
    @ResponseStatus(HttpStatus.OK)
    public List<ReservationTimeResponse> read(
            @PathVariable final Long themeId,
            @RequestParam final LocalDate date
    ) {
        return reservationTimeService.findAvailableTimes(date, themeId).stream()
                .map(ReservationTimeResponse::from).toList();
    }

}
