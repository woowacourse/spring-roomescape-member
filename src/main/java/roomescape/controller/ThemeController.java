package roomescape.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.dto.ThemeRequest;
import roomescape.dto.ThemeResponse;
import roomescape.exception.ThemeExistException;
import roomescape.service.ThemeService;

import java.util.List;

@RestController
@RequestMapping("/themes")
public class ThemeController {

    private final ThemeService themeService;

    public ThemeController(ThemeService themeService) {
        this.themeService = themeService;
    }

    @PostMapping
    public ResponseEntity<ThemeResponse> add(@Valid @RequestBody ThemeRequest requestDto) {
        return new ResponseEntity<>(themeService.add(requestDto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ThemeResponse>> findAll() {
        return ResponseEntity.ok(themeService.findAll());
    }

    @GetMapping("/rank")
    public ResponseEntity<List<ThemeResponse>> sortByRank() {
        return ResponseEntity.ok(themeService.sortByRank());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        themeService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(ThemeExistException.class)
    public ResponseEntity<ProblemDetail> handleReservationExistException(ThemeExistException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.CONFLICT);
        problemDetail.setDetail(e.getMessage());

        return ResponseEntity.status(HttpStatus.CONFLICT).body(problemDetail);
    }
}
