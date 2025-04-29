package roomescape.controller.api;

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

import lombok.RequiredArgsConstructor;
import roomescape.controller.api.dto.request.CreateReservationTimeRequest;
import roomescape.controller.api.dto.response.ReservationTimeResponse;
import roomescape.service.ReservationTimeService;
import roomescape.service.dto.query.ReservationTimeQuery;

@RestController
@RequestMapping("/times")
@RequiredArgsConstructor
public class ReservationTimeController {

    private final ReservationTimeService reservationTimeService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ReservationTimeResponse create(@RequestBody CreateReservationTimeRequest request) {
        ReservationTimeQuery query = reservationTimeService.create(request.toCommand());
        return ReservationTimeResponse.from(query);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<ReservationTimeResponse> getAll() {
        List<ReservationTimeQuery> reservationTimes = reservationTimeService.getAll();
        return reservationTimes.stream()
                .map(ReservationTimeResponse::from)
                .toList();
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        reservationTimeService.delete(id);
    }
}
