package roomescape.service.dto;

public class LoginMember {

    private final Long id;
    private final String name;

    public LoginMember(Long id, String name) {
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
