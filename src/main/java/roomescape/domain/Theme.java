package roomescape.domain;

import java.util.Objects;
import roomescape.domain.vo.ThemeName;

public class Theme {

    private final Long id;
    private final ThemeName name;
    private final String description;
    private final String imageUrl;

    // TODO: 도메인 전체적으로 인자값 검증
    public Theme(Long id, ThemeName name, String description, String imageUrl) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
    }

    // TODO: 필드 일급컬랙션? (imageUrl 정도는 분리
    public Theme(String name, String description, String imageUrl) {
        this(null, new ThemeName(name), description, imageUrl);
    }

    public Long getId() {
        return id;
    }

    public String getNameValue() {
        return name.value();
    }

    public String getDescription() {
        return description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public Theme withId(Long id) {
        return new Theme(id, this.name, this.description, this.imageUrl);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Theme theme = (Theme) o;
        return id != null
            && Objects.equals(id, theme.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
