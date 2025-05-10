package roomescape.member.service;

import java.util.Optional;
import org.springframework.stereotype.Service;
import roomescape.exception.BadRequestException;
import roomescape.exception.ConflictException;
import roomescape.exception.ExceptionCause;
import roomescape.member.dao.MemberDao;
import roomescape.member.domain.Member;
import roomescape.member.domain.Role;
import roomescape.member.dto.UserLoginCheckResponse;
import roomescape.member.dto.UserLoginRequest;
import roomescape.member.dto.UserSignupRequest;

@Service
public class MemberService {

    private static final Role USER_ROLE = new Role(1L, "user");
    private final JwtUtil jwtUtil;
    private final MemberDao memberDao;

    public MemberService(JwtUtil jwtUtil, MemberDao memberDao) {
        this.jwtUtil = jwtUtil;
        this.memberDao = memberDao;
    }

    public String login(UserLoginRequest userLoginRequest) {
        Member member = findByEmailAndPassword(userLoginRequest.email(), userLoginRequest.password());
        return jwtUtil.generateToken(member);
    }

    public UserLoginCheckResponse getUserNameFromToken(String token) {
        String name = jwtUtil.getUserNameFromToken(token);
        return UserLoginCheckResponse.from(name);
    }

    private Member findByEmailAndPassword(String email, String password) {
        Optional<Member> memberOptional = memberDao.findByEmailAndPassword(email, password);
        if (memberOptional.isEmpty()) {
            throw new BadRequestException(ExceptionCause.MEMBER_NOTFOUND);
        }
        return memberOptional.get();
    }

    public void signup(UserSignupRequest userSignupRequest) {
        if (memberDao.findByEmail(userSignupRequest.email()).isPresent()) {
            throw new ConflictException(ExceptionCause.MEMBER_EXIST);
        }
        Member member = Member.createWithoutId(userSignupRequest.name(), userSignupRequest.email(),
                userSignupRequest.password(), USER_ROLE);
        memberDao.create(member);
    }
}
