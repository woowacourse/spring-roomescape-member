package roomescape.controller;

import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.controller.request.LoginMemberInformation;
import roomescape.controller.request.MemberReservationWebRequest;
import roomescape.controller.response.ReservationWebResponse;
import roomescape.service.ReservationService;
import roomescape.service.request.ReservationAppRequest;
import roomescape.service.response.ReservationAppResponse;

@RestController
@RequestMapping("/reservations")
public class MemberReservationController {

    private final ReservationService reservationService;

    public MemberReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping
    public ResponseEntity<ReservationWebResponse> reserve(@Valid @RequestBody MemberReservationWebRequest request,
                                                          @Valid @AuthArgumentResolver
                                                          LoginMemberInformation loginMemberInformation) {
        ReservationAppResponse appResponse = reservationService.save(
            new ReservationAppRequest(loginMemberInformation.name(), request.date(), request.timeId(),
                request.themeId(), loginMemberInformation.id()));

        Long id = appResponse.id();
        ReservationWebResponse reservationWebResponse = ReservationWebResponse.from(appResponse);

        return ResponseEntity.created(URI.create("/reservations/" + id))
            .body(reservationWebResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBy(@PathVariable Long id) {
        reservationService.delete(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<ReservationWebResponse>> getReservations() {
        List<ReservationAppResponse> appResponses = reservationService.findAll();
        List<ReservationWebResponse> reservationWebResponse = appResponses.stream()
            .map(ReservationWebResponse::from)
            .toList();

        return ResponseEntity.ok(reservationWebResponse);
    }

}
