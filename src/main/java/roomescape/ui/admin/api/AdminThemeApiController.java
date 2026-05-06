package roomescape.ui.admin.api;

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
import roomescape.service.result.ThemeRegisterResult;
import roomescape.ui.admin.api.dto.AdminThemeRequest;
import roomescape.ui.admin.api.dto.AdminThemeResponse;

@RequestMapping("/api/admin/themes")
@RestController
@RequiredArgsConstructor
@Validated
public class AdminThemeApiController {

    private final ThemeService themeService;

    @PostMapping
    public ResponseEntity<AdminThemeResponse> register(@Valid @RequestBody AdminThemeRequest request) {
        ThemeRegisterResult result = themeService.register(request.toCommand());

        URI uri = URI.create("/api/admin/themes/" + result.id());

        return ResponseEntity.created(uri)
                .body(AdminThemeResponse.from(result));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<AdminThemeResponse> remove(
            @PathVariable
            @Positive(message = "테마 삭제 식별자는 양수여야 합니다.") Long id
    ) {
        themeService.remove(id);
        return ResponseEntity.noContent().build();
    }
}
