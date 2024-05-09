package roomescape.domain;

import java.util.ArrayList;
import java.util.List;

public class Member {

    private final Long id;
    private final String name;
    private final String email;
    private final String password;
    private final Role role;
    private final List<Reservation> reservations = new ArrayList<>();

    public Member(String name, String email, String password) {
        this(null, name, email, password, Role.USER);
    }

    public Member(Long id, String name, String email, String password, Role role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public Long getId() {
        return id;
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

    public Role getRole() {
        return role;
    }
}
