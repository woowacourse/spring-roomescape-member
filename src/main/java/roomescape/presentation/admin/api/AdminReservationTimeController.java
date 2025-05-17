package roomescape.presentation.admin.api;

import jakarta.validation.Valid;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.business.service.admin.AdminReservationTimeService;
import roomescape.presentation.admin.dto.ReservationTimeRequestDto;
import roomescape.presentation.admin.dto.ReservationTimeResponseDto;

@RestController
@RequestMapping("/admin/times")
public final class AdminReservationTimeController {

    private final AdminReservationTimeService reservationTimeService;

    public AdminReservationTimeController(AdminReservationTimeService reservationTimeService) {
        this.reservationTimeService = reservationTimeService;
    }

    @PostMapping
    public ResponseEntity<ReservationTimeResponseDto> createReservationTime(
            @Valid @RequestBody ReservationTimeRequestDto reservationTimeDto) {
        ReservationTimeResponseDto time = reservationTimeService.createTime(reservationTimeDto);
        String location = "/times/" + time.id();
        return ResponseEntity.created(URI.create(location)).body(time);
    }

    @DeleteMapping("/{timeId}")
    public ResponseEntity<Void> deleteReservationTime(@PathVariable("timeId") Long id) {
        reservationTimeService.deleteTimeById(id);
        return ResponseEntity.noContent().build();
    }
}
