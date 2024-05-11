package roomescape.member.domain;

public class MemberInfo {

    private final Long id;
    private final String name;

    public MemberInfo(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
