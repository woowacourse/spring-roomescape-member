package roomescape.domain.member.service;

import static roomescape.global.exception.ErrorMessage.DUPLICATE_USER;
import static roomescape.global.exception.ErrorMessage.NOT_FOUND_USER;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.member.dao.MemberDao;
import roomescape.domain.member.dto.request.MemberRequestDto;
import roomescape.domain.member.dto.response.MemberResponseDto;
import roomescape.domain.member.dto.response.MemberResponseDtoOfNames;
import roomescape.domain.member.model.Member;
import roomescape.global.exception.DuplicateException;
import roomescape.global.exception.NotFoundException;

@Service
public class MemberService {

    private final MemberDao memberDao;

    public MemberService(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public MemberResponseDto saveAdmin(MemberRequestDto memberRequestDto) {
        validateAlreadyExist(memberRequestDto);
        Member member = Member.createAdmin(
            memberRequestDto.name(), memberRequestDto.email(), memberRequestDto.password());
        long savedId = memberDao.save(member);
        member.setId(savedId);
        return MemberResponseDto.from(member);
    }

    public MemberResponseDto saveMember(MemberRequestDto memberRequestDto) {
        validateAlreadyExist(memberRequestDto);
        Member member = Member.createUser(
            memberRequestDto.name(), memberRequestDto.email(), memberRequestDto.password());
        long savedId = memberDao.save(member);
        member.setId(savedId);
        return MemberResponseDto.from(member);
    }

    private void validateAlreadyExist(MemberRequestDto memberRequestDto) {
        memberDao.findByEmail(memberRequestDto.email())
            .ifPresent(member -> {
                throw new DuplicateException(DUPLICATE_USER);
            });
    }

    public Member getMemberOf(String email, String password) {
        return memberDao.findByEmailAndPassword(email, password)
            .orElseThrow(() -> new NotFoundException(NOT_FOUND_USER));
    }

    public Member getMemberFrom(Long memberId) {
        return memberDao.findById(memberId)
            .orElseThrow(() -> new NotFoundException(NOT_FOUND_USER));
    }

    public List<MemberResponseDtoOfNames> getAllMembers() {
        return memberDao.findAll().stream()
            .map(MemberResponseDtoOfNames::from)
            .toList();
    }
}
