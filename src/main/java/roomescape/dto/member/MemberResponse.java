package roomescape.dto.member;

import java.util.Objects;
import roomescape.domain.member.Member;

public class MemberResponse {

    private final long id;
    private final String name;
    private final String email;

    public MemberResponse(long id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public MemberResponse(Member member) {
        this(member.getId(), member.getName().getValue(), member.getEmail().getValue());
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MemberResponse other = (MemberResponse) o;
        return this.id == other.id
                && Objects.equals(this.name, other.name)
                && Objects.equals(this.email, other.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, email);
    }

    @Override
    public String toString() {
        return "MemberResponse{" +
                "email='" + email + '\'' +
                ", id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
