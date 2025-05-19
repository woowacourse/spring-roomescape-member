package roomescape.business.service;

import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;
import roomescape.business.Member;
import roomescape.business.Reservation;
import roomescape.business.ReservationTheme;
import roomescape.business.ReservationTime;
import roomescape.exception.MemberException;
import roomescape.exception.ReservationException;
import roomescape.exception.ReservationThemeException;
import roomescape.exception.ReservationTimeException;
import roomescape.persistence.MemberRepository;
import roomescape.persistence.ReservationRepository;
import roomescape.persistence.ReservationThemeRepository;
import roomescape.persistence.ReservationTimeRepository;
import roomescape.presentation.dto.request.AdminReservationRequestDto;
import roomescape.presentation.dto.request.ReservationRequestDto;
import roomescape.presentation.dto.response.MemberResponseDto;
import roomescape.presentation.dto.response.ReservationResponseDto;
import roomescape.presentation.dto.response.ReservationThemeResponseDto;
import roomescape.presentation.dto.response.ReservationTimeResponseDto;

@Named
@Transactional
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ReservationThemeRepository reservationThemeRepository;
    private final MemberRepository memberRepository;

    @Inject
    public ReservationService(ReservationRepository reservationRepository,
                              ReservationTimeRepository reservationTimeRepository,
                              ReservationThemeRepository reservationThemeRepository,
                              MemberRepository memberRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
        this.reservationThemeRepository = reservationThemeRepository;
        this.memberRepository = memberRepository;
    }

    public List<ReservationResponseDto> readReservationAll() {
        List<Reservation> reservations = reservationRepository.findAll();
        return reservations.stream()
                .map(reservation -> new ReservationResponseDto(
                        reservation.getId(),
                        reservation.getDate(),
                        new MemberResponseDto(
                                reservation.getMember().getId(),
                                reservation.getMember().getName(),
                                reservation.getMember().getEmail()
                        ),
                        new ReservationTimeResponseDto(
                                reservation.getTime().getId(),
                                reservation.getTime().getStartAt()
                        ),
                        new ReservationThemeResponseDto(
                                reservation.getTheme().getId(),
                                reservation.getTheme().getName(),
                                reservation.getTheme().getDescription(),
                                reservation.getTheme().getThumbnail()
                        )
                ))
                .toList();
    }

    public ReservationResponseDto readReservationOne(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ReservationException("존재하지 않는 예약입니다."));
        return new ReservationResponseDto(
                reservation.getId(),
                reservation.getDate(),
                new MemberResponseDto(
                        reservation.getMember().getId(),
                        reservation.getMember().getName(),
                        reservation.getMember().getEmail()
                ),
                new ReservationTimeResponseDto(
                        reservation.getTime().getId(),
                        reservation.getTime().getStartAt()
                ),
                new ReservationThemeResponseDto(
                        reservation.getTheme().getId(),
                        reservation.getTheme().getName(),
                        reservation.getTheme().getDescription(),
                        reservation.getTheme().getThumbnail()
                )
        );
    }

    public List<ReservationResponseDto> searchReservationsForThemeAndMemberAndDate(
            Long themeId,
            Long memberId,
            LocalDate dateFrom,
            LocalDate dateTo) {
        List<Reservation> searchedReservations = reservationRepository.findAllByThemeAndMemberAndDate(
                themeId,
                memberId,
                dateFrom,
                dateTo
        );
        return searchedReservations.stream()
                .map(reservation -> new ReservationResponseDto(
                                reservation.getId(),
                                reservation.getDate(),
                                new MemberResponseDto(
                                        reservation.getMember().getId(),
                                        reservation.getMember().getName(),
                                        reservation.getMember().getEmail()
                                ),
                                new ReservationTimeResponseDto(
                                        reservation.getTime().getId(),
                                        reservation.getTime().getStartAt()
                                ),
                                new ReservationThemeResponseDto(
                                        reservation.getTheme().getId(),
                                        reservation.getTheme().getName(),
                                        reservation.getTheme().getDescription(),
                                        reservation.getTheme().getThumbnail()
                                )
                        )
                ).toList();
    }

    public Long createReservation(ReservationRequestDto reservationDto, Long memberId) {
        ReservationTime reservationTime = reservationTimeRepository.findById(reservationDto.timeId())
                .orElseThrow(() -> new ReservationTimeException("존재하지 않는 예약 시간입니다."));
        ReservationTheme theme = reservationThemeRepository.findById(reservationDto.themeId())
                .orElseThrow(() -> new ReservationThemeException("존재하지 않는 예약 테마입니다."));
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException("존재하지 않는 사용자입니다."));
        validatePastDateTime(reservationDto.date(), reservationTime.getStartAt());
        Reservation reservation = new Reservation(member, reservationDto.date(), reservationTime, theme);
        validateDuplicatedReservation(reservation);
        return reservationRepository.add(reservation);
    }

    public Long createReservationWithMember(AdminReservationRequestDto reservationDto) {
        return createReservation(
                new ReservationRequestDto(
                        reservationDto.date(),
                        reservationDto.timeId(),
                        reservationDto.themeId()),
                reservationDto.memberId()
        );
    }

    private void validatePastDateTime(LocalDate date, LocalTime time) {
        LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
        LocalDateTime reservationDateTime = LocalDateTime.of(date, time);
        if (reservationDateTime.isBefore(now)) {
            throw new ReservationException("과거 일시로 예약을 생성할 수 없습니다.");
        }
    }

    private void validateDuplicatedReservation(Reservation reservation) {
        if (reservationRepository.existsByReservation(reservation)) {
            throw new ReservationException("이미 예약되었습니다.");
        }
    }

    public void deleteReservation(Long id) {
        reservationRepository.deleteById(id);
    }
}
