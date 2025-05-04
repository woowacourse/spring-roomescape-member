package roomescape.theme.domain;

import roomescape.common.domain.DomainId;

import java.util.UUID;

public class ThemeId extends DomainId {

    public static final String domainName = "테마 식별자";

    private ThemeId(final Long value, final boolean assigned) {
        super(value, assigned);
    }

    public static ThemeId unassigned() {
        return new ThemeId(UUID.randomUUID().getMostSignificantBits(), false);
    }

    public static ThemeId from(final Long id) {
        return new ThemeId(id, true);
    }
}
