package roomescape.member.service;

import java.util.List;

import org.springframework.stereotype.Service;

import roomescape.auth.config.PasswordEncryptor;
import roomescape.member.dao.MemberDao;
import roomescape.member.domain.Member;
import roomescape.member.dto.MemberRequestDto;

@Service
public class MemberService {
    private final MemberDao memberDao;
    private final PasswordEncryptor passwordEncryptor;

    public MemberService(final MemberDao memberDao, final PasswordEncryptor passwordEncryptor) {
        this.memberDao = memberDao;
        this.passwordEncryptor = passwordEncryptor;
    }

    public Member findById(final Long id) {
        return memberDao.getById(id);
    }

    public List<Member> findAll() {
        return memberDao.findAll();
    }

    public Member save(final MemberRequestDto memberRequestDto) {
        return memberDao.save(memberRequestDto.toMemberOf(
                passwordEncryptor.encrypt(memberRequestDto.password()))
        );
    }
}
