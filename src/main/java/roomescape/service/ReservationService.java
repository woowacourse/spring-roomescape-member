package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.entity.Member;
import roomescape.entity.Reservation;
import roomescape.entity.ReservationTime;
import roomescape.entity.Theme;
import roomescape.exception.impl.*;
import roomescape.repository.MemberRepository;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;
import roomescape.repository.ThemeRepository;

import java.time.LocalDate;
import java.util.List;

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

    public Reservation createMemberReservation(
            final Member member,
            final LocalDate date,
            final long timeId,
            final long themeId
    ) {
        ReservationTime reservationTime = reservationTimeRepository.findById(timeId);
        Theme theme = themeRepository.findById(themeId);

        validateIsNull(member, reservationTime, theme);
        validateDuplicateReservation(date, reservationTime, theme);

        Reservation reservation = Reservation.beforeSave(date, member, reservationTime, theme);
        return reservationRepository.save(reservation);
    }


    public Reservation createAdminReservation(
            final long memberId,
            final LocalDate date,
            final long timeId,
            final long themeId
    ) {
        Member member = memberRepository.findById(memberId);
        ReservationTime reservationTime = reservationTimeRepository.findById(timeId);
        Theme theme = themeRepository.findById(themeId);

        validateIsNull(member, reservationTime, theme);
        validateDuplicateReservation(date, reservationTime, theme);

        Reservation reservation = Reservation.beforeSave(date, member, reservationTime, theme);
        return reservationRepository.save(reservation);
    }

    public List<Reservation> getReservations() {
        return reservationRepository.findAll();
    }

    public List<Reservation> searchReservationsByDateRange(
            final Long memberId,
            final Long themeId,
            final LocalDate dateFrom,
            final LocalDate dateTo
    ) {
        return reservationRepository.findByMemberAndThemeAndDateRange(memberId, themeId, dateFrom, dateTo);
    }

    public void delete(final long id) {
        if (!reservationRepository.isExistByReservationId(id)) {
            throw new ReservationNotFoundException();
        }
        reservationRepository.deleteById(id);
    }

    private void validateIsNull(
            final Member member,
            final ReservationTime reservationTime,
            final Theme theme
    ) {
        if (member == null) {
            throw new MemberNotFountException();
        }
        if (reservationTime == null) {
            throw new ReservationTimeNotFoundException();
        }
        if (theme == null) {
            throw new ThemeNotFoundException();
        }
    }

    private void validateDuplicateReservation(
            final LocalDate date,
            final ReservationTime reservationTime,
            final Theme theme
    ) {
        if (reservationRepository.isDuplicateDateAndTimeAndTheme(date, reservationTime.getStartAt(), theme)) {
            throw new AlreadyReservedException();
        }
    }
}
