package roomescape.domain;

public record Theme(
        Long id,
        String name,
        String description,
        String thumbnailUrl
) {
    public static Theme create(String name, String description, String thumbnailUrl) {
        return new Theme(null, name, description, thumbnailUrl);
    }

    public static Theme createRow(Long id, String name, String description, String thumbnailUrl) {
        return new Theme(id, name, description, thumbnailUrl);
    }

    public Theme appendId(Long id) {
        return new Theme(id, name, description, thumbnailUrl);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Theme that)) {
            return false;
        }
        if (this.id == null || that.id == null) {
            return false;
        }
        return this.id.equals(that.id);
    }

    @Override
    public int hashCode() {
        if (id != null) {
            return id.hashCode();
        } else {
            return System.identityHashCode(this);
        }
    }
}
