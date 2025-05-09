package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.domain.*;
import roomescape.exception.ReservationException;
import roomescape.persistence.query.CreateReservationQuery;
import roomescape.service.param.CreateReservationParam;
import roomescape.service.result.MemberResult;
import roomescape.service.result.ReservationResult;
import roomescape.service.result.ReservationTimeResult;
import roomescape.service.result.ThemeResult;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReservationService {

    private final ReservationTimeRepository reservationTImeRepository;
    private final ReservationRepository reservationRepository;
    private final ThemeRepository themeRepository;
    private final MemberRepository memberRepository;

    public ReservationService(ReservationTimeRepository reservationTImeRepository, ReservationRepository reservationRepository, ThemeRepository themeRepository,  MemberRepository memberRepository) {
        this.reservationTImeRepository = reservationTImeRepository;
        this.reservationRepository = reservationRepository;
        this.themeRepository = themeRepository;
        this.memberRepository = memberRepository;
    }

    public Long create(CreateReservationParam createReservationParam, LocalDateTime currentDateTime) {
        ReservationTime reservationTime = reservationTImeRepository.findById(createReservationParam.timeId()).orElseThrow(
                () -> new ReservationException(createReservationParam.timeId() + "에 해당하는 정보가 없습니다."));
        Theme theme = themeRepository.findById(createReservationParam.themeId()).orElseThrow(
                () -> new ReservationException(createReservationParam.themeId() + "에 해당하는 정보가 없습니다."));
        Member member = memberRepository.findById(createReservationParam.memberId()).orElseThrow(
                () -> new ReservationException(createReservationParam.memberId() + "에 해당하는 정보가 없습니다."));

        validateUniqueReservation(createReservationParam, reservationTime, theme);
        validateReservationDateTime(createReservationParam, currentDateTime, reservationTime);

        return reservationRepository.create(
                new CreateReservationQuery(
                        member,
                        createReservationParam.date(),
                        reservationTime,
                        theme
                ));
    }

    public void deleteById(Long reservationId) {
        reservationRepository.deleteById(reservationId);
    }

    public List<ReservationResult> findAll() {
        List<Reservation> reservations = reservationRepository.findAll();
        return reservations.stream()
                .map(this::toReservationResult)
                .toList();
    }

    public ReservationResult findById(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ReservationException(reservationId + "에 해당하는 reservation 튜플이 없습니다."));
        return toReservationResult(reservation);
    }

    private ReservationResult toReservationResult(Reservation reservation) {
        return new ReservationResult(
                reservation.getId(),
                MemberResult.from(reservation.getMember()),
                reservation.getDate(),
                ReservationTimeResult.from(reservation.getTime()),
                ThemeResult.from(reservation.getTheme()));
    }

    private void validateUniqueReservation(final CreateReservationParam createReservationParam, final ReservationTime reservationTime, final Theme theme) {
        if (reservationRepository.existsByDateAndTimeIdAndThemeId(createReservationParam.date(), reservationTime.id(), theme.getId())) {
            throw new ReservationException("테마에 대해 날짜와 시간이 중복된 예약이 존재합니다.");
        }
    }

    private void validateReservationDateTime(final CreateReservationParam createReservationParam, final LocalDateTime currentDateTime, final ReservationTime reservationTime) {
        LocalDateTime reservationDateTime = LocalDateTime.of(createReservationParam.date(), reservationTime.startAt());
        if (reservationDateTime.isBefore(currentDateTime)) {
            throw new ReservationException("지난 날짜와 시간에 대한 예약은 불가능합니다.");
        }
        Duration duration = Duration.between(currentDateTime, reservationDateTime);
        if (duration.toMinutes() < 10) {
            throw new ReservationException("예약 시간까지 10분도 남지 않아 예약이 불가합니다.");
        }
    }
}
