package roomescape.member.service;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import roomescape.exception.BadRequestException;
import roomescape.exception.ConflictException;
import roomescape.exception.ExceptionCause;
import roomescape.exception.UnauthorizedException;
import roomescape.member.dao.MemberDao;
import roomescape.member.domain.Member;
import roomescape.member.domain.Role;
import roomescape.member.domain.Visitor;
import roomescape.member.dto.MemberLoginRequest;
import roomescape.member.dto.MemberNameResponse;
import roomescape.member.dto.MemberResponse;
import roomescape.member.dto.MemberSignupRequest;

@Service
public class MemberService {

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

    public void signup(MemberSignupRequest memberSignupRequest) {
        if (memberDao.findByEmail(memberSignupRequest.email()).isPresent()) {
            throw new ConflictException(ExceptionCause.MEMBER_EXIST);
        }
        Member member = Member.createWithoutId(memberSignupRequest.name(), memberSignupRequest.email(),
                memberSignupRequest.password(), Role.USER);
        memberDao.create(member);
    }

    public Member findByToken(String token) {
        Long memberId = jwtUtil.getMemberIdFromToken(token);
        return findById(memberId);
    }

    public Member findById(Long id) {
        Optional<Member> memberOptional = memberDao.findById(id);
        if (memberOptional.isEmpty()) {
            throw new BadRequestException(ExceptionCause.MEMBER_NOTFOUND);
        }
        return memberOptional.get();
    }

    public List<MemberResponse> findAllUsers() {
        return findAllMembers().stream()
                .filter(member -> member.getRole().equals(Role.USER))
                .map(MemberResponse::from)
                .toList();
    }

    public MemberNameResponse checkUserLogin(Visitor visitor) {
        if (!visitor.isAuthorized()) {
            throw new UnauthorizedException(ExceptionCause.MEMBER_UNAUTHORIZED);
        }
        return new MemberNameResponse(visitor.name());
    }

    private List<Member> findAllMembers() {
        return memberDao.findAll();
    }

    private Member findByEmailAndPassword(String email, String password) {
        Optional<Member> memberOptional = memberDao.findByEmailAndPassword(email, password);
        if (memberOptional.isEmpty()) {
            throw new BadRequestException(ExceptionCause.MEMBER_NOTFOUND);
        }
        return memberOptional.get();
    }
}
