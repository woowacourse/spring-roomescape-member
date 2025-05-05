package roomescape.reservation.presentation.controller;

import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.reservation.application.ReservationQueryService;
import roomescape.reservation.application.ReservationService;
import roomescape.reservation.application.dto.info.ReservationDto;
import roomescape.reservation.infrastructure.dto.ReservationDetailData;
import roomescape.reservation.presentation.dto.request.AdminReservationRequest;
import roomescape.reservation.presentation.dto.response.ReservationDetailResponse;

@RestController
@RequestMapping("/admin/reservations")
public class AdminReservationController {

    private final ReservationService service;
    private final ReservationQueryService queryService;

    public AdminReservationController(ReservationService service, ReservationQueryService queryService) {
        this.service = service;
        this.queryService = queryService;
    }

    @PostMapping
    public ResponseEntity<ReservationDetailResponse> createReservation(@RequestBody AdminReservationRequest request) {
        ReservationDto reservationDto = service.registerReservationForAdmin(request);

        ReservationDetailData data = queryService.getReservationDetailById(reservationDto.id());
        ReservationDetailResponse response = ReservationDetailResponse.from(data);
        return ResponseEntity.created(URI.create("/admin/reservations/" + reservationDto.id())).body(response);
    }
}
