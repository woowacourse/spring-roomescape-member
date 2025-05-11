package roomescape.member.domain;

import roomescape.common.domain.Id;

public class Member {
    private final Id id;
    private final String name;
    private final String email;
    private final String password;

    public Member(final Id id, final String name, final String email, final String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public static Member of(final Long id, final String name, final String email, final String password) {
        return new Member(Id.from(id), name, email, password);
    }

    public static Member withUnassignedId(final String name, final String email, final String password) {
        return new Member(Id.unassigned(), name, email, password);
    }

    public Long getId() {
        return id.getValue();
    }

    public void setId(final Long value) {
        id.setValue(value);
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
