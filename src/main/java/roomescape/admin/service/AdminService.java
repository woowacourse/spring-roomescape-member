package roomescape.admin.service;

import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomescape.common.exception.DataExistException;
import roomescape.common.exception.DataNotFoundException;
import roomescape.member.domain.Member;
import roomescape.member.repository.MemberRepository;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationTime;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.reservation.repository.ReservationTimeRepository;
import roomescape.theme.domain.Theme;
import roomescape.theme.repository.ThemeRepository;

@RequiredArgsConstructor
@Service
public class AdminService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ThemeRepository themeRepository;
    private final MemberRepository memberRepository;


    public Long saveByAdmin(final LocalDate date, final Long themeId, final Long timeId, final Long memberId) {
        if (reservationRepository.existsByDateAndTimeIdAndThemeId(date, timeId, themeId)) {
            throw new DataExistException("해당 시간에 이미 예약된 테마입니다.");
        }

        final ReservationTime reservationTime = reservationTimeRepository.findById(timeId)
                .orElseThrow(() -> new DataNotFoundException("해당 예약 시간 데이터가 존재하지 않습니다. id = " + timeId));
        final Theme theme = themeRepository.findById(themeId)
                .orElseThrow(() -> new DataNotFoundException("해당 테마 데이터가 존재하지 않습니다. id = " + themeId));
        final Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new DataNotFoundException("해당 회원 데이터가 존재하지 않습니다. id = " + memberId));
        final Reservation reservation = new Reservation(member, date, reservationTime, theme);

        return reservationRepository.save(reservation);
    }

    public Reservation getById(final Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("해당 예약 데이터가 존재하지 않습니다. id = " + id));
    }

    public List<Reservation> findByInFromTo(final Long themeId, final Long memberId, final LocalDate dateFrom,
                                            final LocalDate dateTo) {

        final List<Reservation> searchedReservations = reservationRepository.findByInFromTo(
                themeId,
                memberId,
                dateFrom,
                dateTo
        );

        return searchedReservations;
    }
}
