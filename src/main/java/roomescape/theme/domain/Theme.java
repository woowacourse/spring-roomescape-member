package roomescape.theme.domain;

import roomescape.global.exception.BusinessException;
import roomescape.global.exception.ErrorCode;

public class Theme {

    private Long id;
    private String name;
    private String description;
    private String thumbnail;

    public Theme(Long id, String name, String description, String thumbnail) {
        validateName(name);
        validateDescription(description);
        validateThumbnail(thumbnail);
        this.id = id;
        this.name = name;
        this.description = description;
        this.thumbnail = thumbnail;
    }

    private void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new BusinessException(ErrorCode.INVALID_INPUT);
        }
    }


    private void validateDescription(String description) {
        if (description == null || description.isBlank()) {
            throw new BusinessException(ErrorCode.INVALID_INPUT);
        }
    }

    private void validateThumbnail(String thumbnail) {
        if (thumbnail == null || thumbnail.isBlank()) {
            throw new BusinessException(ErrorCode.INVALID_INPUT);
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

    public String getThumbnail() {
        return thumbnail;
    }
}
