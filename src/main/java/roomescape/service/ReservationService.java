package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.repository.MemberRepository;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;
import roomescape.repository.ThemeRepository;
import roomescape.domain.Member;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.dto.request.AdminReservationRequest;
import roomescape.dto.request.ReservationRequest;

import java.time.LocalDate;
import java.util.List;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ThemeRepository themeRepository;
    private final MemberRepository memberRepository;

    public ReservationService(ReservationRepository reservationRepository, ReservationTimeRepository reservationTimeRepository, ThemeRepository themeRepository, MemberRepository memberRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
        this.themeRepository = themeRepository;
        this.memberRepository = memberRepository;
    }

    public Reservation save(Member member, ReservationRequest reservationRequest) {
        validateReservation(reservationRequest);
        ReservationTime reservationTime = reservationTimeRepository.findById(reservationRequest.timeId());
        Theme theme = themeRepository.findById(reservationRequest.themeId());

        Reservation reservation = reservationRequest.toEntity(member, reservationTime, theme);
        return reservationRepository.insert(reservation);
    }

    public Reservation save(Member member, AdminReservationRequest adminReservationRequest) {
        LocalDate date = adminReservationRequest.getDate();
        Long themeId = adminReservationRequest.getThemeId();
        Long timeId = adminReservationRequest.getTimeId();
        Long memberId = adminReservationRequest.getMemberId();

        Member requestMember = memberRepository.findMemberById(memberId);
        Theme requestTheme = themeRepository.findById(themeId);
        ReservationTime requestTime = reservationTimeRepository.findById(timeId);

        Reservation reservation = new Reservation(requestMember, date, requestTime, requestTheme);
        return reservationRepository.insert(reservation);
    }

    private void validateReservation(ReservationRequest reservationRequest) {
        ReservationTime requestTime = findRequestTime(reservationRequest);
        LocalDate requestDate = reservationRequest.date();
        List<Reservation> reservations = reservationRepository.selectAll();

        requestTime.validateNotPast(requestDate);
        validateNotDuplicated(reservations, requestDate, requestTime);
    }

    private ReservationTime findRequestTime(ReservationRequest reservationRequest) {
        Long timeId = reservationRequest.timeId();
        return reservationTimeRepository.findById(timeId);
    }

    private void validateNotDuplicated(List<Reservation> reservations, LocalDate requestDate, ReservationTime requestTime) {
        reservations.forEach(reservation -> reservation.validateDifferentDateTime(requestDate, requestTime));
    }

    public List<Reservation> findAll() {
        return reservationRepository.selectAll();
    }

    public void delete(long id) {
        reservationRepository.deleteById(id);
    }

    public List<Reservation> findFilteredReservation(Long memberId, Long themeId, LocalDate dateFrom, LocalDate dateTo) {
        return reservationRepository.findReservationsBy(memberId, themeId, dateFrom, dateTo);
    }
}
