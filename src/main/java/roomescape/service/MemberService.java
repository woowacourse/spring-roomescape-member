package roomescape.service;

import java.util.Optional;
import org.springframework.stereotype.Service;
import roomescape.dao.member.MemberDao;
import roomescape.domain.Member;
import roomescape.domain.Role;
import roomescape.dto.request.UserLoginRequest;
import roomescape.dto.request.UserSignupRequest;
import roomescape.dto.response.UserLoginCheckResponse;
import roomescape.exception.ExceptionCause;
import roomescape.exception.MemberExistException;
import roomescape.exception.MemberNotFoundException;

@Service
public class MemberService {

    private final JwtUtil jwtUtil;
    private final MemberDao memberDao;
    private static final Role USER_ROLE = new Role(1L, "user");

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
        if(memberOptional.isEmpty()) {
            throw new MemberNotFoundException(ExceptionCause.MEMBER_NOTFOUND);
        }
        return memberOptional.get();
    }

    public void signup(UserSignupRequest userSignupRequest) {
        if(memberDao.findByEmail(userSignupRequest.email()).isPresent()) {
            throw new MemberExistException(ExceptionCause.MEMBER_EXIST);
        }
        Member member = Member.createWithoutId(userSignupRequest.name(), userSignupRequest.email(),
                userSignupRequest.password(), USER_ROLE);
        memberDao.create(member);
    }
}
