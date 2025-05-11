package roomescape.reservation.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.member.entity.Member;
import roomescape.member.repository.MemberRepository;
import roomescape.reservation.dto.ReservationAvailableTimeResponse;
import roomescape.reservation.dto.ReservationCommand;
import roomescape.reservation.dto.ReservationResponse;
import roomescape.reservation.dto.ReservationsByFilterRequest;
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
    public ReservationResponse createReservation(ReservationCommand command) {
        Member member = memberRepository.findById(command.memberId())
                .orElseThrow(() -> new NoSuchElementException("회원이 존재하지 않습니다."));
        ReservationTime time = reservationTimeRepository.findById(command.timeId());
        Theme theme = themeRepository.findById(command.themeId());
        Reservation reservation = Reservation.createIfDateTimeValid(member, command.date(), time, theme);

        if (isDuplicate(reservation)) {
            throw new ReservationDuplicateException("해당 시각의 중복된 예약이 존재합니다.", reservation.getDate(),
                    reservation.getTime().getStartAt(), reservation.getTheme().getName());
        }

        Reservation newReservation = reservationRepository.save(reservation);
        return ReservationResponse.from(newReservation);
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

    public List<ReservationResponse> readAllByFilter(ReservationsByFilterRequest request) {
        List<Reservation> allByFilter = reservationRepository.findAllByFilter(request.themeId(), request.memberId(), request.dateFrom(), request.dateTo());
        return allByFilter.stream()
                .map(ReservationResponse::from)
                .toList();
    }
}
