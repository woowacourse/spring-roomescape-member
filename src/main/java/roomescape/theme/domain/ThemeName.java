package roomescape.theme.domain;

public class ThemeName {

  private static final int MAXIMUM_ENABLE_NAME_LENGTH = 20;

  private final String value;

  public ThemeName(final String value) {
    validateValue(value);
    this.value = value;
  }

  private void validateValue(final String value) {
    if (value == null || value.isEmpty() || value.length() > MAXIMUM_ENABLE_NAME_LENGTH) {
      throw new IllegalArgumentException("테마 이름은 1글자 이상 20글자 이하여야 합니다.");
    }
  }

  public String getValue() {
    return value;
  }
}
