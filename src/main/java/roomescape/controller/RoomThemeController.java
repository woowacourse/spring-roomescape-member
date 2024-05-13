package roomescape.controller;

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
import roomescape.dto.ExceptionDto;
import roomescape.dto.request.RoomThemeRequest;
import roomescape.dto.response.RoomThemeResponse;
import roomescape.service.RoomThemeService;

@RestController
@RequestMapping("/themes")
public class RoomThemeController {
    private final RoomThemeService roomThemeService;

    public RoomThemeController(RoomThemeService roomThemeService) {
        this.roomThemeService = roomThemeService;
    }

    @GetMapping
    public ResponseEntity<List<RoomThemeResponse>> findAllRoomThemes() {
        return ResponseEntity.ok(roomThemeService.findAll());
    }

    @GetMapping("/ranking")
    public ResponseEntity<List<RoomThemeResponse>> findAllRoomThemesRanking() {
        return ResponseEntity.ok(roomThemeService.findByRanking());
    }

    @PostMapping
    public ResponseEntity<RoomThemeResponse> createRoomTheme(
            @Valid @RequestBody RoomThemeRequest roomThemeRequest) {
        RoomThemeResponse roomThemeResponse = roomThemeService.save(roomThemeRequest);
        return ResponseEntity.created(URI.create("/themes" + roomThemeResponse.id()))
                .body(roomThemeResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ExceptionDto> deleteRoomTheme(@PathVariable Long id) {
        roomThemeService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
