package roomescape.theme.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.response.ResponseCode;
import roomescape.theme.dto.ThemeRequestDto;
import roomescape.theme.dto.ThemeResponseDto;
import roomescape.theme.service.ThemeService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/themes")
public class ThemeController {
    private final ThemeService themeService;

    public ThemeController(final ThemeService themeService) {
        this.themeService = themeService;
    }

    @GetMapping
    public List<ThemeResponseDto> findAll() {
        return themeService.findAll();
    }

    @PostMapping
    public ResponseEntity<ThemeResponseDto> save(@RequestBody final ThemeRequestDto request) {
        ThemeResponseDto responseDto = themeService.save(request);
        return ResponseEntity.created(URI.create("/themes/" + responseDto.id())).body(responseDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") final long id) {
        final ResponseCode deletedStatus = themeService.deleteById(id);
        return ResponseEntity.status(deletedStatus.getHttpStatus()).build();
    }

    @GetMapping("/popular")
    public List<ThemeResponseDto> findPopular() {
        return themeService.findPopular();
    }
}
