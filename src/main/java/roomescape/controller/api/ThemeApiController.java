package roomescape.controller.api;

import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import roomescape.controller.api.dto.request.ThemeCreateRequest;
import roomescape.controller.api.dto.response.ThemeResponse;
import roomescape.service.ThemeService;
import roomescape.service.dto.output.ThemeOutput;

@Controller
@RequestMapping("/themes")
public class ThemeApiController {

    private final ThemeService themeService;

    public ThemeApiController(ThemeService themeService) {
        this.themeService = themeService;
    }

    @PostMapping
    public ResponseEntity<ThemeResponse> createTheme(@RequestBody ThemeCreateRequest request) {
        ThemeOutput output = themeService.createTheme(request.toInput());
        return ResponseEntity.created(URI.create("/times/" + output.id()))
                .body(ThemeResponse.toResponse(output));
    }

    @GetMapping
    public ResponseEntity<List<ThemeResponse>> getAllThemes() {
        List<ThemeOutput> outputs = themeService.getAllThemes();
        return ResponseEntity.ok(ThemeResponse.toResponses(outputs));
    }

    @GetMapping("/popular")
    public ResponseEntity<List<ThemeResponse>> getPopularThemes(@RequestParam String date) {
        List<ThemeOutput> outputs = themeService.getPopularThemes(date);
        return ResponseEntity.ok().body(ThemeResponse.toResponses(outputs));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTheme(@PathVariable long id) {
        themeService.deleteTheme(id);
        return ResponseEntity.noContent().build();
    }
}
