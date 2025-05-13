package roomescape.admin.service;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import roomescape.admin.domain.Admin;
import roomescape.admin.dto.MemberResponse;
import roomescape.admin.repository.AdminRepository;
import roomescape.globalexception.BadRequestException;
import roomescape.member.repository.MemberRepository;

@Service
public class AdminService {

    private final AdminRepository repository;
    private final MemberRepository memberRepository;

    public AdminService(AdminRepository repository, MemberRepository memberRepository) {
        this.repository = repository;
        this.memberRepository = memberRepository;
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

    public List<MemberResponse> getMembers() {
        return memberRepository.findAll()
            .stream()
            .map(MemberResponse::fromMember)
            .toList();
    }
}
