package roomescape.controller.response;

public class MemberResponse {

    private final long id;
    private final String name;
    private final String email;

    public MemberResponse(long id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }
}
