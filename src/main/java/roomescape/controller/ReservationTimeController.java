package roomescape.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import roomescape.domain.ReservationTime;
import roomescape.repository.JdbcReservationTimeRepository;

@Controller
public class ReservationTimeController {
    private final JdbcReservationTimeRepository jdbcReservationTimeRepository;

    public ReservationTimeController(final JdbcReservationTimeRepository jdbcReservationTimeRepository) {
        this.jdbcReservationTimeRepository = jdbcReservationTimeRepository;
    }

    @GetMapping("/times")
    public ResponseEntity<List<ReservationTime>> read() {
        return ResponseEntity.ok().body(jdbcReservationTimeRepository.read());
    }

    @PostMapping("/times")
    public ResponseEntity<Void> create(@RequestBody final ReservationTimeCreateReqDto request) {
        jdbcReservationTimeRepository.create(request.startAt());

        return ResponseEntity.status(201).build();
    }

    @DeleteMapping("/times/{id}")
    public ResponseEntity<Void> delete(@PathVariable final Long id) {
        jdbcReservationTimeRepository.delete(id);
        return ResponseEntity.noContent().build();
    }
}
