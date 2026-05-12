package roomescape.reservationtime.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import roomescape.reservationtime.payload.AvailableReservationTimeRequest;
import roomescape.reservationtime.payload.ReservationTimeResponse;
import roomescape.reservationtime.service.ReservationTimeService;

@RestController
@RequestMapping("/times")
public class ReservationTimeController {

    private final ReservationTimeService reservationTimeService;

    public ReservationTimeController(ReservationTimeService reservationTimeService) {
        this.reservationTimeService = reservationTimeService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ReservationTimeResponse> getAvailableReservationTimes(
            @ModelAttribute AvailableReservationTimeRequest request) {
        if (request.isEmpty()) {
            return reservationTimeService.findAll()
                    .stream()
                    .map(ReservationTimeResponse::from)
                    .toList();
        }
        if (request.isComplete()) {
            return reservationTimeService.findAvailableReservationTimes(request.date(), request.themeId())
                    .stream()
                    .map(ReservationTimeResponse::from)
                    .toList();
        }
        throw new IllegalArgumentException("날짜와 테마는 함께 입력해야 합니다.");
    }
}
