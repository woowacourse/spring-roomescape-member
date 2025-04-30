package roomescape.controller;

import java.net.URI;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserPageController {
    @GetMapping("/")
    public ResponseEntity<Void> getWelcomePage() {
        HttpHeaders header = new HttpHeaders();
        header.setLocation(URI.create("/reservation"));
        return new ResponseEntity<>(header, HttpStatus.PERMANENT_REDIRECT);
    }

    @GetMapping("/reservation")
    public String getReservationPage() {
        return "reservation";
    }
}
