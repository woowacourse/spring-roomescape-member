package roomescape.theme.ui;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.auth.domain.RequiresRole;
import roomescape.member.domain.UserRole;
import roomescape.theme.applcation.ThemeService;
import roomescape.theme.ui.dto.CreateThemeRequest;
import roomescape.theme.ui.dto.CreateThemeResponse;
import roomescape.theme.ui.dto.ThemeResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/themes")
public class ThemeRestController {

    private final ThemeService themeService;

    @PostMapping
    @RequiresRole(userRoles = {UserRole.ADMIN})
    public ResponseEntity<CreateThemeResponse> create(
            @RequestBody final CreateThemeRequest request
    ) {
        final CreateThemeResponse response = themeService.create(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
    }

    @DeleteMapping({"/{id}"})
    @RequiresRole(userRoles = {UserRole.ADMIN})
    public ResponseEntity<Void> delete(
            @PathVariable final Long id
    ) {
        themeService.delete(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @RequiresRole(userRoles = {UserRole.ADMIN, UserRole.MEMBER, UserRole.GUEST})
    public ResponseEntity<List<ThemeResponse>> findAll() {
        final List<ThemeResponse> themeResponses = themeService.findAll();

        return ResponseEntity.ok(themeResponses);
    }

    @GetMapping("/popular-list")
    @RequiresRole(userRoles = {UserRole.ADMIN, UserRole.MEMBER, UserRole.GUEST})
    public ResponseEntity<List<ThemeResponse>> findPopularThemes() {
        final List<ThemeResponse> popularThemes = themeService.findPopularThemes();

        return ResponseEntity.ok(popularThemes);
    }
}
