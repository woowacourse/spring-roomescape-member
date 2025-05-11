package roomescape.reservation.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.member.domain.Member;
import roomescape.member.exception.MemberNotFoundException;
import roomescape.member.repository.MemberRepository;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationDate;
import roomescape.reservation.domain.ReservationDateTime;
import roomescape.reservation.dto.AdminReservationRequest;
import roomescape.reservation.dto.ReservationResponse;
import roomescape.reservation.dto.UserReservationRequest;
import roomescape.reservation.exception.ReservationAlreadyExistsException;
import roomescape.reservation.exception.ReservationNotFoundException;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.reservationtime.domain.ReservationTime;
import roomescape.reservationtime.exception.ReservationTimeNotFoundException;
import roomescape.reservationtime.repository.ReservationTimeRepository;
import roomescape.theme.domain.Theme;
import roomescape.theme.exception.ThemeNotFoundException;
import roomescape.theme.repository.ThemeRepository;

@Service
public class ReservationDefaultService implements ReservationService {
    private final ReservationRepository reservationRepository;
    private final MemberRepository memberRepository;
    private final ReservationTimeRepository timeRepository;
    private final ThemeRepository themeRepository;

    public ReservationDefaultService(ReservationRepository reservationRepository, MemberRepository memberRepository,
                                     ReservationTimeRepository timeRepository, ThemeRepository themeRepository) {
        this.reservationRepository = reservationRepository;
        this.memberRepository = memberRepository;
        this.timeRepository = timeRepository;
        this.themeRepository = themeRepository;
    }

    @Override
    public ReservationResponse createForUser(UserReservationRequest request, Long memberId) {
        return create(request.date(), request.timeId(), request.themeId(), memberId);
    }

    @Override
    public ReservationResponse createForAdmin(AdminReservationRequest request) {
        return create(request.date(), request.timeId(), request.themeId(), request.memberId());
    }

    private ReservationResponse create(LocalDate dateInput, Long timeId, Long themeId, Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(MemberNotFoundException::new);

        ReservationDate date = new ReservationDate(dateInput);
        ReservationTime time = timeRepository.findById(timeId)
                .orElseThrow(ReservationTimeNotFoundException::new);

        ReservationDateTime dateTime = new ReservationDateTime(date, time);

        Theme theme = themeRepository.findById(themeId)
                .orElseThrow(ThemeNotFoundException::new);

        Reservation newReservation = Reservation.createWithoutId(member, dateTime.getDate(),
                dateTime.getTime(), theme);

        if (reservationRepository.existsByDateAndTime(date.getDate(), time.getId())) {
            throw new ReservationAlreadyExistsException();
        }

        return ReservationResponse.from(reservationRepository.add(newReservation));
    }

    @Override
    public List<ReservationResponse> getAll() {
        return ReservationResponse.from(reservationRepository.findAll());
    }

    @Override
    public void deleteById(Long id) {
        int affectedCount = reservationRepository.deleteById(id);
        if (affectedCount != 1) {
            throw new ReservationNotFoundException();
        }
    }
}
