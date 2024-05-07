package roomescape.global.dto.response;

import roomescape.global.exception.error.ErrorType;

public record ApiResponse<T>(
        String message,
        T data
) {
    private static final String SUCCESS_MESSAGE = "요청이 성공적으로 수행되었습니다.";

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(SUCCESS_MESSAGE, data);
    }

    public static <T> ApiResponse<T> success() {
        return new ApiResponse<>(SUCCESS_MESSAGE, null);
    }

    public static <T> ApiResponse<T> fail(final ErrorType errorType) {
        return new ApiResponse<>(errorType.getDescription(), null);
    }
}
