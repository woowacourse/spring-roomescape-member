package roomescape.reservation.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.reservation.domain.dto.ReservationReqDto;
import roomescape.reservation.domain.dto.ReservationResDto;
import roomescape.reservation.service.ReservationService;

@RestController
public class ReservationUserApiController {

    private final ReservationService service;

    public ReservationUserApiController(ReservationService service) {
        this.service = service;
    }

    @GetMapping("reservations")
    public ResponseEntity<List<ReservationResDto>> readAll() {
        List<ReservationResDto> resDtos = service.readAll();
        return ResponseEntity.ok(resDtos);
    }

    @PostMapping("reservations")
    public ResponseEntity<ReservationResDto> add(@RequestBody ReservationReqDto dto) {
        ReservationResDto resDto = service.add(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(resDto);
    }

    @DeleteMapping("reservations/{reservationId}")
    public ResponseEntity<Void> delete(@PathVariable("reservationId") Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
