package roomescape.controller;

import jakarta.validation.Valid;
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
import roomescape.controller.dto.ResourceIdResponseDto;
import roomescape.controller.dto.theme.PopularThemesResponseDto;
import roomescape.controller.dto.theme.ThemeRequestDto;
import roomescape.controller.dto.theme.ThemeResponseDto;
import roomescape.controller.dto.theme.ThemesResponseDto;
import roomescape.exception.BusinessException;
import roomescape.exception.ErrorCode;
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
    public ResourceIdResponseDto addTheme(
        @Valid @RequestBody ThemeRequestDto request,
        @RequestParam(value = "role", required = false) String role
    ) {
        if (!"admin".equals(role)) {
            throw new BusinessException(ErrorCode.ADMIN_ROLE_REQUIRED);
        }

        Theme saved = themeService.addTheme(request.toCommand());
        return new ResourceIdResponseDto(saved.getId());
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTheme(
        @PathVariable Long id,
        @RequestParam(value = "role", required = false) String role
    ) {
        if (!"admin".equals(role)) {
            throw new BusinessException(ErrorCode.ADMIN_ROLE_REQUIRED);
        }

        themeService.deleteThemeById(id);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ThemesResponseDto findAll() {
        return new ThemesResponseDto(themeService.getThemes().stream()
            .map(ThemeResponseDto::from)
            .toList());
    }

    @GetMapping("/popular/week")
    @ResponseStatus(HttpStatus.OK)
    public PopularThemesResponseDto findWeekPopularThemesOrderByRank(
        @RequestParam("limit") final int limit
    ) {
        return PopularThemesResponseDto.from(themeService.findWeekPopularThemesOrderByRank(limit));
    }
}
