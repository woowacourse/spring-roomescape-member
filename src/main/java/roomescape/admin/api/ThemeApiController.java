package roomescape.admin.api;

import jakarta.validation.Valid;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.admin.api.dto.ThemeRequest;
import roomescape.admin.api.dto.ThemeResponse;
import roomescape.global.auth.Accessor;
import roomescape.global.auth.CustomPrincipal;
import roomescape.service.ThemeService;
import roomescape.service.result.ThemeRegisterResult;

@RequestMapping("/admin/themes")
@RestController
@RequiredArgsConstructor
public class ThemeApiController {

    private final ThemeService themeService;

    @PostMapping
    public ResponseEntity<ThemeResponse> register(
            @CustomPrincipal Accessor accessor,
            @Valid @RequestBody ThemeRequest request
    ) {
        ThemeRegisterResult result = themeService.register(accessor, request.toCommand());

        URI uri = URI.create("/admin/themes/" + result.id());

        return ResponseEntity.created(uri)
                .body(ThemeResponse.from(result));
    }
}
