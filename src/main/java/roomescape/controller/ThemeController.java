package roomescape.controller;

import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import roomescape.domain.Theme;
import roomescape.dto.ResourceIdResponse;
import roomescape.dto.theme.PopularThemesResponse;
import roomescape.dto.theme.ThemeRequest;
import roomescape.dto.theme.ThemeResponse;
import roomescape.exception.ForbiddenAccessException;
import roomescape.service.ThemeService;

@RestController
@RequestMapping("/themes")
public class ThemeController {

    private final ThemeService themeService;

    public ThemeController(ThemeService themeService) {
        this.themeService = themeService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResourceIdResponse addTheme(
        @Valid @RequestBody ThemeRequest request,
        @RequestParam(value = "role", required = false) String role
    ) {
        if (!"admin".equals(role)) {
            throw new ForbiddenAccessException("테마 추가는 관리자만 가능합니다.");
        }

        Theme saved = themeService.addTheme(request);
        return new ResourceIdResponse(saved.getId());
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTheme(
        @PathVariable Long id,
        @RequestParam(value = "role", required = false) String role
    ) {
        if (!"admin".equals(role)) {
            throw new ForbiddenAccessException("테마 삭제는 관리자만 가능합니다.");
        }

        themeService.deleteThemeById(id);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ThemeResponse> findAll() {
        return themeService.getThemes().stream()
            .map(ThemeResponse::from)
            .toList();
    }

    // TODO: uri 선언 시 / 로 시작할지? 없이 시작할지?
    @GetMapping("/popular/week")
    @ResponseStatus(HttpStatus.OK)
    public PopularThemesResponse findWeekPopularThemesOrderByRank(
        @RequestParam("limit") final int limit
    ) {
        List<Theme> themes = themeService.findWeekPopularThemesOrderByRank(limit);
        return PopularThemesResponse.from(themes);
    }
}
