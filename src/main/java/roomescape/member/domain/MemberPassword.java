package roomescape.member.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.FieldNameConstants;
import roomescape.common.utils.Validator;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@FieldNameConstants(level = AccessLevel.PRIVATE)
@EqualsAndHashCode
public class MemberPassword {

    // TODO: Add password encryption logic
    private final String value;

    public static MemberPassword from(final String password) {
        validate(password);
        return new MemberPassword(password);
    }

    private static void validate(final String value) {
        Validator.of(MemberPassword.class)
                .notNullField(MemberPassword.Fields.value, value)
                .notBlankField(MemberPassword.Fields.value, value.strip());
    }
}
