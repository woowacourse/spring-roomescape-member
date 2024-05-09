package roomescape.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.domain.dto.BookResponses;
import roomescape.service.BookService;

import java.time.LocalDate;

@RestController
@RequestMapping("books")
public class ClientReservationController {
    private final BookService bookService;

    public ClientReservationController(final BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/{date}/{theme_id}")
    public ResponseEntity<BookResponses> read(@PathVariable(value = "date") LocalDate date, @PathVariable(value = "theme_id") Long themeId) {
        return ResponseEntity.ok(bookService.findAvaliableBookList(date, themeId));
    }
}
