package roomescape.controller.api;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import roomescape.controller.dto.request.CreateThemeRequest;
import roomescape.controller.dto.response.PopularThemeResponse;
import roomescape.controller.dto.response.RoomThemeResponse;
import roomescape.service.RoonThemeService;
import roomescape.service.dto.RoomThemeCreation;

@RequestMapping("/themes")
@RestController
public class RoomThemeController {

    private final RoonThemeService roonThemeService;

    public RoomThemeController(RoonThemeService roonThemeService) {
        this.roonThemeService = roonThemeService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RoomThemeResponse addTheme(@RequestBody CreateThemeRequest request) {
        final RoomThemeCreation creation = RoomThemeCreation.from(request);
        return roonThemeService.addTheme(creation);
    }

    @GetMapping
    public List<RoomThemeResponse> findAllThemes() {
        return roonThemeService.findAllThemes();
    }

    @GetMapping("/popular")
    public List<PopularThemeResponse> findPopularThemes() {
        return roonThemeService.findPopularThemes();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTheme(@PathVariable long id) {
        roonThemeService.deleteTheme(id);
    }
}
