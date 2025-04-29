package roomescape.exception.custom;

public class PharmaceuticalViolationException extends RuntimeException {

    public PharmaceuticalViolationException(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return "규칙에 위반되는 요청입니다";
    }
}
