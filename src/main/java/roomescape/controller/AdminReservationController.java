package roomescape.controller;

import jakarta.validation.Valid;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.domain.LoginMember;
import roomescape.dto.reservation.ReservationRequest;
import roomescape.dto.reservation.ReservationResponse;
import roomescape.exception.UnauthorizedAccessException;
import roomescape.service.BookService;

@RestController
public class AdminReservationController {

    private final BookService bookService;

    public AdminReservationController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping("/admin/reservations")
    public ResponseEntity<ReservationResponse> addReservation(@Valid @RequestBody ReservationRequest request, LoginMember member) {
        if (member.getRole().equalsIgnoreCase("USER")) {
            throw new UnauthorizedAccessException("[ERROR] 접근 권한이 없습니다.");
        }

        ReservationResponse responseDto = bookService.createAdminReservation(request);
        return ResponseEntity.created(URI.create("reservations/" + responseDto.id())).body(responseDto);
    }
}
