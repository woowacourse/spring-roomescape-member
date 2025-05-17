package roomescape.admin.repository;

import java.util.Optional;
import roomescape.admin.domain.Admin;

public interface AdminRepository {

    Optional<Admin> findByLoginId(String loginId);

    boolean existsByLoginIdAndPassword(String loginId, String password);
}
