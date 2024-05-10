package roomescape.controller;

import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import roomescape.controller.resolver.AuthenticationPrincipal;
import roomescape.domain.LoginMember;
import roomescape.service.ReservationService;
import roomescape.service.dto.AdminReservationRequestDto;
import roomescape.service.dto.CreateReservationDto;
import roomescape.service.dto.MemberReservationRequestDto;
import roomescape.service.dto.ReservationResponseDto;

@RestController
public class ReservationApiController {

    private final ReservationService reservationService;

    public ReservationApiController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping("/reservations")
    public List<ReservationResponseDto> findReservations() {
        return reservationService.findAllReservations();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/reservations")
    public ReservationResponseDto createReservationMember(@AuthenticationPrincipal LoginMember loginMember,
                                                          @Valid @RequestBody MemberReservationRequestDto requestDto) {
        return reservationService.createReservation(new CreateReservationDto(loginMember, requestDto));
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/admin/reservations")
    public ReservationResponseDto createReservationAdmin(@AuthenticationPrincipal LoginMember loginMember,
                                                         @Valid @RequestBody AdminReservationRequestDto reservationDto) {
        return reservationService.createReservation(reservationDto.toCreateReservation());
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/reservations/{id}")
    public void deleteReservation(@PathVariable long id) {
        reservationService.deleteReservation(id);
    }
}
