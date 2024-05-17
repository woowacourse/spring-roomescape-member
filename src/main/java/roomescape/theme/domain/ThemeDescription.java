package roomescape.theme.domain;

public class ThemeDescription {

  private static final int MAXIMUM_ENABLE_NAME_LENGTH = 80;

  private final String value;

  public ThemeDescription(final String value) {
    validateValue(value);
    this.value = value;
  }

  private void validateValue(final String value) {
    if (value == null || value.isEmpty() || value.length() > MAXIMUM_ENABLE_NAME_LENGTH) {
      throw new IllegalArgumentException("테마 설명은 1글자 이상 80글자 이하여야 합니다.");
    }
  }

  public String getValue() {
    return value;
  }
}
