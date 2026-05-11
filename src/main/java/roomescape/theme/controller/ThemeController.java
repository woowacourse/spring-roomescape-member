package roomescape.theme.controller;

import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import roomescape.theme.dto.response.ThemeDetailDto;
import roomescape.theme.service.ThemeService;

@RestController
public class ThemeController {
    private final ThemeService themeService;

    public ThemeController(ThemeService themeService) {
        this.themeService = themeService;
    }

    @GetMapping("/themes")
    @Operation(summary = "Read active themes", description = "활성화된 테마를 조회하는 api")
    public ResponseEntity<List<ThemeDetailDto>> getActiveThemes(){
        List<ThemeDetailDto> responseData = themeService.readActiveThemes().stream()
                .map(ThemeDetailDto::from)
                .toList();
        return ResponseEntity.ok(responseData);
    }

    @GetMapping("/themes/popular")
    @Operation(summary = "Read popular themes", description = "인기 테마를 조회하는 api")
    public ResponseEntity<List<ThemeDetailDto>> getPopularThemes(@RequestParam int top){
        List<ThemeDetailDto> responseData = themeService.readPopularThemes(top).stream()
                .map(ThemeDetailDto::from)
                .toList();
        return ResponseEntity.ok(responseData);
    }
}
