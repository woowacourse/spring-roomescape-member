package roomescape.repository;

import java.util.Optional;
import roomescape.domain.Member;

public interface MemberRepository {
    Optional<Member> findByEmailAndEncryptedPassword(String email, String encryptedPassword);
}
