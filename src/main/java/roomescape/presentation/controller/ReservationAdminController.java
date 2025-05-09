package roomescape.presentation.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import roomescape.common.exception.UnauthorizedException;
import roomescape.dto.request.ReservationAdminRegisterDto;
import roomescape.model.LoginMember;
import roomescape.model.Role;
import roomescape.service.ReservationAdminService;

@RestController
@RequestMapping("/admin/reservations")
public class ReservationAdminController {

    private final ReservationAdminService reservationAdminService;

    public ReservationAdminController(ReservationAdminService reservationAdminService) {
        this.reservationAdminService = reservationAdminService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void addReservation(@RequestBody ReservationAdminRegisterDto reservationAdminRegisterDto,
                               LoginMember loginMember) {
        if (!loginMember.role().equals(Role.ADMIN)) {
            throw new UnauthorizedException("해당 경로로 접근할 권한이 없습니다.");
        }

        reservationAdminService.saveReservation(reservationAdminRegisterDto);
    }
}
