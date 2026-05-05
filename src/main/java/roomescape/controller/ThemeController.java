package roomescape.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.service.ThemeService;
import roomescape.service.dto.request.ThemeCreateRequest;
import roomescape.service.dto.response.ThemeResponse;

@RestController
@RequestMapping("/themes")
@RequiredArgsConstructor
public class ThemeController {

    private final ThemeService themeService;

    @PostMapping
    public ResponseEntity<ThemeResponse> create(
            @RequestBody ThemeCreateRequest request
    ) {
        final ThemeResponse result = themeService.create(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(result);
    }

    @DeleteMapping("/{theme-id}")
    public ResponseEntity<Void> delete(
            @PathVariable("theme-id") Long themeId
    ) {
        themeService.delete(themeId);
        return ResponseEntity.noContent().build();
    }
}
