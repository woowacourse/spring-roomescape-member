package roomescape.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.dto.LoginMember;
import roomescape.dto.request.MemberReservationRequest;
import roomescape.dto.request.ReservationRequest;
import roomescape.dto.response.BookResponse;
import roomescape.dto.response.ReservationResponse;
import roomescape.service.BookService;
import roomescape.service.ReservationService;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/books")
public class ClientReservationController {
    private final BookService bookService;
    private final ReservationService reservationService;

    public ClientReservationController(final BookService bookService, final ReservationService reservationService) {
        this.bookService = bookService;
        this.reservationService = reservationService;
    }

    @GetMapping("/{date}/{theme_id}")
    public ResponseEntity<List<BookResponse>> read(@PathVariable final LocalDate date,
                                                   @PathVariable(value = "theme_id") final Long themeId) {
        List<BookResponse> books = bookService.findAvaliableBookList(date, themeId);
        return ResponseEntity.ok(books);
    }

    @PostMapping("/reservations")
    public ResponseEntity<ReservationResponse> create(@Valid @RequestBody final MemberReservationRequest memberRequest,
                                                      final LoginMember member) {
        ReservationRequest reservationRequest = new ReservationRequest(member.id(), memberRequest.date(),
                memberRequest.timeId(), memberRequest.themeId());
        ReservationResponse reservationResponse = reservationService.create(reservationRequest);
        return ResponseEntity.created(URI.create("/reservations/" + reservationResponse.id())).body(reservationResponse);
    }
}
