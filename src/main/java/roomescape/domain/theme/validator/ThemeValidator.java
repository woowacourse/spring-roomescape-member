package roomescape.domain.theme.validator;

import java.util.ArrayList;
import java.util.List;
import roomescape.domain.global.exception.BadRequestException;
import roomescape.domain.global.exception.ErrorCode;
import roomescape.domain.global.exception.ErrorDetail;

public final class ThemeValidator {

    private ThemeValidator() {}

    public static void validate(String name, String description, String imageUrl) {
        List<ErrorDetail> errors = new ArrayList<>();
        validateName(name, errors);
        validateDescription(description, errors);
        validateImageUrl(imageUrl, errors);

        if (!errors.isEmpty()) {
            throw new BadRequestException(ErrorCode.THEME_INVALID_REQUEST, errors);
        }
    }

    private static void validateName(String name, List<ErrorDetail> errors) {
        if (name.isBlank()) {
            errors.add(ErrorDetail.of("name", name, "테마명은 비어있지 않은 문자열이어야 합니다."));
        }
    }

    private static void validateDescription(String description, List<ErrorDetail> errors) {
        if (description.isBlank()) {
            errors.add(ErrorDetail.of("description", description, "테마 설명은 비어있지 않은 문자열이어야 합니다."));
        }
    }

    private static void validateImageUrl(String imageUrl, List<ErrorDetail> errors) {
        if (imageUrl.isBlank()) {
            errors.add(ErrorDetail.of("imageUrl", imageUrl, "썸네일 url은 비어있지 않은 문자열이어야 합니다."));
        }
    }
}
