package roomescape.admin.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.reservation.service.ReservationTimeService;

@RequestMapping("/admin/Times")
@RestController
@RequiredArgsConstructor
public class AdminReservationTimeController {
    private final ReservationTimeService reservationTimeService;

    @DeleteMapping("/{id}")
    public void deleteTime(@PathVariable long id) {
        reservationTimeService.deleteReservationTime(id);
    }

}
