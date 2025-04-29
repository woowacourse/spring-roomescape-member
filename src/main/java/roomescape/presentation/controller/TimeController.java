package roomescape.presentation.controller;

import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.presentation.dto.request.TimeRequest;
import roomescape.presentation.dto.response.ReservationResponse;
import roomescape.presentation.dto.response.TimeResponse;
import roomescape.application.TimeService;

@RestController
@RequestMapping("/times")
public class TimeController {

    private final TimeService service;

    public TimeController(TimeService service) {
        this.service = service;
    }

    @GetMapping
    public List<TimeResponse> getAllTimes() {
        return service.getAllTimes();
    }

    @PostMapping
    public ResponseEntity<TimeResponse> addTime(@Valid @RequestBody TimeRequest request) {
        TimeResponse timeResponse = service.registerNewTime(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(timeResponse);
    }

    @DeleteMapping("/{id}")
    public void deleteTime(@PathVariable(name = "id") Long id) {
        service.deleteTime(id);
    }
}
