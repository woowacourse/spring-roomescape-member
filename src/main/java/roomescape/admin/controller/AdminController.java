package roomescape.admin.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.admin.domain.dto.AdminReservationRequestDto;
import roomescape.admin.service.AdminService;
import roomescape.reservation.domain.dto.ReservationResponseDto;
import roomescape.user.domain.User;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping("/reservations")
    public ResponseEntity<ReservationResponseDto> createReservation(
            @RequestBody AdminReservationRequestDto adminReservationRequestDto,
            User admin) {
        ReservationResponseDto reservationResponseDto = adminService.createReservation(
                adminReservationRequestDto, admin);
        return ResponseEntity.status(HttpStatus.CREATED).body(reservationResponseDto);
    }
}
