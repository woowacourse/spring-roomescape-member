package roomescape.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

@Getter
@AllArgsConstructor
public class Theme {

    private Long id;

    @NonNull
    private String name;

    @NonNull
    private String description;

    @NonNull
    private String thumbnail;

    public Theme(@NonNull final String name, @NonNull final String description, @NonNull final String thumbnail) {
        this.id = null;
        this.name = name;
        this.description = description;
        this.thumbnail = thumbnail;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Theme other)) {
            return false;
        }
        if (this.id == null || other.id == null) {
            return false;
        }
        return this.id.equals(other.id);
    }

    @Override
    public int hashCode() {
        if (id == null) {
            return 0;
        }
        return id.hashCode();
    }

}
