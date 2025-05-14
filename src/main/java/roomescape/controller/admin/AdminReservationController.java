package roomescape.controller.admin;

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
import roomescape.dto.request.ReservationPostRequestByAdmin;
import roomescape.dto.response.ReservationPostResponse;
import roomescape.service.ReservationCommandService;
import roomescape.service.ReservationQueryService;

@RestController
@RequestMapping("/admin/reservations")
public class AdminReservationController {

    private final ReservationCommandService reservationCommandService;
    private final ReservationQueryService reservationQueryService;

    public AdminReservationController(ReservationCommandService reservationCommandService,
                                      ReservationQueryService reservationQueryService) {
        this.reservationCommandService = reservationCommandService;
        this.reservationQueryService = reservationQueryService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ReservationPostResponse> readReservations(
            @RequestParam(required = false) Long themeId,
            @RequestParam(required = false) Long memberId,
            @RequestParam(required = false) LocalDate dateFrom,
            @RequestParam(required = false) LocalDate dateTo
    ) {

        return reservationQueryService.searchReservations(themeId, memberId, dateFrom, dateTo);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ReservationPostResponse createReservation(
            @RequestBody ReservationPostRequestByAdmin request
    ) {
        return reservationCommandService.createReservationOfRequestMember(request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteReservation(
            @PathVariable long id
    ) {
        reservationCommandService.deleteReservation(id);
    }
}
