package roomescape.application;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import roomescape.domain.member.repository.MemberRepository;
import roomescape.domain.reservation.Reservation;
import roomescape.domain.reservation.repository.ReservationRepository;
import roomescape.domain.theme.Theme;
import roomescape.domain.theme.repository.ThemeRepository;
import roomescape.domain.time.ReservationTime;
import roomescape.dto.reservation.ReservationInfo;

@Service
public class ReservationService {
    private final ReservationTimeService reservationTimeService;
    private final ReservationRepository reservationRepository;
    private final ThemeRepository themeRepository;
    private final MemberRepository memberRepository;

    public ReservationService(ReservationTimeService reservationTimeService,
                              ReservationRepository reservationRepository,
                              ThemeRepository themeRepository,
                              MemberRepository memberRepository) {
        this.reservationTimeService = reservationTimeService;
        this.reservationRepository = reservationRepository;
        this.themeRepository = themeRepository;
        this.memberRepository = memberRepository;
    }

    public Reservation reserve(ReservationInfo request) {
        ReservationTime time = reservationTimeService.getReservationTime(request.timeId());
        if (reservationRepository.existsByReservationDateTimeAndTheme(request.date(), time.getId(),
                request.themeId())) {
            throw new IllegalArgumentException("이미 예약된 날짜, 시간입니다.");
        }

        Reservation reservation = new Reservation(request.date(), time, getTheme(request.themeId()));
        return reservationRepository.save(reservation);
    }

    private Theme getTheme(long themeId) {
        return themeRepository.findById(themeId)
                .orElseThrow(() -> new IllegalArgumentException("테마가 존재하지 않습니다."));
    }

    public List<Reservation> getReservations() {
        return reservationRepository.findAll();
    }

    public void cancel(long id) {
        Optional<Reservation> reservation = reservationRepository.findById(id);
        if (reservation.isEmpty()) {
            throw new IllegalArgumentException("존재하지 않는 예약입니다.");
        }
        reservationRepository.deleteById(id);
    }
}
