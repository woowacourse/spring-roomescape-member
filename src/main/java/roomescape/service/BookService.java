package roomescape.service;

import java.time.LocalDateTime;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;
import roomescape.domain.LoginMember;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationName;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.dto.reservation.ReservationRequest;
import roomescape.dto.reservation.ReservationResponse;
import roomescape.dto.reservation.UserReservationRequest;
import roomescape.exception.DuplicateContentException;
import roomescape.exception.InvalidRequestException;
import roomescape.exception.NotFoundException;
import roomescape.repository.MemberRepository;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;
import roomescape.repository.ThemeRepository;

@Component
public class BookService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ThemeRepository themeRepository;
    private final MemberRepository memberRepository;

    public BookService(ReservationRepository reservationRepository, ReservationTimeRepository reservationTimeRepository, ThemeRepository themeRepository, MemberRepository memberRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
        this.themeRepository = themeRepository;
        this.memberRepository = memberRepository;
    }

    public ReservationResponse createUserReservation(UserReservationRequest dto, LoginMember member) {
        Reservation reservation = createReservationWithoutId(dto, member);
        return createReservation(reservation);
    }

    public ReservationResponse createAdminReservation(ReservationRequest dto) {
        LoginMember member = memberRepository.findById(dto.memberId())
                .orElseThrow(() -> new NotFoundException("[ERROR] 회원을 찾을 수 없습니다."));
        ReservationName reservationName = new ReservationName(member.getId(), member.getName());

        Reservation reservation = createReservationWithoutId(dto, reservationName);
        return createReservation(reservation);
    }

    public ReservationResponse createReservation(Reservation reservation) {
        try {
            long id = reservationRepository.save(reservation);
            Reservation newReservation = new Reservation(id, reservation.getName(), reservation.getDate(), reservation.getTime(), reservation.getTheme());
            return ReservationResponse.from(newReservation, newReservation.getTime(), newReservation.getTheme());
        } catch (DuplicateKeyException e) {
            throw new DuplicateContentException("[ERROR] 해당 날짜와 테마로 이미 예약된 내역이 존재합니다.");
        }
    }

    private Reservation createReservationWithoutId(UserReservationRequest dto, LoginMember member) {
        ReservationRequest request = new ReservationRequest(dto.date(), dto.timeId(), dto.themeId(), member.getId());
        return createReservationWithoutId(request, new ReservationName(member.getId(), member.getName()));
    }

    private Reservation createReservationWithoutId(ReservationRequest dto, ReservationName reservationName) {
        ReservationTime reservationTime = reservationTimeRepository.findById(dto.timeId())
                .orElseThrow(() -> new NotFoundException("[ERROR] 예약 시간을 찾을 수 없습니다. id : " + dto.timeId()));

        validateRequestDateTime(LocalDateTime.of(dto.date(), reservationTime.getStartAt()));

        Theme theme = themeRepository.findById(dto.themeId())
                .orElseThrow(() -> new NotFoundException("[ERROR] 테마를 찾을 수 없습니다. id : " + dto.themeId()));

        return dto.createWithoutId(reservationTime, theme, reservationName);
    }

    private void validateRequestDateTime(LocalDateTime requestDateTime) {
        LocalDateTime currentDateTime = LocalDateTime.now();
        if (requestDateTime.isBefore(currentDateTime) || requestDateTime.equals(currentDateTime)) {
            throw new InvalidRequestException("[ERROR] 현 시점 이후의 날짜와 시간을 선택해주세요.");
        }
    }

    public void deleteReservation(Long id) {
        int deletedReservationCount = reservationRepository.deleteById(id);

        if (deletedReservationCount == 0) {
            throw new NotFoundException("[ERROR] 등록된 예약번호만 삭제할 수 있습니다. 입력된 번호는 " + id + "입니다.");
        }
    }
}
