package roomescape.domain;

import java.util.Objects;

public class Theme {

    public static final String DEFAULT_THUMBNAIL = "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg";

    private final Long id;
    private final String name;
    private final String description;
    private final String thumbnail;

    public Theme(final Long id, final String name, final String description, final String thumbnail) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.thumbnail = thumbnail;
    }

    public Theme(final Long id) {
        this(id, null, null, null);
    }

    public Theme(final String name, final String description, final String thumbnail) { //todo 이생성자 지워보기
        this.id = null;
        this.name = name;
        this.description = description;
        this.thumbnail = getDefaultThumbnailIfNotExists(thumbnail);
    }

    private String getDefaultThumbnailIfNotExists(final String thumbnail) {
        if (thumbnail.isBlank()) {
            return DEFAULT_THUMBNAIL; //TODO 그냥 디비에 default로 박을까
        }
        return thumbnail;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    @Override
    public boolean equals(final Object target) {
        if (this == target) {
            return true;
        }
        if (target == null || getClass() != target.getClass()) {
            return false;
        }
        final Theme theme = (Theme) target;
        return Objects.equals(getId(), theme.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return "Theme{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", thumbnail='" + thumbnail + '\'' +
                '}';
    }
}
