package roomescape.domain.reservation.controller;

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
import roomescape.domain.reservation.dto.ThemeRequest;
import roomescape.domain.reservation.dto.ThemeResponse;
import roomescape.domain.reservation.service.ThemeService;

@RequestMapping("/themes")
@RestController
public class ThemeController {

    private final ThemeService themeService;

    public ThemeController(ThemeService themeService) {
        this.themeService = themeService;
    }

    @GetMapping
    public ResponseEntity<List<ThemeResponse>> readAllReservationTimes() {
        List<ThemeResponse> responses = themeService.getAll();

        return ResponseEntity.ok(responses);
    }

    @PostMapping
    public ResponseEntity<ThemeResponse> create(@Valid @RequestBody ThemeRequest request) {

        ThemeResponse response = themeService.create(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        themeService.delete(id);

        return ResponseEntity.noContent().build();
    }
}
