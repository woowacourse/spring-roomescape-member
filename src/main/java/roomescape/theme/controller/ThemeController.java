package roomescape.theme.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import roomescape.global.dto.response.ApiResponse;
import roomescape.theme.dto.ThemeRequest;
import roomescape.theme.dto.ThemeResponse;
import roomescape.theme.dto.ThemesResponse;
import roomescape.theme.service.ThemeService;

import java.time.LocalDate;

@RestController
public class ThemeController {

    private final ThemeService themeService;

    public ThemeController(final ThemeService themeService) {
        this.themeService = themeService;
    }

    @GetMapping("/themes")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<ThemesResponse> getAllThemes() {
        return ApiResponse.success(themeService.findAllThemes());
    }

    // TODO: defaultValue에 제한을 걸던지, 특정 동작만 수행하게 하던지 둘 중 하나로 변경
    @GetMapping("/themes/top")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<ThemesResponse> getTopNThemesBetweenDate(
            @RequestParam(required = false, defaultValue = "10") final int count,
            @RequestParam(required = false, defaultValue = "#{T(java.time.LocalDate).now().minusDays(7)}") final LocalDate startAt,
            @RequestParam(required = false, defaultValue = "#{T(java.time.LocalDate).now().minusDays(1)}") final LocalDate endAt
    ) {
        return ApiResponse.success(themeService.findTopNThemesBetweenDate(count, startAt, endAt));
    }

    @PostMapping("/themes")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<ThemeResponse> saveTheme(
            @RequestBody final ThemeRequest request,
            final HttpServletResponse response
    ) {
        ThemeResponse themeResponse = themeService.addTheme(request);
        response.setHeader(HttpHeaders.LOCATION, "/themes/" + themeResponse.id());

        return ApiResponse.success(themeResponse);
    }

    @DeleteMapping("/themes/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ApiResponse<Void> removeTheme(@PathVariable final Long id) {
        themeService.removeThemeById(id);

        return ApiResponse.success();
    }
}
