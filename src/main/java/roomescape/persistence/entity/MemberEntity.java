package roomescape.persistence.entity;

import java.util.Objects;
import roomescape.business.domain.member.Member;
import roomescape.business.domain.member.MemberRole;
import roomescape.business.domain.member.SignUpMember;

public final class MemberEntity {

    private final Long id;
    private final String name;
    private final String email;
    private final String password;
    private final String role;

    public MemberEntity(Long id, String name, String email, String password, String role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public MemberEntity(String name, String email, String password, String role) {
        this(null, name, email, password, role);
    }

    public MemberEntity(Long id, String name, String email, String role) {
        this(id, name, email, null, role);
    }

    public static MemberEntity fromDomain(SignUpMember signUpMember) {
        if (signUpMember.getId() != null) {
            return new MemberEntity(
                    signUpMember.getId(),
                    signUpMember.getName(),
                    signUpMember.getEmail(),
                    signUpMember.getPassword(),
                    signUpMember.getRole().value()
            );
        }
        return new MemberEntity(
                signUpMember.getName(),
                signUpMember.getEmail(),
                signUpMember.getPassword(),
                signUpMember.getRole().value()
        );
    }

    public static MemberEntity fromDomain(Member member) {
        if (member.getId() != null) {
            return new MemberEntity(
                    member.getId(),
                    member.getName(),
                    member.getEmail(),
                    null,
                    member.getRole().value()
            );
        }
        return new MemberEntity(
                member.getName(),
                member.getEmail(),
                null,
                member.getRole().value()
        );
    }

    public MemberEntity copyWithId(Long id) {
        return new MemberEntity(id, name, email, password, role);
    }

    public Member toDomain() {
        if (id == null) {
            throw new IllegalArgumentException("사용자 엔티티의 ID가 null이어서 도메인 객체로 변환할 수 없습니다.");
        }
        return new Member(id, name, email, MemberRole.from(role));
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

    public String getRole() {
        return role;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (id == null || ((MemberEntity) o).id == null) {
            return false;
        }
        MemberEntity that = (MemberEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
