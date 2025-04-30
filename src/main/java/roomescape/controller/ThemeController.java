package roomescape.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import roomescape.dto.ThemeRequestDto;
import roomescape.model.Theme;
import roomescape.service.ThemeService;

@Controller
@RequestMapping("/themes")
public class ThemeController {

    private final ThemeService themeService;

    public ThemeController(ThemeService themeService) {
        this.themeService = themeService;
    }
//    @PostMapping()
//    public ResponseEntity<Reservation> addReservation(@RequestBody ReservationRequestDto reservationRequestDto) {
//        return ResponseEntity.status(HttpStatus.CREATED).body(reservationService.addReservation(reservationRequestDto));
//    }
    @PostMapping
    public ResponseEntity<Theme> addTheme(@RequestBody ThemeRequestDto themeRequestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(themeService.addTheme(themeRequestDto));
    }
}
