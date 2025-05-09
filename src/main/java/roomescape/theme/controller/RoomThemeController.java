package roomescape.theme.controller;

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
import roomescape.theme.controller.dto.CreateRoomThemeRequest;
import roomescape.theme.service.dto.CreateRoomThemeServiceRequest;
import roomescape.theme.controller.dto.PopularThemeResponse;
import roomescape.theme.domain.RoomTheme;
import roomescape.theme.controller.dto.RoomThemeResponse;
import roomescape.theme.service.RoomThemeService;

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
        final CreateRoomThemeServiceRequest creation = CreateRoomThemeServiceRequest.from(
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
