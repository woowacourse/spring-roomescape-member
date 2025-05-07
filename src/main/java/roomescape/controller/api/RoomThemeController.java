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
import roomescape.controller.dto.request.CreateRoomThemeRequest;
import roomescape.controller.dto.response.PopularThemeResponse;
import roomescape.controller.dto.response.RoomThemeResponse;
import roomescape.domain.roomtheme.RoomTheme;
import roomescape.service.RoomThemeService;

@RequestMapping("/themes")
@RestController
public class RoomThemeController {

    private final RoomThemeService roomThemeService;

    public RoomThemeController(final RoomThemeService roomThemeService) {
        this.roomThemeService = roomThemeService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RoomThemeResponse addTheme(@RequestBody CreateRoomThemeRequest request) {
        final roomescape.service.dto.CreateRoomThemeRequest creation = roomescape.service.dto.CreateRoomThemeRequest.from(
                request);
        final RoomTheme savedTheme = roomThemeService.addTheme(creation);
        return RoomThemeResponse.from(savedTheme);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<RoomThemeResponse> findAllThemes() {
        return roomThemeService.findAllThemes()
                .stream()
                .map(RoomThemeResponse::from)
                .toList();
    }

    @GetMapping("/popular")
    @ResponseStatus(HttpStatus.OK)
    public List<PopularThemeResponse> findPopularThemes() {
        return roomThemeService.findPopularThemes()
                .stream()
                .map(PopularThemeResponse::from)
                .toList();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTheme(@PathVariable long id) {
        roomThemeService.deleteTheme(id);
    }
}
