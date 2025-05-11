package roomescape.reservation.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.auth.dto.LoginMember;
import roomescape.member.entity.Member;
import roomescape.member.repository.MemberRepository;
import roomescape.reservation.dto.AdminReservationRequest;
import roomescape.reservation.dto.ReservationAvailableTimeResponse;
import roomescape.reservation.dto.ReservationRequest;
import roomescape.reservation.dto.ReservationResponse;
import roomescape.reservation.entity.Reservation;
import roomescape.reservation.exception.ReservationDuplicateException;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.theme.entity.Theme;
import roomescape.theme.repository.ThemeRepository;
import roomescape.time.entity.ReservationTime;
import roomescape.time.repository.ReservationTimeRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional(readOnly = true)
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ThemeRepository themeRepository;
    private final MemberRepository memberRepository;

    public ReservationService(ReservationRepository reservationRepository,
                              ReservationTimeRepository reservationTimeRepository,
                              ThemeRepository themeRepository,
                              MemberRepository memberRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
        this.themeRepository = themeRepository;
        this.memberRepository = memberRepository;
    }

    @Transactional
    public ReservationResponse createReservation(ReservationRequest request, LoginMember loginMember) {
        Member member = memberRepository.findById(loginMember.id())
                .orElseThrow(() -> new NoSuchElementException("로그인 정보가 존재하지 않습니다."));
        return createReservationInternal(request.timeId(), request.themeId(), member, request.date());
    }

    @Transactional
    public ReservationResponse createReservation(AdminReservationRequest request) {
        Member member = memberRepository.findById(request.memberId())
                .orElseThrow(() -> new NoSuchElementException("해당 사용자는 존재하지 않습니다."));
        return createReservationInternal(request.timeId(), request.themeId(), member, request.date());
    }

    public List<ReservationResponse> readReservations() {
        return reservationRepository.findAll().stream()
                .map(ReservationResponse::from)
                .toList();
    }

    @Transactional
    public void deleteReservationById(Long id) {
        reservationRepository.deleteById(id);
    }

    private boolean isDuplicate(Reservation reservation) {
        return reservationRepository.findAll().stream()
                .anyMatch(current -> current.isDuplicate(reservation));
    }

    public List<ReservationAvailableTimeResponse> readAvailableReservationTimes(LocalDate date, Long themeId) {
        List<Long> bookedTimeIds = reservationRepository.findBookedTimeIdsByDateAndThemeId(date, themeId);
        List<ReservationTime> reservationTimes = reservationTimeRepository.findAll();

        return reservationTimes.stream()
                .map(time -> ReservationAvailableTimeResponse.of(time, bookedTimeIds.contains(time.getId())))
                .toList();
    }

    private ReservationResponse createReservationInternal(Long request, Long request1, Member member, LocalDate request2) {
        ReservationTime time = reservationTimeRepository.findById(request);
        Theme theme = themeRepository.findById(request1);
        Reservation reservation = Reservation.createIfDateTimeValid(member, request2, time, theme);

        if (isDuplicate(reservation)) {
            throw new ReservationDuplicateException("해당 시각의 중복된 예약이 존재합니다.", reservation.getDate(),
                    reservation.getTime().getStartAt(), reservation.getTheme().getName());
        }

        Reservation newReservation = reservationRepository.save(reservation);
        return ReservationResponse.from(newReservation);
    }
}
