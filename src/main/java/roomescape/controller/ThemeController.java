package roomescape.controller;

import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import roomescape.dto.request.ThemeCreateRequest;
import roomescape.dto.response.ThemeCreateResponse;
import roomescape.dto.response.ThemeResponse;
import roomescape.service.ThemeService;

@RestController
@RequestMapping("/themes")
public class ThemeController {

    private final ThemeService themeService;

    public ThemeController(final ThemeService themeService) {
        this.themeService = themeService;
    }

    @GetMapping
    public List<ThemeResponse> findAll() {
        return themeService.findAll();
    }

    @PostMapping
    public ResponseEntity<ThemeCreateResponse> create(@RequestBody ThemeCreateRequest themeCreateRequest) {
        ThemeCreateResponse themeCreateResponse = themeService.create(themeCreateRequest);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(themeCreateResponse.id())
                .toUri();
        return ResponseEntity.created(location).body(themeCreateResponse);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        themeService.deleteIfNoReservation(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("rank")
    public List<ThemeResponse> findPopularThemesInLastWeek() {
        return themeService.findPopularThemesInLastWeek();
    }


}
