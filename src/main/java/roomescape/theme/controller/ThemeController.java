package roomescape.theme.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import roomescape.theme.payload.PopularThemeRequest;
import roomescape.theme.payload.ThemeResponse;
import roomescape.theme.service.ThemeService;

@RestController
@RequestMapping("/themes")
public class ThemeController {

    private final ThemeService themeService;

    public ThemeController(ThemeService themeService) {
        this.themeService = themeService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ThemeResponse> getPopularThemes(@ModelAttribute PopularThemeRequest request) {
        if (request.isEmpty()) {
            return themeService.findAll()
                    .stream()
                    .map(ThemeResponse::from)
                    .toList();
        }
        if (request.isComplete()) {
            return themeService.findPopularThemes(request.days(), request.limits())
                    .stream()
                    .map(ThemeResponse::from)
                    .toList();
        }
        throw new IllegalArgumentException("날짜와 제한은 함께 입력해야 합니다.");
    }
}
