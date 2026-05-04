package roomescape.domain;

import java.util.Objects;

public record Theme(Long id, String name, String description, String thumbnailUrl) {


    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Theme theme)) return false;
        return Objects.equals(id, theme.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
