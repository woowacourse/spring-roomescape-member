package roomescape.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import roomescape.dto.ReservationTimeRequest;
import roomescape.dto.ReservationTimeResponse;
import roomescape.service.ReservationTimeService;

@RestController
public class ReservationTimeController {

    private final ReservationTimeService service;

    public ReservationTimeController(ReservationTimeService service) {
        this.service = service;
    }

    @GetMapping("/times")
    public List<ReservationTimeResponse> readAllReservationTime() {
        return service.readAllReservationTime();
    }

    @PostMapping("/times")
    @ResponseStatus(HttpStatus.CREATED)
    public ReservationTimeResponse postReservationTime(@RequestBody ReservationTimeRequest request) {
        return service.postReservationTime(request);
    }

    @DeleteMapping("/times/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteReservationTime(@PathVariable long id) {
        service.deleteReservationTime(id);
    }
}
