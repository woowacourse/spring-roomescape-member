package roomescape.domain;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;

public class Themes {
    private final List<Theme> themes;

    public Themes(List<Theme> themes) {
        this.themes = themes;
    }

    public boolean hasNameOf(String name) {
        return themes.stream()
                .anyMatch(theme -> theme.isNameOf(name));
    }

    public <T> List<T> mapTo(Function<Theme, T> mapper) {
        return themes.stream()
                .map(mapper)
                .toList();
    }

    public List<Theme> getThemes() {
        return Collections.unmodifiableList(themes);
    }
}
