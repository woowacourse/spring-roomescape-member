package roomescape.reservation.controller;

import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.member.service.dto.LoginMemberInfo;
import roomescape.reservation.controller.dto.AdminReservationCreateRequest;
import roomescape.reservation.controller.dto.ReservationCreateRequest;
import roomescape.reservation.controller.dto.ReservationSearchConditionRequest;
import roomescape.reservation.service.ReservationService;
import roomescape.reservation.service.dto.ReservationCreateCommand;
import roomescape.reservation.service.dto.ReservationInfo;
import roomescape.reservation.service.dto.ReservationSearchCondition;

@RestController
@RequestMapping
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(final ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping("/reservations")
    public ResponseEntity<ReservationInfo> create(
            @RequestBody @Valid final ReservationCreateRequest request,
            LoginMemberInfo loginMemberInfo
    ) {
        ReservationCreateCommand reservationCreateCommand = request.convertToCreateCommand(loginMemberInfo.id());
        final ReservationInfo reservationInfo = reservationService.createReservation(reservationCreateCommand);
        return ResponseEntity.created(URI.create("/reservations/" + reservationInfo.id())).body(reservationInfo);
    }

    @PostMapping("/admin/reservations")
    public ResponseEntity<ReservationInfo> create(@RequestBody @Valid final AdminReservationCreateRequest request) {
        final ReservationInfo reservationInfo = reservationService.createReservation(request.convertToCreateCommand());
        return ResponseEntity.created(URI.create("/reservations/" + reservationInfo.id())).body(reservationInfo);
    }

    @GetMapping("/reservations")
    public ResponseEntity<List<ReservationInfo>> findAll(@ModelAttribute ReservationSearchConditionRequest request) {
        ReservationSearchCondition condition = request.toCondition();
        final List<ReservationInfo> reservationInfos = reservationService.getReservations(condition);
        return ResponseEntity.ok().body(reservationInfos);
    }

    @DeleteMapping("/reservations/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") final long id) {
        reservationService.cancelReservationById(id);
        return ResponseEntity.noContent().build();
    }
}
