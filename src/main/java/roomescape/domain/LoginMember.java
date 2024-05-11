package roomescape.domain;

public class LoginMember {
    private final Long id;
    private final String name;

    public LoginMember(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "LoginMember{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
