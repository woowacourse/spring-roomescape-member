package roomescape.reservation.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.reservation.dao.ReservationTimeDao;
import roomescape.reservation.dto.ReservationTimeRequest;
import roomescape.reservation.dto.ReservationTimeResponse;
import roomescape.reservation.entity.ReservationTime;
import roomescape.reservation.service.ReservationTimeService;

@RestController
@RequestMapping("/times")
public class ReservationTimeController {

    private ReservationTimeService reservationTimeService;
    private ReservationTimeDao reservationTimeDao;

    public ReservationTimeController(ReservationTimeDao reservationTimeDao,
                                     ReservationTimeService reservationTimeService) {
        this.reservationTimeService = reservationTimeService;
        this.reservationTimeDao = reservationTimeDao;
    }

    @PostMapping
    public ResponseEntity<ReservationTimeResponse> createTime(
            @RequestBody ReservationTimeRequest reservationTimeRequest
    ) {
        return ResponseEntity.ok().body(
                reservationTimeService.createReservationTime(reservationTimeRequest)
        );
    }

    @GetMapping
    public ResponseEntity<List<ReservationTime>> getTimes(
    ) {
        return ResponseEntity.ok().body(
                reservationTimeDao.findAllTimes()
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTime(
            @PathVariable Long id
    ) {
        reservationTimeDao.delete(id);
        return ResponseEntity.noContent().build();
    }
}
