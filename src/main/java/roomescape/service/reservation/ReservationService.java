package roomescape.service.reservation;

import org.springframework.stereotype.Service;
import roomescape.controller.reservation.ReservationRequest;
import roomescape.controller.reservation.ReservationResponse;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.repository.MemberRepository;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;
import roomescape.repository.ThemeRepository;
import roomescape.repository.exception.TimeNotFoundException;
import roomescape.service.reservation.exception.PreviousTimeException;
import roomescape.service.reservation.exception.ReservationDuplicatedException;
import roomescape.service.reservation.exception.ReservationNotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final MemberRepository memberRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ThemeRepository themeRepository;

    public ReservationService(final ReservationRepository reservationRepository,
                              final MemberRepository memberRepository,
                              final ReservationTimeRepository reservationTimeRepository,
                              final ThemeRepository themeRepository) {
        this.reservationRepository = reservationRepository;
        this.memberRepository = memberRepository;
        this.reservationTimeRepository = reservationTimeRepository;
        this.themeRepository = themeRepository;
    }

    public List<ReservationResponse> getReservations() {
        return reservationRepository.findAll().stream()
                .map(this::assignTime)
                .map(this::assignTheme)
                .map(ReservationResponse::from)
                .toList();
    }

    private Reservation assignTime(final Reservation reservation) {
        final ReservationTime time = reservationTimeRepository.findById(reservation.getTime().getId())
                .orElse(reservation.getTime());

        return reservation.assignTime(time);
    }

    private Reservation assignTheme(final Reservation reservation) {
        final Theme theme = themeRepository.findById(reservation.getTheme().getId())
                .orElse(reservation.getTheme());

        return reservation.assignTheme(theme);
    }

    public ReservationResponse addReservation(final ReservationRequest reservationRequest) {
        validateMemberAndThemeExist(reservationRequest);

        final Reservation parsedReservation = reservationRequest.toDomain();
        validateTimeDuplicated(
                parsedReservation.getDate(),
                parsedReservation.getTime().getId(),
                parsedReservation.getTheme().getId());
        validateTimeExistAndFuture(
                parsedReservation.getDate(),
                parsedReservation.getTime().getId());

        final Reservation savedReservation = reservationRepository.save(parsedReservation);
        return ReservationResponse.from(savedReservation);
    }

    private void validateMemberAndThemeExist(final ReservationRequest reservationRequest) {
        memberRepository.fetchById(reservationRequest.memberId());
        themeRepository.fetchById(reservationRequest.themeId());
    }

    private void validateTimeDuplicated(LocalDate date, long timeId, long themeId) {
        if (reservationRepository.existsByDateAndTimeIdAndThemeId(date, timeId, themeId)) {
            throw new ReservationDuplicatedException("중복된 시간으로 예약이 불가합니다.");
        }
    }

    private void validateTimeExistAndFuture(LocalDate date, long timeId) {
        final ReservationTime time = reservationTimeRepository.findById(timeId)
                .orElseThrow(() -> new TimeNotFoundException("존재 하지 않는 시간 입니다."));


        if (date.atTime(time.getStartAt()).isBefore(LocalDateTime.now())) {
            throw new PreviousTimeException("지난 시간으로 예약할 수 없습니다.");
        }
    }

    public int deleteReservation(final Long id) {
        final int deletedCount = reservationRepository.delete(id);
        validateNotFound(deletedCount);

        return deletedCount;
    }

    private void validateNotFound(final int deletedCount) {
        if (deletedCount == 0) {
            throw new ReservationNotFoundException("존재하지 않는 예약 입니다.");
        }
    }
}
