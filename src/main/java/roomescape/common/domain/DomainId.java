package roomescape.common.domain;

import lombok.EqualsAndHashCode;
import lombok.experimental.FieldNameConstants;
import roomescape.common.utils.Validator;

@FieldNameConstants
@EqualsAndHashCode
public abstract class DomainId {

    private final Long value;
    private final boolean assigned;

    protected DomainId(final Long value, final boolean assigned) {
        validate(value);

        this.value = value;
        this.assigned = assigned;
    }

    private static void validate(final Long value) {
        Validator.of(DomainId.class)
                .notNullField(Fields.value, value);
    }

    public Long getValue() {
        if (assigned) {
            return value;
        }
        throw new IllegalStateException("저장되지 않아서 식별할 수 없습니다.");
    }
}
