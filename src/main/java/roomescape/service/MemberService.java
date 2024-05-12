package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.dao.MemberDao;
import roomescape.domain.Email;
import roomescape.domain.Password;
import roomescape.repository.ReservationRepository;
import roomescape.service.dto.input.MemberCreateInput;
import roomescape.service.dto.input.MemberLoginInput;
import roomescape.service.dto.output.MemberOutput;

@Service
public class MemberService {

    private final ReservationRepository reservationRepository;
    private final MemberDao memberDao;

    public MemberService(final ReservationRepository reservationRepository,
                         final MemberDao memberDao) {
        this.reservationRepository = reservationRepository;
        this.memberDao = memberDao;
    }

    public MemberOutput createMember(final MemberCreateInput input) {
        final var member = memberDao.create(input.toMember());
        return MemberOutput.from(member);
    }

    public MemberOutput getMember(final MemberLoginInput input) {
        final var member = reservationRepository.getMember(new Email(input.email()), new Password(input.password()));
        return MemberOutput.from(member);
    }

    public MemberOutput getMember(final Long id) {
        final var member = reservationRepository.getMemberById(id);
        return MemberOutput.from(member);
    }

    public List<MemberOutput> getAllMembers() {
        final var members = memberDao.getAll();
        return MemberOutput.list(members);
    }
}
