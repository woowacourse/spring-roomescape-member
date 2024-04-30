package roomescape.console.view;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RequestTest {
    @Test
    @DisplayName("방탈출 어드민 요청의 BODY를 반환한다.")
    void body() {
        final String input = "POST/times 10:10";
        final Request request = Request.of(input);

        assertEquals("10:10", request.body().get(0));
    }

    @Test
    @DisplayName("방탈출 어드민 요청의 메소드를 반환한다.")
    void method() {
        final String input = "POST/times 10:10";
        final Request request = Request.of(input);

        assertEquals(RequestMethod.POST_TIME, request.method());
    }
}
