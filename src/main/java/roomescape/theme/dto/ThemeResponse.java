package roomescape.theme.dto;

import roomescape.theme.domain.Theme;

public record ThemeResponse(
    Long id,
    String name,
    String description,
    String thumbnail
) {

  public static ThemeResponse from(final Theme theme) {
    return new ThemeResponse(
        theme.getId(),
        theme.getName().getValue(),
        theme.getDescription().getValue(),
        theme.getThumbnail()
    );
  }
}
