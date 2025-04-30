package roomescape.presentation.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.business.service.PlayTimeService;
import roomescape.presentation.dto.PlayTimeRequest;
import roomescape.presentation.dto.PlayTimeResponse;

@RestController
@RequestMapping("/times")
public class PlayTimeController {

    private final PlayTimeService playTimeService;

    public PlayTimeController(final PlayTimeService playTimeService) {
        this.playTimeService = playTimeService;
    }

    @PostMapping
    public ResponseEntity<PlayTimeResponse> create(
            @RequestBody final PlayTimeRequest playTimeRequest
    ) {
        try {
            final PlayTimeResponse playTimeResponse = playTimeService.create(playTimeRequest);
            return ResponseEntity.ok(playTimeResponse);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<PlayTimeResponse>> readAll() {
        final List<PlayTimeResponse> playTimeRespons = playTimeService.findAll();

        return ResponseEntity.ok(playTimeRespons);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") final Long id) {
        try {
            playTimeService.remove(id);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok().build();
    }
}
