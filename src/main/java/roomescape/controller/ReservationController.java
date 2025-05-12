package roomescape.controller;

import java.time.LocalDate;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import roomescape.application.dto.ReservationRequest;
import roomescape.application.dto.ReservationResponse;
import roomescape.application.service.ReservationService;
import roomescape.domain.LoginMember;
import roomescape.global.config.AuthenticationPrincipal;

@RestController
@RequestMapping("reservations")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping
    public List<ReservationResponse> readFilterReservations(
            @RequestParam(required = false) Long themeId,
            @RequestParam(required = false) Long memberId,
            @RequestParam(required = false) LocalDate dateFrom,
            @RequestParam(required = false) LocalDate dateTo
    ) {
        if (hasNoParameters(themeId, memberId, dateFrom, dateTo)) {
            return reservationService.getReservations();
        }
        return reservationService.getFilteredReservations(themeId, memberId, dateFrom, dateTo);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ReservationResponse createReservation(
            @AuthenticationPrincipal LoginMember loginMember,
            @RequestBody ReservationRequest request
    ) {
        return reservationService.createReservation(loginMember, request);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteReservation(
            @PathVariable long id
    ) {
        reservationService.deleteReservation(id);
    }

    private boolean hasNoParameters(Long themeId, Long memberId, LocalDate dateFrom, LocalDate dateTo) {
        return themeId == null && memberId == null && dateFrom == null && dateTo == null;
    }
}
