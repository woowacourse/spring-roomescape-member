package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.auth.JwtProvider;
import roomescape.dto.request.LoginRequest;
import roomescape.dto.request.LoginCheckRequest;
import roomescape.dto.response.LoginCheckResponse;
import roomescape.dto.response.LoginResponse;
import roomescape.entity.Member;
import roomescape.exception.AuthenticationException;
import roomescape.repository.MemberDao;

import java.util.List;


@Service
public class MemberService {

    private final MemberDao memberDao;
    private final JwtProvider jwtProvider;

    public MemberService(MemberDao memberDao, JwtProvider jwtProvider) {
        this.memberDao = memberDao;
        this.jwtProvider = jwtProvider;
    }

    public LoginResponse login(LoginRequest request) {
        Member member = memberDao.findByEmailAndPassword(request.email(), request.password())
            .orElseThrow(() -> new AuthenticationException("로그인 정보를 찾을 수 없습니다."));

        String accessToken = jwtProvider.generateToken(member);
        return LoginResponse.from(accessToken);
    }

    public LoginCheckRequest findById(Long memberId) {
        Member findMember = memberDao.findById(memberId)
            .orElseThrow(() -> new AuthenticationException("로그인 정보가 일치하지 않습니다."));
        return LoginCheckRequest.of(findMember.getId(), findMember.getName(), findMember.getEmail(), findMember.getRole());
    }

    public List<LoginCheckResponse> findAll() {
        List<Member> allMembers = memberDao.findAll();
        return allMembers.stream()
            .map(member -> LoginCheckResponse.from(member.getId(), member.getName()))
            .toList();
    }
}
