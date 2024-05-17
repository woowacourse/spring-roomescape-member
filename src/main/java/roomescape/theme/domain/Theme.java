package roomescape.theme.domain;

import java.util.Objects;

public class Theme {

  private final Long id;
  private final ThemeName name;
  private final ThemeDescription description;
  private final String thumbnail;

  private Theme(final Long id, final ThemeName name, final ThemeDescription description,
      final String thumbnail) {
    this.id = id;
    this.name = name;
    this.description = description;
    this.thumbnail = thumbnail;
  }

  public static Theme of(final String name, final String description, final String thumbnail) {
    return new Theme(null, new ThemeName(name), new ThemeDescription(description), thumbnail);
  }

  public static Theme of(final Long id, final String name, final String description,
      final String thumbnail) {
    return new Theme(id, new ThemeName(name), new ThemeDescription(description), thumbnail);
  }

  public Theme copyWithId(final Long themeId) {
    return new Theme(themeId, name, description, thumbnail);
  }

  public Long getId() {
    return id;
  }

  public ThemeName getName() {
    return name;
  }

  public ThemeDescription getDescription() {
    return description;
  }

  public String getThumbnail() {
    return thumbnail;
  }

  @Override
  public boolean equals(final Object o) {
      if (this == o) {
          return true;
      }
      if (!(o instanceof final Theme theme)) {
          return false;
      }
    return Objects.equals(id, theme.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
