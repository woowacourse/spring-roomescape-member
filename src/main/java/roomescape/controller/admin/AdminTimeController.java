package roomescape.controller.admin;

import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.domain.Time;
import roomescape.dto.request.TimeRequestDto;
import roomescape.dto.response.TimeResponseDto;
import roomescape.service.TimeService;

@RestController
@RequestMapping("/admin/times")
public class AdminTimeController {
    private final TimeService timeService;

    public AdminTimeController(TimeService timeService) {
        this.timeService = timeService;
    }

    @PostMapping
    public ResponseEntity<TimeResponseDto> create(@Valid @RequestBody TimeRequestDto timeRequest) {
        Time time = timeService.create(timeRequest);
        return ResponseEntity.ok(TimeResponseDto.from(time));
    }

    @GetMapping
    public ResponseEntity<List<TimeResponseDto>> findAll() {
        List<Time> times = timeService.findAll();
        return ResponseEntity.ok(times.stream()
                .map(TimeResponseDto::from)
                .toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TimeResponseDto> findById(@PathVariable Long id) {
        Time timeById = timeService.findById(id);
        return ResponseEntity.ok(TimeResponseDto.from(timeById));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@Valid @PathVariable Long id) {
        timeService.delete(id);
        return ResponseEntity.ok().build();
    }
}
