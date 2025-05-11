package roomescape.controller.api.reservation;

import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import roomescape.controller.helper.LoginMember;
import roomescape.controller.api.reservation.dto.AddReservationRequest;
import roomescape.controller.api.reservation.dto.ReservationResponse;
import roomescape.model.Member;
import roomescape.service.ReservationService;

@Controller
@RequestMapping("/reservations")
public class ReservationApiController {

    private final ReservationService service;

    public ReservationApiController(final ReservationService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<ReservationResponse>> findAllReservations() {
        var reservations = service.findAll();
        return ResponseEntity.ok(reservations);
    }

    @PostMapping
    public ResponseEntity<ReservationResponse> addReservation(
            @LoginMember final Member member,
            @RequestBody @Valid final AddReservationRequest request
            ) {
        var response = service.add(request, member);
        return ResponseEntity.created(URI.create("/reservations/" + response.id())).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable("id") final Long id) {
        boolean isRemoved = service.removeById(id);
        if (isRemoved) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
