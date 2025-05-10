package roomescape.member.controller.dto;

import lombok.AccessLevel;
import lombok.experimental.FieldNameConstants;
import roomescape.common.utils.Validator;

@FieldNameConstants(level = AccessLevel.PRIVATE)
public record MemberInfo(
        Long id,
        String name,
        String email
) {

    public MemberInfo {
        validate(id, name, email);
    }

    private void validate(final Long id,
                          final String name,
                          final String email) {
        Validator.of(MemberInfo.class)
                .notNullField(Fields.id, id)
                .notNullField(Fields.name, name)
                .notNullField(Fields.email, email);
    }
}
