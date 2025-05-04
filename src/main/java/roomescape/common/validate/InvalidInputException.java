package roomescape.common.validate;

import roomescape.common.exception.BusinessException;
import roomescape.common.validate.Validator.ValidationType;

public class InvalidInputException extends BusinessException {

    public InvalidInputException(final ValidationType type,
                                 final String className,
                                 final String fieldName,
                                 final String fieldDescription) {
        super(
                buildLoggingMessage(type, className, fieldName),
                buildUserMessage(fieldDescription)
        );
    }

    private static String buildLoggingMessage(final ValidationType type,
                                              final String className,
                                              final String fieldName) {
        return "Validation failed [" + type.getDescription() + "]: " + className + "." + fieldName;
    }

    private static String buildUserMessage(final String fieldDescription) {
        return fieldDescription + "을(를) 올바르게 입력해주세요.";
    }
}
