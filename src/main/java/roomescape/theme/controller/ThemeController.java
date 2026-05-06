package roomescape.theme.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import roomescape.theme.dto.response.ThemeDetailDto;
import roomescape.theme.service.ThemeService;

@RestController
@RequestMapping("/member")
public class ThemeController {
    private final ThemeService themeService;

    public ThemeController(ThemeService themeService) {
        this.themeService = themeService;
    }

    @GetMapping("/themes")
    public ResponseEntity<List<ThemeDetailDto>> getActiveThemes(){
        List<ThemeDetailDto> responseData = themeService.readActiveThemes().stream()
                .map(ThemeDetailDto::from)
                .toList();
        return ResponseEntity.ok(responseData);
    }

    // TODO: 인기 조회 테마 추가
    @GetMapping("/themes/popular")
    public ResponseEntity<List<ThemeDetailDto>> getPopularThemes(@RequestParam int top){
        List<ThemeDetailDto> responseData = themeService.readPopularThemes(top).stream()
                .map(ThemeDetailDto::from)
                .toList();
        return ResponseEntity.ok(responseData);
    }

}
