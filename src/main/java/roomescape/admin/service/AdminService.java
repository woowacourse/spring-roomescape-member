package roomescape.admin.service;

import java.util.Optional;
import org.springframework.stereotype.Service;
import roomescape.admin.domain.Admin;
import roomescape.admin.repository.AdminRepository;
import roomescape.globalexception.BadRequestException;

@Service
public class AdminService {

    private final AdminRepository repository;

    public AdminService(AdminRepository repository) {
        this.repository = repository;
    }

    public Admin findExistingAdminByLoginIdAndPassword(String loginId, String password) {
        return repository.findByLoginIdAndPassword(loginId, password)
            .orElseThrow(() -> new BadRequestException("아이디 혹은 비밀번호가 잘못되었습니다."));
    }

    public Optional<Admin> findByLoginId(String loginId) {
        return repository.findByLoginId(loginId);
    }

    public boolean existsByLoginIdAndPassword(String loginId, String password) {
        return repository.existsByLoginIdAndPassword(loginId, password);
    }
}
