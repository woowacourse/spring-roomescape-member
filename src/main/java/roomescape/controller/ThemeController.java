package roomescape.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
@Tag(name = "테마 관리", description = "방탈출 테마 조회, 생성, 삭제 API")
public class ThemeController {

    private final ThemeService themeService;

    public ThemeController(ThemeService themeService) {
        this.themeService = themeService;
    }

    @GetMapping
    @Operation(
            summary = "전체 테마 조회",
            description = "모든 방탈출 테마 정보를 조회합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "테마 목록 조회 성공",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ThemeResponse.class))
                    )
            }
    )
    public ResponseEntity<List<ThemeResponse>> findAll() {
        return ResponseEntity.ok(themeService.findAll());
    }

    @PostMapping
    @Operation(
            summary = "테마 생성",
            description = "새로운 방탈출 테마를 생성합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "테마 생성 성공",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ThemeCreateResponse.class))
                    )
            }
    )
    public ResponseEntity<ThemeCreateResponse> create(
            @RequestBody @Valid final ThemeCreateRequest themeCreateRequest) {
        final ThemeCreateResponse themeCreateResponse = themeService.create(themeCreateRequest);

        final URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(themeCreateResponse.id())
                .toUri();
        return ResponseEntity.created(location).body(themeCreateResponse);
    }

    @DeleteMapping("{id}")
    @Operation(
            summary = "테마 삭제",
            description = "ID로 테마를 삭제합니다. 해당 테마에 예약이 있으면 삭제할 수 없습니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "테마 삭제 성공"
                    )
            }
    )
    public ResponseEntity<Void> delete(
            @Parameter(description = "삭제할 테마 ID") @PathVariable final Long id) {
        themeService.deleteIfNoReservation(id);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("rank")
    @Operation(
            summary = "인기 테마 조회",
            description = "최근 7일 간의 인기 테마를 조회합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "인기 테마 조회 성공",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ThemeResponse.class))
                    )
            }
    )
    public ResponseEntity<List<ThemeResponse>> findPopularThemesInRecentSevenDays() {
        return ResponseEntity.ok(themeService.findPopularThemesInRecentSevenDays());
    }
}
