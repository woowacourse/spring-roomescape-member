package roomescape.controller;

import jakarta.validation.Valid;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import roomescape.controller.resolver.AuthenticationPrincipal;
import roomescape.service.ReservationService;
import roomescape.service.dto.AdminReservationRequest;
import roomescape.service.dto.CreateReservation;
import roomescape.service.dto.LoginMember;
import roomescape.service.dto.MemberReservationRequest;
import roomescape.service.dto.ReservationResponseDto;
import roomescape.service.dto.ReservationSearchParams;

@RestController
public class ReservationApiController {

    private final ReservationService reservationService;

    public ReservationApiController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping("/admin/reservations")
    public List<ReservationResponseDto> findReservations(
            @RequestParam(name = "member", required = false) Long memberId,
            @RequestParam(name = "theme", required = false) Long themeId,
            @RequestParam(name = "start-date", required = false) LocalDate dateFrom,
            @RequestParam(name = "end-date", required = false) LocalTime dateTo) {

        ReservationSearchParams requestDto = new ReservationSearchParams(memberId, themeId, dateFrom, dateTo);
        return reservationService.findAllReservations(requestDto);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/reservations")
    public ReservationResponseDto createReservationMember(@AuthenticationPrincipal LoginMember loginMember,
                                                          @Valid @RequestBody MemberReservationRequest requestDto) {
        return reservationService.createReservation(new CreateReservation(loginMember, requestDto));
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/admin/reservations")
    public ReservationResponseDto createReservationAdmin(
            @Valid @RequestBody AdminReservationRequest reservationDto) {
        return reservationService.createReservation(reservationDto.toCreateReservation());
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/admin/reservations/{id}")
    public void deleteReservation(@PathVariable long id) {
        reservationService.deleteReservation(id);
    }
}
