package roomescape.console.view;

import java.util.List;
import roomescape.dto.ReservationResponse;
import roomescape.dto.ReservationTimeResponse;

public class OutputView {
    private static final int EMPTY_SIZE = 0;
    private static final String NEW_LINE = System.lineSeparator();

    public void printStartMessage() {
        System.out.println("=== 방탈출 어드민 프로그램을 시작합니다 ===");
    }

    public void printAdminFunction() {
        System.out.println("""
                [ 관리자 메뉴 목록 ]
                1. 예약 조회
                2. 예약 추가
                3. 예약 삭제
                4. 예약 시간 조회
                5. 예약 시간 추가
                6. 예약 시간 삭제
                7. 프로그램 종료
                """
        );
    }

    public void printAllReservations(List<ReservationResponse> allReservations) {
        if (allReservations.size() == EMPTY_SIZE) {
            System.out.println("존재하는 예약이없습니다." + NEW_LINE);
            return;
        }
        System.out.println("[예약 목록]");
        for (ReservationResponse response : allReservations) {
            System.out.printf(
                    "id: %s, name: %s, date: %s, time-id: %d, start-at: %s" + NEW_LINE,
                    response.id(),
                    response.name(),
                    response.date(),
                    response.time().id(),
                    response.time().startAt()
            );
        }
        System.out.print(NEW_LINE);
    }

    public void printCreateReservationSuccessMessage(ReservationResponse response) {
        System.out.printf("""
                        예약 생성이 완료되었습니다.
                        생성된 예약 정보 - id: %s, name: %s, date: %s, time-id: %d, start-at: %s
                         """ + NEW_LINE,
                response.id(),
                response.name(),
                response.date(),
                response.time().id(),
                response.time().startAt()
        );
    }

    public void printDeleteReservationSuccessMessage() {
        System.out.println("예약 삭제가 완료되었습니다." + NEW_LINE);
    }

    public void printAllReservationTimes(List<ReservationTimeResponse> allReservationTimes) {
        if (allReservationTimes.size() == EMPTY_SIZE) {
            System.out.println("존재하는 예약 시간이없습니다." + NEW_LINE);
            return;
        }
        System.out.println("[예약시간 목록]");
        for (ReservationTimeResponse response : allReservationTimes) {
            System.out.printf(
                    "id: %d, start-at: %s" + NEW_LINE,
                    response.id(),
                    response.startAt()
            );
        }
        System.out.print(NEW_LINE);
    }

    public void printCreateTimeSuccessMessage(ReservationTimeResponse response) {
        System.out.printf("""
                         예약 시간 생성이 완료되었습니다.
                         생성된 예약시간 정보 - id: %d, start-at: %s
                        """ + NEW_LINE,
                response.id(),
                response.startAt());
    }

    public void printDeleteTimeSuccessMessage() {
        System.out.println("예약 시간 삭제가 완료되었습니다." + NEW_LINE);
    }
}
