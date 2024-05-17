package roomescape.auth.dto;

public record CheckLoginResponse(String name) {

  public static CheckLoginResponse from(final String name) {
    return new CheckLoginResponse(name);
  }
}
