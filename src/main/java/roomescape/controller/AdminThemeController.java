package roomescape.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.domain.Theme;
import roomescape.dto.CreateThemeRequest;
import roomescape.service.ThemeService;

@Tag(name = "관리자 - 테마 관리", description = "관리자용 테마 생성·삭제 API")
@RestController
@RequestMapping("/admin/themes")
public class AdminThemeController {

    private final ThemeService themeService;

    public AdminThemeController(ThemeService themeService) {
        this.themeService = themeService;
    }

    @Operation(summary = "테마 생성", description = "새로운 방탈출 테마를 생성합니다.")
    @ApiResponse(responseCode = "201", description = "테마 생성 성공")
    @PostMapping
    public ResponseEntity<Void> createTheme(@RequestBody CreateThemeRequest createThemeRequest) {
        Theme createdTheme = themeService.createTheme(createThemeRequest);
        URI location = URI.create("/admin/themes/" + createdTheme.getId());
        return ResponseEntity.created(location).build();
    }

    @Operation(summary = "테마 삭제", description = "ID로 테마를 삭제합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "테마 삭제 성공"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 테마 ID")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTheme(
            @Parameter(description = "삭제할 테마 ID", example = "1")
            @PathVariable Long id) {
        themeService.deleteTheme(id);
        return ResponseEntity.noContent().build();
    }
}
