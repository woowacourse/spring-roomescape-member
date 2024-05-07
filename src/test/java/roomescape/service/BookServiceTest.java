package roomescape.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import roomescape.domain.dto.BookResponse;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class BookServiceTest {
    private BookService bookService;

    @Autowired
    public BookServiceTest(final BookService bookService) {
        this.bookService = bookService;
    }

    @DisplayName("예약 가능한 시간 목록들을 반환한다.")
    @Test
    void given_when_findAvaliableBookList_thenReturnBookResponse() {
        //when
        final List<BookResponse> bookResponses = bookService.findAvaliableBookList(LocalDate.parse("2099-05-08"), 1L);
        //then
        assertThat(bookResponses.size()).isEqualTo(4);
    }
}
