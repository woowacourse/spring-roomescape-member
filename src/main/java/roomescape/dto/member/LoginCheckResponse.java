package roomescape.dto.member;

import java.util.Objects;
import roomescape.domain.member.Member;

public class LoginCheckResponse {

    private final String name;

    private LoginCheckResponse(String name) {
        this.name = name;
    }

    public LoginCheckResponse(Member member) {
        this(member.getName().getValue());
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        LoginCheckResponse other = (LoginCheckResponse) o;
        return Objects.equals(this.name, other.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }

    @Override
    public String toString() {
        return "LoginCheckResponse{" +
                "name='" + name + '\'' +
                '}';
    }
}
