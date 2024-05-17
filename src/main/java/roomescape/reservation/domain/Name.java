package roomescape.reservation.domain;

import java.util.Objects;

public class Name {

  private static final int MAXIMUM_ENABLE_NAME_LENGTH = 5;

  private final String value;

  public Name(final String value) {
    validateClientName(value);
    this.value = value;
  }

  private void validateClientName(final String value) {
    if (value == null || value.isEmpty() || value.length() > MAXIMUM_ENABLE_NAME_LENGTH) {
      throw new IllegalArgumentException("예약자 이름은 1글자 이상 5글자 이하여야 합니다.");
    }
  }

  public String getValue() {
    return value;
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof final Name that)) {
      return false;
    }
    return Objects.equals(value, that.value);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(value);
  }
}
