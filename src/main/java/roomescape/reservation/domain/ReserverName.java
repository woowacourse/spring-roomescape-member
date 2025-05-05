package roomescape.reservation.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.FieldNameConstants;
import roomescape.common.domain.DomainTerm;
import roomescape.common.validate.Validator;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@FieldNameConstants(level = AccessLevel.PRIVATE)
@EqualsAndHashCode
@ToString
public class ReserverName {

    private final String value;

    public static ReserverName from(final String name) {
        validate(name);
        return new ReserverName(name);
    }

    private static void validate(final String value) {
        Validator.of(ReserverName.class)
                .validateNotBlank(Fields.value, value, DomainTerm.RESERVER_NAME.label());
    }
}
