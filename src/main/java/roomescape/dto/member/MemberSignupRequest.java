package roomescape.dto.member;

import java.util.Objects;
import roomescape.domain.member.Member;
import roomescape.domain.member.MemberEmail;
import roomescape.domain.member.MemberName;
import roomescape.domain.member.MemberPassword;
import roomescape.domain.member.MemberRole;

public class MemberSignupRequest {

    private final String name;
    private final String email;
    private final String password;

    public MemberSignupRequest(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public Member toDomain() {
        return new Member(
                null,
                new MemberName(name),
                new MemberEmail(email),
                new MemberPassword(password),
                MemberRole.USER
        );
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MemberSignupRequest other = (MemberSignupRequest) o;
        return Objects.equals(this.name, other.name)
               && Objects.equals(this.email, other.email)
               && Objects.equals(this.password, other.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, email, password);
    }

    @Override
    public String toString() {
        return "MemberSignupRequest{" +
               "email='" + email + '\'' +
               ", name='" + name + '\'' +
               ", password='" + password + '\'' +
               '}';
    }
}
