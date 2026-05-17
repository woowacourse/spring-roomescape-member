package roomescape.theme.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import roomescape.theme.controller.dto.ThemeCreateRequest;
import roomescape.theme.controller.dto.ThemeResponse;
import roomescape.theme.service.ThemeService;
import roomescape.theme.service.dto.ThemeResult;

@Tag(name = "admin-theme", description = "테마 관리자 API")
@RestController
@RequestMapping("/admin/themes")
@RequiredArgsConstructor
public class ThemeAdminController {

    private final ThemeService themeService;

    @GetMapping
    @Operation(summary = "전체 테마 조회", description = "관리자 페이지에서 모든 테마 정보를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "전체 테마 조회 성공")
    })
    public List<ThemeResponse> read() {
        return themeService.getAll().stream()
                .map(ThemeResponse::from)
                .toList();
    }

    @PostMapping
    @Operation(summary = "테마 생성", description = "테마 이름, 설명, 썸네일 URL로 새 테마를 생성합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "테마 생성 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 또는 유효성 검증 실패"),
            @ApiResponse(responseCode = "409", description = "중복 테마")
    })
    public ResponseEntity<ThemeResponse> create(@Valid @RequestBody ThemeCreateRequest request) {
        ThemeResult themeResult = themeService.save(
                request.name(),
                request.description(),
                request.thumbnailUrl()
        );

        URI location = URI.create("/admin/themes/" + themeResult.id());

        return ResponseEntity.created(location)
                .body(ThemeResponse.from(themeResult));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "테마 삭제", description = "관리자가 테마 ID로 테마를 삭제합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "테마 삭제 성공"),
            @ApiResponse(responseCode = "409", description = "해당 테마에 예약이 존재하여 삭제 불가")
    })
    public void delete(@PathVariable Long id) {
        themeService.deleteById(id);
    }

}
