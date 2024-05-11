package roomescape.member.service;

import org.springframework.stereotype.Service;
import roomescape.exception.BadRequestException;
import roomescape.member.dao.MemberJdbcDao;
import roomescape.member.dto.MemberLoginRequest;
import roomescape.member.dto.MemberRegistrationInfo;

@Service
public class MemberLoginService {

    private final MemberJdbcDao memberJdbcDao;

    public MemberLoginService(MemberJdbcDao memberJdbcDao) {
        this.memberJdbcDao = memberJdbcDao;
    }

    public MemberRegistrationInfo findRegistrationInfo(MemberLoginRequest memberLoginRequest) {

        MemberRegistrationInfo registrationInfo = memberJdbcDao.findRegistrationInfoByEmail(
                memberLoginRequest.email());
        validateLoginRequest(registrationInfo);
        return registrationInfo;
    }

    private void validateLoginRequest(MemberRegistrationInfo registrationInfo) {
        if (registrationInfo == null) {
            throw new BadRequestException("등록되지 않은 회원입니다.");
        }
    }

}
