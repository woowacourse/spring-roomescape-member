package roomescape.controller.admin;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import roomescape.dto.request.CreateAdminReservationRequest;
import roomescape.service.ReservationService;

@RestController
@RequestMapping("/admin/reservations")
@RequiredArgsConstructor
public class AdminReservationController {

    private final ReservationService reservationService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public void create(@RequestBody @Valid CreateAdminReservationRequest request) {
        reservationService.create(request.memberId(), request.date(), request.timeId(), request.themeId());
    }
}
