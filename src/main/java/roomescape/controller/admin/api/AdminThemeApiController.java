package roomescape.controller.admin.api;

import static org.springframework.http.HttpStatus.CREATED;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.controller.admin.api.dto.AdminThemeRequest;
import roomescape.controller.admin.api.dto.AdminThemeResponse;
import roomescape.service.ThemeService;
import roomescape.service.result.ThemeRegisterResult;

@RequestMapping("/api/admin/themes")
@RestController
@RequiredArgsConstructor
@Validated
public class AdminThemeApiController {

    private final ThemeService themeService;

    @PostMapping
    public ResponseEntity<AdminThemeResponse> register(@Valid @RequestBody AdminThemeRequest request) {
        ThemeRegisterResult result = themeService.register(request.toCommand());
        return ResponseEntity.status(CREATED).body(AdminThemeResponse.from(result));
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
