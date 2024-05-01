package roomescape.controller;

import java.net.URI;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import roomescape.controller.dto.request.ThemeCreateRequest;
import roomescape.controller.dto.response.ThemeCreateResponse;
import roomescape.service.ThemeService;
import roomescape.service.dto.output.ThemeOutput;

@Controller
@RequestMapping("/themes")
public class ThemeController {

    private final ThemeService themeService;

    public ThemeController(ThemeService themeService) {
        this.themeService = themeService;
    }

    @PostMapping
    public ResponseEntity<ThemeCreateResponse> createReservationTime(@RequestBody ThemeCreateRequest request) {
        ThemeOutput output = themeService.createTheme(request.toInput());
        return ResponseEntity.created(URI.create("/times/" + output.id()))
                .body(ThemeCreateResponse.toResponse(output));
    }

    @ExceptionHandler(value = IllegalArgumentException.class)
    public ResponseEntity<String> handleArgumentException(IllegalArgumentException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

}
