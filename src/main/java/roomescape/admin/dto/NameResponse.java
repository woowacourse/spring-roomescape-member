package roomescape.admin.dto;

import roomescape.admin.domain.Admin;

public record NameResponse(
    String name
) {

    public static NameResponse fromAdmin(Admin admin) {
        return new NameResponse(admin.getName());
    }
}
