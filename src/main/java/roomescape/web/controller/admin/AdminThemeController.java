package roomescape.web.controller.admin;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.service.ThemeService;
import roomescape.web.dto.ThemeRequest;
import roomescape.web.dto.ThemeResponse;

@RestController
@RequestMapping("/api/admin/themes")
@Validated
@RequiredArgsConstructor
public class AdminThemeController {

    private final ThemeService themeService;

    @PostMapping
    public ResponseEntity<ThemeResponse> register(@Valid @RequestBody ThemeRequest request) {
        ThemeResponse response = themeService.register(request);

        URI uri = URI.create("/api/admin/themes/" + response.id());

        return ResponseEntity.created(uri)
                .body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remove(
            @PathVariable
            @Positive(message = "테마 삭제 식별자는 양수여야 합니다.") Long id
    ) {
        themeService.remove(id);

        return ResponseEntity.noContent().build();
    }
}
