package roomescape.controller.api.reservation;

import jakarta.validation.Valid;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import roomescape.controller.api.member.dto.LoginMemberInfo;
import roomescape.controller.api.reservation.dto.AddReservationRequest;
import roomescape.controller.api.reservation.dto.ReservationResponse;
import roomescape.controller.api.reservation.dto.ReservationSearchFilter;
import roomescape.controller.helper.LoginMember;
import roomescape.service.ReservationService;

@Controller
@RequestMapping("/reservations")
public class ReservationApiController {

    private final ReservationService reservationService;

    public ReservationApiController(final ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping
    public ResponseEntity<List<ReservationResponse>> findAllReservations(
            @RequestParam(name = "memberId", required = false) final Long memberId,
            @RequestParam(name = "themeId", required = false) final Long themeId,
            @RequestParam(name = "dateFrom", required = false) final LocalDate dateFrom,
            @RequestParam(name = "dateTo", required = false) final LocalDate dateTo
    ) {
        final ReservationSearchFilter searchFilter = new ReservationSearchFilter(memberId, themeId, dateFrom, dateTo);
        final List<ReservationResponse> response = reservationService.findAll(searchFilter);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping
    public ResponseEntity<ReservationResponse> addReservation(
            @LoginMember final LoginMemberInfo loginMemberInfo,
            @RequestBody @Valid final AddReservationRequest request
    ) {
        final ReservationResponse response = reservationService.add(request, loginMemberInfo);
        return ResponseEntity.created(URI.create("/reservations/" + response.id())).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable("id") final Long id) {
        final boolean isRemoved = reservationService.removeById(id);
        if (isRemoved) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
