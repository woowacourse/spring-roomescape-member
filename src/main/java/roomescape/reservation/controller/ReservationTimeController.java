package roomescape.reservation.controller;

import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import roomescape.reservation.dao.ReservationTimeDao;
import roomescape.reservation.entity.ReservationTimeEntity;

@RestController
@RequestMapping("/times")
public class ReservationTimeController {

    private ReservationTimeDao reservationTimeDao;

    public ReservationTimeController(ReservationTimeDao reservationTimeDao) {
        this.reservationTimeDao = reservationTimeDao;
    }

    @PostMapping
    public ResponseEntity<Void> createTime(
            @RequestBody ReservationTimeEntity reservationTimeEntity
    ){
        int timeId = reservationTimeDao.insert(reservationTimeEntity);
        return ResponseEntity.created(createUri(timeId)).build();
    }

    @GetMapping
    public ResponseEntity<List<ReservationTimeEntity>> getTimes(
    ){
        return ResponseEntity.ok().body(
                reservationTimeDao.findAllTimes()
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTime(
            @PathVariable Long id
    ){
        reservationTimeDao.delete(id);
        return ResponseEntity.noContent().build();
    }

    private URI createUri(int reservationId) {
        return ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(reservationId)
                .toUri();
    }
}
