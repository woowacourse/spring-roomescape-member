package roomescape.controller.admin;

import jakarta.validation.Valid;
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

import java.util.List;

@RestController
@RequestMapping("/admin/times")
public class AdminTimeController {
    private final TimeService timeService;

    public AdminTimeController(TimeService timeService) {
        this.timeService = timeService;
    }

    @PostMapping
    public ResponseEntity<TimeResponseDto> create(@Valid @RequestBody TimeRequestDto timeRequest) {
        return ResponseEntity.ok(timeService.create(timeRequest));
    }

    @GetMapping
    public ResponseEntity<List<TimeResponseDto>> findAll() {
        return ResponseEntity.ok(timeService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TimeResponseDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(timeService.findById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@Valid @PathVariable Long id) {
        timeService.delete(id);
        return ResponseEntity.ok().build();
    }
}
