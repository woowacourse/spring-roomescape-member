package roomescape.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.dto.ThemeResponse;
import roomescape.service.ThemeService;

@RequestMapping("/theme")
@RestController
public class ThemeController {

    private final ThemeService userThemeService;

    public ThemeController(ThemeService userThemeService) {
        this.userThemeService = userThemeService;
    }
    
    /*
    사용자 테마 조회 기능	GET /themes	-	200 [{id, name, description, thumbnail_url}]
    사용자 테마 시간 조회 기능	GET /themes/{id}/times	-	200 {date:"2025-06-06", times:[{id: 1, start_at:"10:00", available:true},...]}
     */
    
    @GetMapping
    public ResponseEntity<List<ThemeResponse>> getAllThemes() {
        final List<ThemeResponse> themes = userThemeService.getAllThemes();
        return ResponseEntity.ok(themes);
    }
}
