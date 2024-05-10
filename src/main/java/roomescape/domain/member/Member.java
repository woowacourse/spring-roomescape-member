package roomescape.domain.member;

public class Member {

    private final Long id;
    private final String name;

    public Member(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Member(String name) {
        this(null, name);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
