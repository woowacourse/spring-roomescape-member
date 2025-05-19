package roomescape.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.Member;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTheme;
import roomescape.domain.ReservationTime;
import roomescape.dto.ReservationRequest;
import roomescape.dto.ReservationResponse;
import roomescape.exception.exception.DataNotFoundException;
import roomescape.exception.exception.DuplicateReservationException;
import roomescape.exception.exception.PastReservationTimeException;
import roomescape.repository.MemberRepository;
import roomescape.repository.RoomescapeRepository;
import roomescape.repository.RoomescapeThemeRepository;
import roomescape.repository.RoomescapeTimeRepository;

@Service
public class ReservationService {

    private final RoomescapeRepository roomescapeRepository;
    private final RoomescapeTimeRepository roomescapeTimeRepository;
    private final RoomescapeThemeRepository roomescapeThemeRepository;
    private final MemberRepository memberRepository;

    public ReservationService(final RoomescapeRepository roomescapeRepository,
                              final RoomescapeTimeRepository roomescapeTimeRepository,
                              final RoomescapeThemeRepository roomescapeThemeRepository,
                              final MemberRepository memberRepository) {
        this.roomescapeRepository = roomescapeRepository;
        this.roomescapeTimeRepository = roomescapeTimeRepository;
        this.roomescapeThemeRepository = roomescapeThemeRepository;
        this.memberRepository = memberRepository;
    }

    public List<ReservationResponse> findReservations(
            final Long memberId,
            final Long themeId,
            final LocalDate dateFrom,
            final LocalDate dateTo) {
        List<Reservation> reservations = roomescapeRepository.findAll(memberId, themeId, dateFrom, dateTo);
        return reservations.stream().map(ReservationResponse::of).toList();
    }

    public ReservationResponse addReservation(final ReservationRequest request, final Long memberId) {
        Reservation reservation = toReservation(request, memberId);

        validateFutureDateTime(reservation);
        validateUniqueReservation(reservation);

        Reservation saved = roomescapeRepository.save(reservation);
        return ReservationResponse.of(saved);
    }

    public void removeReservation(final long id) {
        if (!roomescapeRepository.deleteById(id)) {
            throw new DataNotFoundException(String.format("[ERROR] 예약번호 %d번에 해당하는 예약이 없습니다.", id));
        }
    }

    private Reservation toReservation(final ReservationRequest request, final Long memberId) {
        Member member = findMemberById(memberId);
        ReservationTime time = findTimeById(request.timeId());
        ReservationTheme theme = findThemeById(request.themeId());

        return new Reservation(request.date(), member, time, theme);
    }

    private void validateFutureDateTime(final Reservation reservation) {
        LocalDateTime requestDateTime = reservation.toDateTime();
        if (!requestDateTime.isAfter(LocalDateTime.now())) {
            throw new PastReservationTimeException("[ERROR] 현재 시각 이후로 예약해 주세요.");
        }
    }

    private void validateUniqueReservation(final Reservation reservation) {
        if (existsSameReservation(reservation)) {
            throw new DuplicateReservationException("[ERROR] 이미 존재하는 예약입니다. 다른 시간을 선택해 주세요.");
        }
    }

    private ReservationTheme findThemeById(final long themeId) {
        return roomescapeThemeRepository.findById(themeId).orElseThrow(
                () -> new DataNotFoundException(String.format("[ERROR] 예약 테마 %d번에 해당하는 테마가 없습니다.", themeId)));
    }

    private ReservationTime findTimeById(final long timeId) {
        return roomescapeTimeRepository.findById(timeId).orElseThrow(
                () -> new DataNotFoundException(String.format("[ERROR] 예약 시간 %d번에 해당하는 시간이 없습니다.", timeId)));
    }

    private Member findMemberById(final long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new DataNotFoundException(String.format("[ERROR] %d번에 해당하는 멤버가 없습니다.", memberId)));
    }

    private boolean existsSameReservation(final Reservation reservation) {
        return roomescapeRepository.existsByDateAndTime(reservation.getDate(), reservation.getTime());
    }
}
