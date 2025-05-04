package roomescape.common.domain;

import lombok.EqualsAndHashCode;
import lombok.experimental.FieldNameConstants;
import roomescape.common.validate.Validator;

@FieldNameConstants
@EqualsAndHashCode
public abstract class DomainId {

    public static final String domainName = "식별자";

    private final Long value;
    private final boolean assigned;

    protected DomainId(final Long value, final boolean assigned) {
        validate(value);

        this.value = value;
        this.assigned = assigned;
    }

    private void validate(final Long value) {
        Validator.of(DomainId.class)
                .notNullField(Fields.value, value, domainName);
    }

    public Long getValue() {
        if (assigned) {
            return value;
        }
        throw new IllegalStateException("식별자가 할당되지 않아 사용할 수 없습니다.");
    }

    @Override
    public String toString() {
        if (assigned) {
            return getClass().getSimpleName() + "(" + value + ")";
        }
        return getClass().getSimpleName() + "(unassigned)";
    }
}
