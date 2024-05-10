package roomescape.controller.roomescape.member;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import roomescape.controller.dto.request.ReservationSaveRequest;
import roomescape.controller.dto.response.ReservationDeleteResponse;
import roomescape.controller.dto.response.ReservationResponse;
import roomescape.controller.dto.response.SelectableTimeResponse;
import roomescape.domain.member.Member;
import roomescape.service.roomescape.member.ReservationService;
import roomescape.util.infrastructure.AuthenticationPrincipal;

@RestController
@RequestMapping("/reservations")
public class MemberReservationController {
    private final ReservationService reservationService;

    public MemberReservationController(final ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping
    public ResponseEntity<ReservationResponse> save(
            @RequestBody ReservationSaveRequest reservationSaveRequest,
            @AuthenticationPrincipal Member member
    ) {
        ReservationResponse reservationResponse = reservationService.save(reservationSaveRequest, member);
        return ResponseEntity.created(URI.create("/reservations/" + reservationResponse.id()))
                .body(reservationResponse);
    }

    @GetMapping
    public ResponseEntity<List<ReservationResponse>> getAll() {
        return ResponseEntity.ok(reservationService.getAll());
    }

    @GetMapping("/times")
    public ResponseEntity<List<SelectableTimeResponse>> findSelectableTimes(
            @RequestParam(name = "date") LocalDate date,
            @RequestParam(name = "themeId") long themeId
    ) {
        return ResponseEntity.ok(reservationService.findSelectableTimes(date, themeId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ReservationDeleteResponse> delete(@PathVariable("id") long id) {
        return ResponseEntity.ok().body(reservationService.delete(id));
    }
}
