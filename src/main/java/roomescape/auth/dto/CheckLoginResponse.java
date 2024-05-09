package roomescape.auth.dto;

public record CheckLoginResponse(String name) {

  public static CheckLoginResponse from(String name) {
    return new CheckLoginResponse(name);
  }
}
