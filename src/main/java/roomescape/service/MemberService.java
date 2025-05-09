package roomescape.service;

import java.util.Optional;
import org.springframework.stereotype.Service;
import roomescape.dao.member.MemberDao;
import roomescape.domain.Member;
import roomescape.dto.request.UserLoginRequest;
import roomescape.dto.response.UserLoginCheckResponse;
import roomescape.exception.ExceptionCause;
import roomescape.exception.MemberNotFoundException;

@Service
public class MemberService {

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
        if(memberOptional.isEmpty()) {
            throw new MemberNotFoundException(ExceptionCause.MEMBER_NOTFOUND);
        }
        return memberOptional.get();
    }
}
