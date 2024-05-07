package roomescape.advice.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class GlobalExceptionHandlerTestController {

    @GetMapping("/test/illegalArgumentException")
    public String throwIllegalArgumentException() {
        throw new IllegalArgumentException("예외 메시지");
    }

    @GetMapping("/test/nullPointerException")
    public String throwNullPointerException() {
        throw new NullPointerException();
    }

    @GetMapping("/test/unexpectedException")
    public String throwUnexpectedException() {
        throw new UnsupportedOperationException();
    }
}
