package roomescape.admin.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.admin.dto.AdminReservationTimeRequest;
import roomescape.admin.dto.AdminReservationTimeResponse;
import roomescape.admin.service.AdminReservationTimeService;
import roomescape.domain.ReservationTime;

@RestController
@RequestMapping("/admin")
public class AdminReservationTimeController {

    private final AdminReservationTimeService adminReservationTimeService;

    public AdminReservationTimeController(AdminReservationTimeService adminReservationTimeService) {
        this.adminReservationTimeService = adminReservationTimeService;
    }

    @PostMapping("/times")
    public ResponseEntity<AdminReservationTimeResponse> create(@RequestBody AdminReservationTimeRequest request) {
        ReservationTime time = adminReservationTimeService.add(request.startAt());
        return ResponseEntity.status(HttpStatus.CREATED).body(AdminReservationTimeResponse.from(time));
    }

    @DeleteMapping("/times/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        adminReservationTimeService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
