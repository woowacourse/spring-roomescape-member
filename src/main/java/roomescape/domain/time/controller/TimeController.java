package roomescape.domain.time.controller;

import java.time.LocalDate;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import roomescape.domain.time.dto.request.TimeCreateRequestDTO;
import roomescape.domain.time.dto.response.TimeResponseDTO;
import roomescape.domain.time.service.TimeService;

@RestController
@RequestMapping("/api")
public class TimeController {

    private final TimeService timeService;

    public TimeController(TimeService timeService) {
        this.timeService = timeService;
    }

    @GetMapping("/admin/times")
    public ResponseEntity<List<TimeResponseDTO>> getTimes() {
        return ResponseEntity.ok(timeService.getTimes());
    }

    @GetMapping("/times")
    public ResponseEntity<List<TimeResponseDTO>> getAvailableTimes(
        @RequestParam LocalDate date,
        @RequestParam Long themeId
    ) {
        return ResponseEntity.ok(timeService.getAvailableTimes(date, themeId));
    }

    @PostMapping("/admin/times")
    public ResponseEntity<TimeResponseDTO> saveTime(@RequestBody TimeCreateRequestDTO requestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(timeService.saveTime(requestDTO));
    }

    @DeleteMapping("/admin/times/{id}")
    public ResponseEntity<Void> deleteTime(@PathVariable Long id) {
        timeService.deleteTimeById(id);
        return ResponseEntity.ok().build();
    }
}
