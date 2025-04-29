package roomescape.controller;

import java.net.URI;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping("/")
    public ResponseEntity<Void> getWelcomePage() {
        return ResponseEntity
                .status(HttpStatus.PERMANENT_REDIRECT.value())
                .location(URI.create("/admin")).build();
    }

    @GetMapping("/admin")
    public String getMainPage() {
        return "admin/index";
    }

    @GetMapping("/admin/reservation")
    public String getReservationPage() {
        return "admin/reservation";
    }

    @GetMapping("/admin/time")
    public String getTimePage() {
        return "admin/time";
    }
}
