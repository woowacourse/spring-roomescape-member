package roomescape.controller;

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
import roomescape.dao.resetvationTime.ReservationTimeDao;
import roomescape.domain.ReservationTime;
import roomescape.dto.request.ReservationTimeCreateRequest;
import roomescape.dto.response.ReservationTimeCreateResponse;
import roomescape.dto.response.ReservationTimeResponse;

@RestController
@RequestMapping("/times")
public class TimeController {

    private final ReservationTimeDao reservationTimeDao;

    public TimeController(final ReservationTimeDao reservationTimeDao) {
        this.reservationTimeDao = reservationTimeDao;
    }

    @GetMapping
    public List<ReservationTimeResponse> findAll() {
        return reservationTimeDao.findAll().stream()
                .map(ReservationTimeResponse::new)
                .toList();
    }

    @PostMapping
    public ResponseEntity<ReservationTimeCreateResponse> create(
            @RequestBody ReservationTimeCreateRequest reservationTimeCreateRequest) {
        ReservationTime reservationTime = reservationTimeDao.create(
                new ReservationTime(reservationTimeCreateRequest.getLocalTime()));
        ReservationTimeCreateResponse reservationTimeCreateResponse = new ReservationTimeCreateResponse(
                reservationTime);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(reservationTimeCreateResponse.getId())
                .toUri();
        return ResponseEntity.created(location).body(reservationTimeCreateResponse);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable long id) {
        reservationTimeDao.delete(id);
        return ResponseEntity.noContent().build();
    }
}
