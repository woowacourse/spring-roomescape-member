package roomescape.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import roomescape.dto.ReservationAvailableTimeResponse;
import roomescape.dto.ReservationRequest;
import roomescape.dto.ReservationResponse;
import roomescape.service.ReservationService;
import roomescape.service.UserReservationTimeService;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserReservationController {

    private final ReservationService reservationService;
    private final UserReservationTimeService userReservationTimeService;

    public UserReservationController(ReservationService reservationService,
                                     UserReservationTimeService userReservationTimeService) {
        this.reservationService = reservationService;
        this.userReservationTimeService = userReservationTimeService;
    }

    @PostMapping("/reservations")
    public ResponseEntity<ReservationResponse> postReservation(@RequestBody ReservationRequest request) {
        ReservationResponse reservationResponse = reservationService.postReservation(request);
        URI location = URI.create("/reservations/" + reservationResponse.id());
        return ResponseEntity.created(location).body(reservationResponse);
    }

    @GetMapping("/times")
    public List<ReservationAvailableTimeResponse> readAvailableReservationTimes(
            @RequestParam LocalDate date, @RequestParam long themeId
    ) {
        return userReservationTimeService.readAvailableReservationTimes(date, themeId);
    }
}
