package roomescape.controller.request;

import roomescape.service.param.CreateThemeParam;

public record CreateThemeRequest(String name, String description, String thumbnail) {

    private static final String ERROR_MESSAGE_FORMAT = "테마 필수 정보가 누락되었습니다. %s: %s";

    public CreateThemeRequest {
        validateString(name, "name");
        validateString(description, "description");
        validateString(thumbnail, "thumbnail");
    }

    private void validateString(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(String.format(ERROR_MESSAGE_FORMAT, fieldName, value));
        }
    }

    public CreateThemeParam toServiceParam() {
        return new CreateThemeParam(name, description, thumbnail);
    }
}
