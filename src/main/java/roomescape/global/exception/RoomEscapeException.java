package roomescape.global.exception;

public class RoomEscapeException {
    public static class ResourceNotFoundException extends RuntimeException {
        public ResourceNotFoundException(String message) {
            super(message);
        }

        public ResourceNotFoundException() {
            super("해당 리소스를 찾을 수 없습니다.");
        }
    }

    public static class BadRequestException extends RuntimeException {
        public BadRequestException(String message) {
            super(message);
        }

        public BadRequestException() {
            super("잘못된 요청입니다.");
        }
    }

    public static class AuthenticationException extends RuntimeException {
        public AuthenticationException(String message) {
            super(message);
        }

        public AuthenticationException() {
            super("인증에 실패하였습니다.");
        }
    }
}
