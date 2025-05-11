package roomescape.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.Member;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationDate;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.dto.request.AdminReservationRequestDto;
import roomescape.dto.request.ReservationRequestDto;
import roomescape.dto.response.AdminReservationResponse;
import roomescape.dto.response.ReservationResponseDto;
import roomescape.exception.InvalidReservationException;
import roomescape.repository.MemberRepository;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;
import roomescape.repository.ThemeRepository;
import roomescape.service.nowdate.CurrentDateTime;

@Service
public class ReservationService {

    private final MemberRepository memberRepository;
    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ThemeRepository themeRepository;
    private final CurrentDateTime currentDateTime;

    public ReservationService(
        MemberRepository memberRepository,
        ReservationRepository reservationRepository,
        ReservationTimeRepository reservationTimeRepository,
        ThemeRepository themeRepository,
        CurrentDateTime currentDateTime) {
        this.memberRepository = memberRepository;
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
        this.themeRepository = themeRepository;
        this.currentDateTime = currentDateTime;
    }

    public List<ReservationResponseDto> getAllReservations() {
        return reservationRepository.findAll().stream()
            .map(ReservationResponseDto::from)
            .toList();
    }

    public void deleteReservation(Long id) {
        reservationRepository.delete(id);
    }

    public ReservationResponseDto saveReservationOfMember(ReservationRequestDto request,
        long memberId) {
        Reservation reservation = saveReservation(
            request.date(), memberId, request.timeId(), request.themeId());
        return ReservationResponseDto.from(reservation);
    }

    public AdminReservationResponse saveReservationOfAdmin(AdminReservationRequestDto request,
        long memberId) {
        Reservation reservation = saveReservation(
            request.date(), memberId, request.timeId(), request.themeId());
        return new AdminReservationResponse(reservation.getId());
    }

    private Reservation saveReservation(String date, long memberId, long timeId, long themeId) {
        Member member = memberRepository.findById(memberId);
        Reservation reservation = createReservation(member, date, timeId, themeId);
        validateDateTimeAndSaveReservation(reservation, themeId);
        return reservation;
    }

    private Reservation createReservation(Member member, String date, long timeId, long themeId) {
        LocalDateTime currentDateTimeInfo = currentDateTime.get();
        ReservationDate reservationDate = new ReservationDate(LocalDate.parse(date));
        reservationDate.validateDate(currentDateTimeInfo.toLocalDate());
        ReservationTime reservationTime = reservationTimeRepository.findById(timeId);
        Theme theme = themeRepository.findById(themeId);
        return new Reservation(member, reservationDate, reservationTime, theme);
    }

    private void validateDateTimeAndSaveReservation(Reservation reservation, long timeId) {
        reservation.validateDateTime(currentDateTime.get());
        validateAlreadyExistDateTime(reservation.getReservationDate(), timeId);
        reservationRepository.save(reservation);
    }

    private void validateAlreadyExistDateTime(ReservationDate date, long timeId) {
        if (reservationRepository.hasAnotherReservation(date, timeId)) {
            throw new InvalidReservationException("중복된 날짜와 시간을 예약할 수 없습니다.");
        }
    }

    public List<ReservationResponseDto> getAllReservationsOf(
        String dateFrom,
        String dateTo,
        Long memberId,
        Long themeId
    ) {
        List<Reservation> reservations = reservationRepository.findOf(
            dateFrom, dateTo, memberId, themeId);
        return reservations.stream()
            .map(ReservationResponseDto::from)
            .toList();
    }
}
