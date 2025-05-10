package roomescape.admin.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.admin.domain.dto.AdminReservationRequestDto;
import roomescape.admin.domain.dto.AdminReservationResponseDto;
import roomescape.admin.service.AdminService;
import roomescape.user.domain.User;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping("/reservation")
    public ResponseEntity<AdminReservationResponseDto> createReservation(
            @RequestBody AdminReservationRequestDto adminReservationRequestDto,
            User user) {
        AdminReservationResponseDto adminReservationResponseDto = adminService.createReservation(
                adminReservationRequestDto, user);
        return ResponseEntity.status(HttpStatus.CREATED).body(adminReservationResponseDto);
    }
}
