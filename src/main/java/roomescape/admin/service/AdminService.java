package roomescape.admin.service;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import roomescape.admin.domain.Admin;
import roomescape.admin.dto.MemberResponse;
import roomescape.admin.repository.AdminRepository;
import roomescape.member.repository.MemberRepository;

@Service
public class AdminService {

    private final AdminRepository repository;
    private final MemberRepository memberRepository;

    public AdminService(AdminRepository repository, MemberRepository memberRepository) {
        this.repository = repository;
        this.memberRepository = memberRepository;
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
