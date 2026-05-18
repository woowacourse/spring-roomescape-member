package roomescape.domain;

import roomescape.exception.ThemeNotFoundException;

public class Theme {
    private final Long id;
    private final String name;
    private final String description;
    private final String imgUrl;

    private Theme(Long id, String name, String description, String imgUrl) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.imgUrl = imgUrl;
    }

    public static Theme from(Long id, String name, String description, String imgUrl) {
        return new Theme(id, name, description, imgUrl);
    }

    public static Theme from(String name, String description, String imgUrl) {
        return new Theme(null, name, description, imgUrl);
    }

    public static void validateDeletion(int deleteCount) {
        if (deleteCount == 0) {
            throw new ThemeNotFoundException("해당 테마를 찾을 수 없습니다.");
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

    public String getImgUrl() {
        return imgUrl;
    }
}
