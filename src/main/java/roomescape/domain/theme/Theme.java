package roomescape.domain.theme;

import java.util.Objects;
import roomescape.exception.ErrorCode;
import roomescape.exception.InvalidInputException;

public class Theme {
    private static final String INVALID_THEME_ID = "INVALID_THEME_ID";

    private final Long id;
    private final String name;
    private final String description;
    private final String thumbnailUrl;

    private Theme(final Long id, final String name, final String description, final String thumbnailUrl) {
        validateName(name);
        this.id = id;
        this.name = name;
        this.description = description;
        this.thumbnailUrl = thumbnailUrl;
    }

    private void validateName(final String name) {
        if (name == null || name.isBlank()) {
            throw new InvalidInputException(ErrorCode.INVALID_THEME_NAME, "테마 이름은 비어있을 수 없습니다.");
        }
    }

    public static Theme createNew(final String name, final String description, final String thumbnailUrl) {
        return new Theme(null, name, description, thumbnailUrl);
    }

    public static Theme of(final Long id, final String name, final String description, final String thumbnailUrl) {
        validateId(id);
        return new Theme(id, name, description, thumbnailUrl);
    }

    public Theme withId(final Long id) {
        validateId(id);
        return new Theme(id, name, description, thumbnailUrl);
    }

    private static void validateId(final Long id){
        if(id == null) {
            throw new InvalidInputException(INVALID_THEME_ID, "Id는 비어있을 수 없습니다.");
        }
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

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    @Override
    public boolean equals(Object o){
        if(!(o instanceof Theme)) {
            return false;
        }
        Theme t = (Theme) o;
        return Objects.equals(id, t.getId());
    }

    @Override
    public int hashCode(){
        return Objects.hash(id);
    }

}
