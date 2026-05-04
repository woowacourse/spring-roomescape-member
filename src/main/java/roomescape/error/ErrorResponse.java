package roomescape.error;

public record ErrorResponse(ErrorCode code, String message) {
  public static ErrorResponse of(ErrorCode code, String message) {
    return new ErrorResponse(code, message);
  }
}

