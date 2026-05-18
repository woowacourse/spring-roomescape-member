package roomescape.handler;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @RequestMapping("/exception-handling-test")
    public void throwException() {
    }

    @GetMapping("/binding-test/missing-param")
    public void missingParam(@RequestParam String requiredParam) {
    }

    @PostMapping("/binding-test/validation")
    public void validationBody(@Valid @RequestBody DummyRequest request) {
    }

    public record DummyRequest(@NotBlank(message = "이름은 필수입니다.") String name) {
    }
}
