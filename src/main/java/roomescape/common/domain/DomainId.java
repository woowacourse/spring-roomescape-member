package roomescape.common.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.FieldNameConstants;
import roomescape.common.validate.Validator;

@FieldNameConstants
@EqualsAndHashCode
public abstract class DomainId {

    public static final String domainName = "식별자";

    private final Long value;
    @Getter
    private final boolean assigned;

    protected DomainId(final Long value, final boolean assigned) {
        validate(value);

        this.value = value;
        this.assigned = assigned;
    }

    private void validate(final Long value) {
        Validator.of(DomainId.class)
                .validateNotNull(Fields.value, value, domainName);
    }

    public Long getValue() {
        requireAssigned();
        return value;
    }

    public void requireAssigned() {
        if (assigned) {
            return;
        }
        throw new IllegalStateException("식별자가 할당되지 않았습니다.");
    }

    @Override
    public String toString() {
        if (assigned) {
            return getClass().getSimpleName() + "(" + value + ")";
        }
        return getClass().getSimpleName() + "(unassigned)";
    }
}
