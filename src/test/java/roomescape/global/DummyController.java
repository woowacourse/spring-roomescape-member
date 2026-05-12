package roomescape.global;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import roomescape.global.exception.CustomException;
import roomescape.global.exception.DuplicateEntityException;
import roomescape.global.exception.EntityNotFoundException;
import roomescape.global.exception.ForbiddenException;

@RestController
@Validated
public class DummyController {

    @PostMapping("/dummy")
    public ResponseEntity<Long> testMethod(@Valid @RequestBody DummyDto.DummyData body) {
        return ResponseEntity.ok(body.testField());
    }

    @PostMapping("/dummy/{data}")
    public ResponseEntity<Long> testMethod(@Positive(message = "양수가 아님") @PathVariable Long data) {
        return ResponseEntity.ok(data);
    }

    @GetMapping("/dummy/param")
    public void requestParamCheck(@RequestParam String test) {
    }

    @GetMapping("/dummy/business")
    public void business() {
        throw new BusinessException();
    }

    @GetMapping("/dummy/badRequest")
    public void badRequest() {
        throw new IllegalArgumentException("잘못된 요청 예외");
    }

    @GetMapping("/dummy/illegalState")
    public void illegalState() {
        throw new IllegalStateException("잘못된 상태 예외");
    }

    @GetMapping("/dummy/forbidden")
    public void forbidden() {
        throw new ForbiddenException("접근 권한이 없습니다.");
    }

    @GetMapping("/dummy/entityNotFound")
    public void entityNotFound() {
        throw new EntityNotFoundException("데이터 없음");
    }

    @GetMapping("/dummy/duplicateEntity")
    public void duplicateEntity() {
        throw new DuplicateEntityException("충돌");
    }

    @GetMapping("/dummy/dataIntegrity")
    public void dataIntegrity() {
        throw new DataIntegrityViolationException("무결성");
    }

    @GetMapping("/dummy/internal")
    public void internal() {
        throw new RuntimeException();
    }

    @GetMapping("/api/admin")
    public void accessCheck() {
    }

    private static class BusinessException extends CustomException {

        public BusinessException() {
            super(HttpStatus.BAD_REQUEST, "비즈니스 예외");
        }
    }
}
