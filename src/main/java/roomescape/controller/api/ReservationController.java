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
import roomescape.controller.api.dto.request.CreateReservationRequest;
import roomescape.controller.api.dto.response.ReservationResponse;
import roomescape.service.ReservationService;
import roomescape.service.dto.response.ReservationServiceResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<ReservationResponse> getAll() {
        List<ReservationServiceResponse> responses = reservationService.getAll();
        return responses.stream()
                .map(ReservationResponse::from)
                .toList();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ReservationResponse create(@RequestBody CreateReservationRequest request) {
        ReservationServiceResponse response = reservationService.create(request.toServiceRequest());
        return ReservationResponse.from(response);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        reservationService.delete(id);
    }
}
