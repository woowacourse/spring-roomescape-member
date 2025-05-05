package roomescape.reservation.presentation.controller;

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
import roomescape.auth.presentation.AuthenticatedMember;
import roomescape.reservation.application.ReservationQueryService;
import roomescape.reservation.application.ReservationService;
import roomescape.reservation.application.dto.info.ReservationDto;
import roomescape.reservation.domain.Member;
import roomescape.reservation.infrastructure.dto.ReservationDetailData;
import roomescape.reservation.presentation.dto.request.ReservationRequest;
import roomescape.reservation.presentation.dto.response.ReservationDetailResponse;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationService service;
    private final ReservationQueryService queryService;

    public ReservationController(ReservationService service, ReservationQueryService queryService) {
        this.service = service;
        this.queryService = queryService;
    }

    @GetMapping
    public List<ReservationDetailResponse> getAllReservations() {
        List<ReservationDetailData> reservationDetails = queryService.getAllReservationDetails();
        return ReservationDetailResponse.from(reservationDetails);
    }

    @PostMapping
    public ResponseEntity<ReservationDetailResponse> createReservation(
            @Valid @RequestBody ReservationRequest request,
            @AuthenticatedMember Member member) {
        ReservationDto reservationDto = service.registerReservationForUser(request, member.getId());

        ReservationDetailData data = queryService.getReservationDetailById(reservationDto.id());
        ReservationDetailResponse response = ReservationDetailResponse.from(data);
        return ResponseEntity.created(URI.create("/reservations/" + reservationDto.id())).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable("id") Long id) {
        service.deleteReservation(id);
        return ResponseEntity.noContent().build();
    }
}
