package roomescape.application.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.domain.exception.ReservationDuplicatedException;
import roomescape.domain.exception.ResourceNotExistException;
import roomescape.domain.model.Member;
import roomescape.domain.model.Reservation;
import roomescape.domain.model.ReservationTime;
import roomescape.domain.model.Theme;
import roomescape.domain.repository.MemberRepository;
import roomescape.domain.repository.ReservationRepository;
import roomescape.domain.repository.ReservationTimeRepository;
import roomescape.domain.repository.ThemeRepository;
import roomescape.presentation.dto.request.AdminReservationRequest;
import roomescape.presentation.dto.request.ReservationRequest;
import roomescape.presentation.dto.response.ReservationResponse;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ThemeRepository themeRepository;
    private final MemberRepository memberRepository;

    public ReservationService(final ReservationRepository reservationRepository, final ReservationTimeRepository reservationTimeRepository, final ThemeRepository themeRepository, final MemberRepository memberRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
        this.themeRepository = themeRepository;
        this.memberRepository = memberRepository;
    }

    public List<ReservationResponse> findAll() {
        return reservationRepository.findAll().stream()
                .map(info -> ReservationResponse.of(info.id(), info.member(), info.date(), info.reservationTime(), info.theme()))
                .toList();
    }

    public List<ReservationResponse> findByThemeIdAndMemberIdAndDate(final Long themeId, final Long memberId, final LocalDate dateFrom, final LocalDate dateTo) {
        return reservationRepository.findByThemeIdAndMemberIdAndDate(themeId, memberId, dateFrom, dateTo).stream()
                .map(info -> ReservationResponse.of(info.id(), info.member(), info.date(), info.reservationTime(), info.theme()))
                .toList();
    }

    @Transactional
    public void deleteReservation(Long id) {
        int count = reservationRepository.deleteById(id);
        if (count == 0) {
            throw new ResourceNotExistException();
        }
    }

    @Transactional
    public ReservationResponse save(AdminReservationRequest request) {
        ReservationTime reservationTime = getReservationTime(request.timeId());
        Theme theme = getTheme(request.themeId());

        validateSaveReservation(request.date(), request.timeId(), request.themeId(), reservationTime);
        Reservation reservation = new Reservation(
                request.memberId(),
                request.date(),
                reservationTime,
                theme
        );
        return getReservationResponse(reservation);
    }

    @Transactional
    public ReservationResponse save(final Member member, final ReservationRequest request) {
        ReservationTime reservationTime = getReservationTime(request.timeId());
        Theme theme = getTheme(request.themeId());

        validateSaveReservation(request.date(), request.timeId(), request.themeId(), reservationTime);
        Reservation reservation = new Reservation(
                member.getId(),
                request.date(),
                reservationTime,
                theme
        );
        return getReservationResponse(reservation);
    }

    private ReservationTime getReservationTime(final Long timeId) {
        return reservationTimeRepository.findById(timeId);
    }

    private Theme getTheme(final Long themeId) {
        return themeRepository.findById(themeId);
    }

    private void validateSaveReservation(
            LocalDate date,
            Long timeId,
            Long themeId,
            ReservationTime reservationTime
    ) {
        validateIsDuplicate(date, timeId, themeId);
        validateNotPast(date, reservationTime);
    }

    private void validateIsDuplicate(LocalDate date, Long timeId, Long themeId) {
        boolean isReservationExist = reservationRepository.existByDateAndTimeIdAndThemeId(date, timeId, themeId);
        if (isReservationExist) {
            throw new ReservationDuplicatedException();
        }
    }

    private void validateNotPast(LocalDate date, ReservationTime time) {
        LocalDateTime dateTime = LocalDateTime.of(date, time.getStartAt());
        if (dateTime.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("[ERROR] 현재보다 과거 시간에는 예약이 불가능합니다.");
        }
    }

    private ReservationResponse getReservationResponse(Reservation reservation) {
        Reservation savedReservation = reservationRepository.save(reservation);
        Member member = memberRepository.findById(savedReservation.getMemberId());
        return ReservationResponse.of(savedReservation.getId(), member, savedReservation.getDate(), savedReservation.getTime(), savedReservation.getTheme());
    }
}
