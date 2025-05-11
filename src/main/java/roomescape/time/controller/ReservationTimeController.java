package roomescape.time.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.auth.RoleRequired;
import roomescape.member.entity.Role;
import roomescape.time.dto.ReservationTimeRequest;
import roomescape.time.dto.ReservationTimeResponse;
import roomescape.time.service.ReservationTimeService;

import java.net.URI;
import java.util.List;

@RestController
public class ReservationTimeController {

    private final ReservationTimeService service;

    public ReservationTimeController(ReservationTimeService service) {
        this.service = service;
    }

    @PostMapping("/times")
    @RoleRequired(Role.ADMIN)
    public ResponseEntity<ReservationTimeResponse> postReservationTime(@RequestBody ReservationTimeRequest request) {
        ReservationTimeResponse reservationTimeResponse = service.createReservationTime(request);
        URI location = URI.create("/times/" + reservationTimeResponse.id());
        return ResponseEntity.created(location).body(reservationTimeResponse);
    }

    @GetMapping("/times")
    public List<ReservationTimeResponse> getReservationTimes() {
        return service.readReservationTimes();
    }

    @DeleteMapping("/times/{id}")
    @RoleRequired(Role.ADMIN)
    public ResponseEntity<Void> deleteReservationTime(@PathVariable long id) {
        service.deleteReservationTime(id);
        return ResponseEntity.noContent().build();
    }
}
