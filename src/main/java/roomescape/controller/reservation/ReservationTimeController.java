package roomescape.controller.reservation;

import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import roomescape.dto.reservation.ReservationTimeRequest;
import roomescape.dto.reservation.ReservationTimeResponse;
import roomescape.service.reservation.ReservationTimeService;

@RestController
@RequestMapping("/times")
public class ReservationTimeController {

    private final ReservationTimeService service;

    public ReservationTimeController(ReservationTimeService service) {
        this.service = service;
    }

    @GetMapping
    public List<ReservationTimeResponse> readAllReservationTime() {
        return service.readAllReservationTime();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ReservationTimeResponse postReservationTime(@RequestBody ReservationTimeRequest request,
                                                       HttpServletResponse response) {
        ReservationTimeResponse reservationTimeResponse = service.postReservationTime(request);
        response.setHeader("Location", "/times/" + reservationTimeResponse.id());
        return reservationTimeResponse;
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteReservationTime(@PathVariable Long id) {
        service.deleteReservationTime(id);
    }
}
