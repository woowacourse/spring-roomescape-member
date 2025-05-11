package roomescape.global.exception;

import org.springframework.http.HttpStatus;

public class NotFoundException extends CustomException {
    public NotFoundException(String targetName, Object value) {
        super(new DetailedErrorCode(targetName, value), "리소스를 찾을 수 없습니다");
    }

    private static class DetailedErrorCode implements ErrorCode {
        private final String message;

        public DetailedErrorCode(String targetName, Object value) {
            this.message = "%s를 찾을 수 없습니다. 입력값: %s".formatted(targetName, value);
        }

        @Override
        public HttpStatus getStatus() {
            return HttpStatus.NOT_FOUND;
        }

        @Override
        public String getMessage() {
            return message;
        }

        @Override
        public String name() {
            return HttpStatus.NOT_FOUND.name();
        }
    }
}
