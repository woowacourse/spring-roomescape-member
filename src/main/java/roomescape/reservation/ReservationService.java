package roomescape.reservation;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import roomescape.auth.dto.LoginMember;
import roomescape.exception.custom.reason.reservation.ReservationConflictException;
import roomescape.exception.custom.reason.reservation.ReservationNotExistsMemberException;
import roomescape.exception.custom.reason.reservation.ReservationNotExistsThemeException;
import roomescape.exception.custom.reason.reservation.ReservationNotExistsTimeException;
import roomescape.exception.custom.reason.reservation.ReservationNotFoundException;
import roomescape.exception.custom.reason.reservation.ReservationPastDateException;
import roomescape.exception.custom.reason.reservation.ReservationPastTimeException;
import roomescape.member.MemberRepository;
import roomescape.reservation.dto.ReservationRequest;
import roomescape.reservation.dto.ReservationResponse;
import roomescape.reservationtime.ReservationTime;
import roomescape.reservationtime.ReservationTimeRepository;
import roomescape.theme.ThemeRepository;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ThemeRepository themeRepository;
    private final MemberRepository memberRepository;

    @Autowired
    public ReservationService(
            final ReservationRepository reservationRepository,
            final ReservationTimeRepository reservationTimeRepository,
            final ThemeRepository themeRepository,
            final MemberRepository memberRepository
    ) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
        this.themeRepository = themeRepository;
        this.memberRepository = memberRepository;
    }

    public ReservationResponse create(final ReservationRequest request, final LoginMember member) {
        validateExistsReservationTime(request);
        validateExistsTheme(request);
        validateExistsMember(member);
        validateDuplicateDateTime(request);
        validatePastDateTime(request);

        final Reservation reservation = new Reservation(request.date());
        final Long id = reservationRepository.save(reservation, request.timeId(), request.themeId(), member.id());
        final Reservation savedReservation = reservationRepository.findById(id);
        return ReservationResponse.from(savedReservation);
    }

    public List<ReservationResponse> readAll() {
        return reservationRepository.findAll().stream()
                .map(ReservationResponse::from)
                .toList();
    }

    public void deleteById(final Long id) {
        if (!reservationRepository.existsById(id)) {
            throw new ReservationNotFoundException();
        }
        reservationRepository.delete(id);
    }

    private void validatePastDateTime(final ReservationRequest request) {
        final LocalDate today = LocalDate.now();
        final LocalDate reservationDate = request.date();
        if (reservationDate.isBefore(today)) {
            throw new ReservationPastDateException();
        }
        if (reservationDate.isEqual(today)) {
            validatePastTime(request);
        }
    }

    private void validatePastTime(final ReservationRequest request) {
        final ReservationTime reservationTime = reservationTimeRepository.findById(request.timeId());
        if (reservationTime.isBefore(LocalTime.now())) {
            throw new ReservationPastTimeException();
        }
    }

    private void validateDuplicateDateTime(final ReservationRequest request) {
        if (reservationRepository.existsByReservationTimeIdAndDate(request.timeId(), request.date())) {
            throw new ReservationConflictException();
        }
    }

    private void validateExistsReservationTime(final ReservationRequest request) {
        if (!reservationTimeRepository.existsById(request.timeId())) {
            throw new ReservationNotExistsTimeException();
        }
    }

    private void validateExistsTheme(final ReservationRequest request) {
        if (!themeRepository.existsById(request.themeId())) {
            throw new ReservationNotExistsThemeException();
        }
    }

    private void validateExistsMember(final LoginMember member) {
        if(!memberRepository.existsById(member.id())){
            throw new ReservationNotExistsMemberException();
        }
    }
}
