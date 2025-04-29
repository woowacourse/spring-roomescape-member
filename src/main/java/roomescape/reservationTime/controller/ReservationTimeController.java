package roomescape.reservationTime.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.reservationTime.domain.dto.ReservationTimeReqDto;
import roomescape.reservationTime.domain.dto.ReservationTimeResDto;
import roomescape.reservationTime.service.ReservationTimeService;

@RestController
public class ReservationTimeController {

    private final ReservationTimeService service;

    public ReservationTimeController(ReservationTimeService service) {
        this.service = service;
    }

    @GetMapping("times")
    public ResponseEntity<List<ReservationTimeResDto>> readAll() {
        List<ReservationTimeResDto> resDtos = service.readAll();
        return ResponseEntity.ok(resDtos);
    }

    @PostMapping("times")
    public ResponseEntity<ReservationTimeResDto> add(@RequestBody ReservationTimeReqDto dto) {
        ReservationTimeResDto resDto = service.add(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(resDto);
    }

    @DeleteMapping("times/{reservationTimeId}")
    public ResponseEntity<Void> delete(@PathVariable("reservationTimeId") Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
