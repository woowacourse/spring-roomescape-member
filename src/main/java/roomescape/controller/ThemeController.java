package roomescape.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.dto.request.ThemeCreateRequest;
import roomescape.dto.response.ThemeResponse;
import roomescape.service.ThemeService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/themes")
public class ThemeController {
    private final ThemeService themeService;

    @Autowired
    public ThemeController(ThemeService themeService) {
        this.themeService = themeService;
    }

    @PostMapping
    public ResponseEntity<ThemeResponse> create(@RequestBody ThemeCreateRequest themeCreateRequest) {
        ThemeResponse response = themeService.createTheme(themeCreateRequest);
        URI location = URI.create("/themes/" + response.id());
        return ResponseEntity.created(location).body(response);
    }

    @GetMapping
    public ResponseEntity<List<ThemeResponse>> read() {
        List<ThemeResponse> themes = themeService.findAll();
        return ResponseEntity.ok(themes);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        themeService.deleteThemeById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/lists")
    public ResponseEntity<List<ThemeResponse>> readLists(
            @RequestParam(value = "order_type", required = false) String orderType,
            @RequestParam(value = "list_num", required = false) Long listNum
    ) {
        orderType = "popular_desc";
        listNum = 10L;
        List<ThemeResponse> listedTheme = themeService.findLimitedThemesByPopularDesc(orderType, listNum);
        return ResponseEntity.ok(listedTheme);
    }
}
