package roomescape.time.controller.admin;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.time.service.dto.request.ReservationTimeRequest;
import roomescape.time.service.dto.response.ReservationTimeResponse;
import roomescape.time.service.ReservationTimeService;

import java.util.List;

@RestController
@RequestMapping("/times")
public class ReservationTimeController {
    private final ReservationTimeService service;

    public ReservationTimeController(ReservationTimeService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ReservationTimeResponse> create(@RequestBody @Valid ReservationTimeRequest requestDto) {
        ReservationTimeResponse responseDto = service.create(requestDto);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping
    public List<ReservationTimeResponse> getAllTimes() {
        return service.getAllTimes();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") @NotNull final Long id) {
        service.delete(id);
        return ResponseEntity.ok().build();
    }
}
