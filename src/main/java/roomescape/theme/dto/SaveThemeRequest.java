package roomescape.theme.dto;

import roomescape.theme.domain.Theme;

public record SaveThemeRequest(String name, String description, String thumbnail) {

  public Theme toTheme() {
    return Theme.of(name, description, thumbnail);
  }
}
