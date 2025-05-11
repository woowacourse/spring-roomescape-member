package roomescape.member.service;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import roomescape.exception.BadRequestException;
import roomescape.exception.ConflictException;
import roomescape.exception.ExceptionCause;
import roomescape.member.dao.MemberDao;
import roomescape.member.domain.Member;
import roomescape.member.domain.Role;
import roomescape.member.dto.MemberNameResponse;
import roomescape.member.dto.MemberLoginRequest;
import roomescape.member.dto.MemberResponse;
import roomescape.member.dto.MemberSignupRequest;

@Service
public class MemberService {

    private static final Role USER_ROLE = new Role(1L, "user");
    private final JwtUtil jwtUtil;
    private final MemberDao memberDao;

    public MemberService(JwtUtil jwtUtil, MemberDao memberDao) {
        this.jwtUtil = jwtUtil;
        this.memberDao = memberDao;
    }

    public String login(MemberLoginRequest memberLoginRequest) {
        Member member = findByEmailAndPassword(memberLoginRequest.email(), memberLoginRequest.password());
        return jwtUtil.generateToken(member);
    }

    public MemberNameResponse getUserNameFromToken(String token) {
        String name = jwtUtil.getMemberNameFromToken(token);
        return MemberNameResponse.from(name);
    }

    private Member findByEmailAndPassword(String email, String password) {
        Optional<Member> memberOptional = memberDao.findByEmailAndPassword(email, password);
        if (memberOptional.isEmpty()) {
            throw new BadRequestException(ExceptionCause.MEMBER_NOTFOUND);
        }
        return memberOptional.get();
    }

    public void signup(MemberSignupRequest memberSignupRequest) {
        if (memberDao.findByEmail(memberSignupRequest.email()).isPresent()) {
            throw new ConflictException(ExceptionCause.MEMBER_EXIST);
        }
        Member member = Member.createWithoutId(memberSignupRequest.name(), memberSignupRequest.email(),
                memberSignupRequest.password(), USER_ROLE);
        memberDao.create(member);
    }

    public Member findById(Long id) {
        Optional<Member> memberOptional = memberDao.findById(id);
        if(memberOptional.isEmpty()) {
            throw new BadRequestException(ExceptionCause.MEMBER_NOTFOUND);
        }
        return memberOptional.get();
    }

    public List<MemberResponse> findAllMembers() {
        return memberDao.findAll().stream()
                .map(MemberResponse::from)
                .toList();
    }
}
