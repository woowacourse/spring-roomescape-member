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
import roomescape.controller.dto.response.RoomThemeResponse;
import roomescape.service.RoomThemeService;
import roomescape.service.dto.request.RoomThemeCreation;
import roomescape.service.dto.response.PopularThemeResult;
import roomescape.service.dto.response.RoomThemeResult;

@RequestMapping("/themes")
@RestController
public class RoomThemeController {

    private final RoomThemeService roomThemeService;

    public RoomThemeController(RoomThemeService roomThemeService) {
        this.roomThemeService = roomThemeService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RoomThemeResponse addTheme(@RequestBody CreateThemeRequest request) {
        final RoomThemeCreation creation = RoomThemeCreation.from(request);
        RoomThemeResult roomThemeResult = roomThemeService.addTheme(creation);
        return RoomThemeResponse.from(roomThemeResult);
    }

    @GetMapping
    public List<RoomThemeResponse> findAllThemes() {
        return roomThemeService.findAllThemes()
                .stream()
                .map(RoomThemeResponse::from)
                .toList();
    }

    @GetMapping("/popular")
    public List<PopularThemeResult> findPopularThemes() {
        return roomThemeService.findPopularThemes();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTheme(@PathVariable long id) {
        roomThemeService.deleteTheme(id);
    }
}
