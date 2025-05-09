package roomescape.service;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import roomescape.domain.Member;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.dto.other.ReservationCreationContent;
import roomescape.dto.other.ReservationSearchCondition;
import roomescape.exception.BadRequestException;
import roomescape.exception.NotFoundException;
import roomescape.repository.member.MemberRepository;
import roomescape.repository.reservation.ReservationRepository;
import roomescape.repository.reservationtime.ReservationTimeRepository;
import roomescape.repository.theme.ThemeRepository;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ThemeRepository themeRepository;
    private final MemberRepository memberRepository;

    public ReservationService(
            ReservationRepository reservationRepository,
            ReservationTimeRepository reservationTimeRepository,
            ThemeRepository themeRepository,
            MemberRepository memberRepository
    ) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
        this.themeRepository = themeRepository;
        this.memberRepository = memberRepository;
    }

    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    public List<Reservation> getAllReservationsByFilter(ReservationSearchCondition condition) {
        return reservationRepository.findAllByFilter(condition);
    }

    public Reservation getReservationById(long id) {
        return loadReservationById(id);
    }

    public long saveReservation(ReservationCreationContent content) {
        Member member = loadMemberById(content.memberId());
        Theme theme = loadThemeById(content.themeId());
        ReservationTime reservationTime = loadReservationTimeById(content.timeId());
        Reservation reservation = Reservation.createWithoutId(
                member, content.date(), reservationTime, theme);

        reservation.validatePastDateTime();
        validateAlreadyReserved(reservation);

        return reservationRepository.add(reservation);
    }

    public void deleteReservation(long id) {
        validateReservationById(id);
        reservationRepository.deleteById(id);
    }

    private Reservation loadReservationById(long reservationId) {
        Optional<Reservation> reservation = reservationRepository.findById(reservationId);
        return reservation
                .orElseThrow(() -> new NotFoundException("[ERROR] ID에 해당하는 예약이 존재하지 않습니다."));
    }

    private Member loadMemberById(long memberId) {
        Optional<Member> member = memberRepository.findById(memberId);
        return member
                .orElseThrow(() -> new NotFoundException("[ERROR] ID에 해당하는 회원이 존재하지 않습니다."));
    }

    private ReservationTime loadReservationTimeById(long reservationTimeId) {
        Optional<ReservationTime> reservationTime = reservationTimeRepository.findById(reservationTimeId);
        return reservationTime
                .orElseThrow(() -> new NotFoundException("[ERROR] ID에 해당하는 예약시간이 존재하지 않습니다."));
    }

    private Theme loadThemeById(long themeId) {
        Optional<Theme> theme = themeRepository.findById(themeId);
        return theme
                .orElseThrow(() -> new NotFoundException("[ERROR] ID에 해당하는 테마가 존재하지 않습니다."));
    }

    private void validateAlreadyReserved(Reservation reservation) {
        boolean isAlreadyReserved = reservationRepository.checkAlreadyReserved(
                reservation.getDate(), reservation.getTime().getId(), reservation.getTheme().getId());
        if (isAlreadyReserved) {
            throw new BadRequestException("[ERROR] 이미 존재하는 예약입니다.");
        }
    }

    private void validateReservationById(long reservationId) {
        Optional<Reservation> reservation = reservationRepository.findById(reservationId);
        if (reservation.isEmpty()) {
            throw new NotFoundException("[ERROR] ID에 해당하는 예약이 존재하지 않습니다.");
        }
    }
}
