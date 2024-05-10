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
import roomescape.controller.response.MemberReservationWebResponse;
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
    public ResponseEntity<MemberReservationWebResponse> reserve(@Valid @RequestBody MemberReservationWebRequest request,
                                                                @Valid @Auth
                                                                LoginMemberInformation loginMemberInformation) {
        ReservationAppResponse appResponse = reservationService.save(
            new ReservationAppRequest(request.date(), request.timeId(),
                request.themeId(), loginMemberInformation.id()));

        Long id = appResponse.id();
        MemberReservationWebResponse memberReservationWebResponse = MemberReservationWebResponse.from(appResponse);

        return ResponseEntity.created(URI.create("/reservations/" + id))
            .body(memberReservationWebResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBy(@PathVariable Long id) {
        reservationService.delete(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<MemberReservationWebResponse>> getReservations() {
        List<ReservationAppResponse> appResponses = reservationService.findAll();
        List<MemberReservationWebResponse> memberReservationWebResponse = appResponses.stream()
            .map(MemberReservationWebResponse::from)
            .toList();

        return ResponseEntity.ok(memberReservationWebResponse);
    }

}
