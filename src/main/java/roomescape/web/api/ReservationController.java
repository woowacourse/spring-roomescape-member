package roomescape.web.api;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import roomescape.service.MemberService;
import roomescape.service.ReservationService;
import roomescape.service.dto.request.ReservationRequest;
import roomescape.service.dto.response.ReservationResponse;
import roomescape.web.api.dto.ReservationAdminRequest;
import roomescape.web.api.dto.ReservationListResponse;
import roomescape.web.api.dto.ReservationMemberRequest;
import roomescape.web.api.resolver.Auth;
import roomescape.web.api.resolver.Principal;

import java.net.URI;
import java.util.List;

@RestController
public class ReservationController {
    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService, MemberService memberService) {
        this.reservationService = reservationService;
    }

    @PostMapping("/reservations")
    public ResponseEntity<ReservationResponse> save(
            @RequestBody @Valid ReservationMemberRequest request,
            @Auth Principal principal
    ) {
        ReservationRequest reservationRequest = new ReservationRequest(principal.id(), request.date(),
                request.timeId(), request.themeId());

        ReservationResponse reservationResponse = reservationService.save(reservationRequest);

        return ResponseEntity.created(URI.create("/reservations/" + reservationResponse.id()))
                .body(reservationResponse);
    }

    @GetMapping("/reservations")
    public ResponseEntity<ReservationListResponse> findAll() {
        List<ReservationResponse> reservationResponses = reservationService.findAll();

        return ResponseEntity.ok(new ReservationListResponse(reservationResponses));
    }

    @DeleteMapping("/reservations/{id}")
    public ResponseEntity<Void> delete(@PathVariable(name = "id") Long id) {
        reservationService.deleteById(id);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/admin/reservations")
    public ResponseEntity<ReservationResponse> save(@RequestBody @Valid ReservationAdminRequest request) {
        ReservationRequest reservationRequest = new ReservationRequest(request.memberId(), request.date(),
                request.timeId(), request.themeId());

        ReservationResponse reservationResponse = reservationService.save(reservationRequest);

        return ResponseEntity.created(URI.create("/reservations/" + reservationResponse.id()))
                .body(reservationResponse);
    }

    @GetMapping("/admin/reservations")
    public ResponseEntity<ReservationListResponse> findByCondition(
            @RequestParam(value = "name") String name,
            @RequestParam(value = "themeId") Long themeId,
            @RequestParam(value = "dateFrom") String dateFrom,
            @RequestParam(value = "dateTo") String dateTo
    ) {
        List<ReservationResponse> reservationResponses = reservationService.findByCondition(name, themeId, dateFrom,
                dateTo);

        return ResponseEntity.ok(new ReservationListResponse(reservationResponses));
    }
}
