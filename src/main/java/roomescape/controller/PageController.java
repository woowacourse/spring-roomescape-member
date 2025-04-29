package roomescape.controller;

import java.net.URI;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {
    @GetMapping("/")
    public ResponseEntity<Void> getWelcomePage() {
        HttpHeaders header = new HttpHeaders();
        header.setLocation(URI.create("/admin"));
        return new ResponseEntity<>(header, HttpStatus.PERMANENT_REDIRECT);
    }

    @GetMapping("/admin")
    public String getMainPage() {
        return "admin/index";
    }

    @GetMapping("/admin/reservation")
    public String getReservationPage() {
        return "admin/reservation-new";
    }

    @GetMapping("/admin/time")
    public String getTimePage() {
        return "admin/time";
    }

    @GetMapping("/admin/themes")
    public String getThemePage() {
        return "admin/theme";
    }
}
