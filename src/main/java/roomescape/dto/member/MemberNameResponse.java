package roomescape.dto.member;

import java.util.Objects;
import roomescape.domain.member.Member;

public class MemberNameResponse {

    private final long id;
    private final String name;

    private MemberNameResponse(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public MemberNameResponse(Member member) {
        this(member.getId(), member.getName().getValue());
    }

    public long getId() {
        return id;
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
        MemberNameResponse other = (MemberNameResponse) o;
        return this.id == other.id
                && Objects.equals(this.name, other.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return "MemberNameResponse{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
