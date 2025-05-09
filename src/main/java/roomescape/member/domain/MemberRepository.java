package roomescape.member.domain;

public interface MemberRepository {

    Member findById(Long id);

    Member findByEmail(String email);

    boolean isExistsByEmail(String email);
}
