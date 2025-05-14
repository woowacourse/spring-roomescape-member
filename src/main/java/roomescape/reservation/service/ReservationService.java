package roomescape.reservation.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.admin.dto.request.AdminReservationCreateRequest;
import roomescape.member.domain.Member;
import roomescape.member.exception.MemberNotFoundException;
import roomescape.member.repository.MemberRepository;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.dto.request.ReservationCreateRequest;
import roomescape.reservation.dto.response.ReservationResponse;
import roomescape.reservation.exception.ReservationAlreadyExistsException;
import roomescape.reservation.exception.ReservationNotFoundException;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.reservationtime.domain.ReservationTime;
import roomescape.reservationtime.repository.ReservationTimeRepository;
import roomescape.theme.domain.Theme;
import roomescape.theme.repository.ThemeRepository;

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
                .map(ReservationResponse::from)
                .toList();
    }

    public void delete(long id) {
        if (!reservationRepository.deleteById(id)) {
            throw new ReservationNotFoundException("요청한 id와 일치하는 예약 정보가 없습니다.");
        }
    }

    public ReservationResponse create(final Long memberId, final ReservationCreateRequest request) {
        if (reservationRepository.existsByDateAndTimeId(request.date(), request.timeId())) {
            throw new ReservationAlreadyExistsException("해당 시간에 이미 예약이 존재합니다.");
        }
        ReservationTime time = reservationTimeRepository.findById(request.timeId())
                .orElseThrow(() -> new ReservationNotFoundException("요청한 id와 일치하는 예약 시간 정보가 없습니다."));

        validateDateTime(request.date(), time.getStartAt());

        Theme theme = themeRepository.findById(request.themeId())
                .orElseThrow(() -> new ReservationNotFoundException("요청한 id와 일치하는 테마 정보가 없습니다."));

        Member member = memberRepository.findById(memberId)
                .orElseThrow(MemberNotFoundException::new);

        Reservation newReservation = reservationRepository.save(
                Reservation.withUnassignedId(request.date(), member, time, theme));
        return ReservationResponse.from(newReservation);
    }

    public ReservationResponse create(final AdminReservationCreateRequest request) {
        if (reservationRepository.existsByDateAndTimeId(request.date(), request.timeId())) {
            throw new ReservationAlreadyExistsException("해당 시간에 이미 예약이 존재합니다.");
        }
        ReservationTime time = reservationTimeRepository.findById(request.timeId())
                .orElseThrow(() -> new ReservationNotFoundException("요청한 id와 일치하는 예약 시간 정보가 없습니다."));

        validateDateTime(request.date(), time.getStartAt());

        Theme theme = themeRepository.findById(request.themeId())
                .orElseThrow(() -> new ReservationNotFoundException("요청한 id와 일치하는 테마 정보가 없습니다."));

        Member member = memberRepository.findById(request.memberId())
                .orElseThrow(MemberNotFoundException::new);

        Reservation newReservation = reservationRepository.save(
                Reservation.withUnassignedId(request.date(), member, time, theme));
        return ReservationResponse.from(newReservation);
    }

    private void validateDateTime(LocalDate date, LocalTime time) {
        if (LocalDateTime.of(date, time).isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("예약 시간이 현재 시간보다 이전일 수 없습니다.");
        }
    }

    public List<ReservationResponse> getFilteredReservations(final Long themeId, final Long memberId,
                                                             final LocalDate dateFrom, final LocalDate dateTo) {
        return reservationRepository.searchByFilters(themeId, memberId, dateFrom, dateTo).stream()
                .map(ReservationResponse::from)
                .toList();
    }
}
