package roomescape.model;

public class LoginMember {

    private final long id;
    private final Name name;
    private final Email email;

    public LoginMember(long id, String name, String email) {
        validateRange(id);
        this.id = id;
        this.name = new Name(name);
        this.email = new Email(email);
    }

    private void validateRange(long id) {
        if (id <= 0) {
            throw new IllegalStateException("id는 0 이하일 수 없습니다.");
        }
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name.getValue();
    }

    public String getEmail() {
        return email.getValue();
    }
}
