package roomescape.reservation.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;
import javax.sql.DataSource;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;

@SpringBootTest(properties = "spring.datasource.url=jdbc:h2:mem:merge-concurrency")
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
class ReservationTimeMergeConcurrencyTest {

    @Autowired
    private DataSource dataSource;

    @DisplayName("두 트랜잭션이 MERGE 대상 행을 반대 순서로 잡으면 데드락 또는 락 타임아웃이 발생할 수 있다.")
    @Test
    void concurrentMergeCanCauseDeadlockOrLockTimeout() throws Exception {
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        CountDownLatch firstMergeDone = new CountDownLatch(2);

        try {
            Future<Throwable> first = executorService.submit(() -> mergeInOppositeOrder(
                    1L,
                    2L,
                    LocalTime.of(10, 30),
                    LocalTime.of(11, 30),
                    firstMergeDone
            ));
            Future<Throwable> second = executorService.submit(() -> mergeInOppositeOrder(
                    2L,
                    1L,
                    LocalTime.of(11, 45),
                    LocalTime.of(10, 45),
                    firstMergeDone
            ));

            List<Throwable> failures = Arrays.asList(
                            first.get(5, TimeUnit.SECONDS),
                            second.get(5, TimeUnit.SECONDS)
                    ).stream()
                    .filter(Objects::nonNull)
                    .toList();

            assertThat(failures)
                    .as("동시에 실행된 MERGE 중 하나 이상은 데드락 또는 락 타임아웃으로 실패해야 한다.")
                    .isNotEmpty();
            List<String> failureMessages = failures.stream()
                    .map(this::rootCauseMessage)
                    .toList();

            System.out.println("MERGE concurrency failure messages = " + failureMessages);

            assertThat(failureMessages)
                    .anySatisfy(message -> assertThat(message)
                            .containsIgnoringCase("lock"));
        } finally {
            executorService.shutdownNow();
        }
    }

    @DisplayName("같은 행에 대한 동시 upsert는 UPDATE 먼저 전략으로 락 실패 없이 처리할 수 있다.")
    @Test
    void updateFirstUpsertDoesNotCauseLockFailureForSameRow() throws Exception {
        int threadCount = 20;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch startGate = new CountDownLatch(1);

        try {
            List<Future<Throwable>> futures = IntStream.range(0, threadCount)
                    .mapToObj(index -> executorService.submit(() -> updateFirstSameRow(
                            1L,
                            LocalTime.of(23, index),
                            startGate
                    )))
                    .toList();

            startGate.countDown();

            List<Throwable> failures = futures.stream()
                    .map(future -> getFailure(future, 5, TimeUnit.SECONDS))
                    .filter(Objects::nonNull)
                    .toList();

            if (!failures.isEmpty()) {
                List<String> failureMessages = failures.stream()
                        .map(this::rootCauseMessage)
                        .toList();
                System.out.println("UPDATE first failure messages = " + failureMessages);
            }

            assertThat(failures)
                    .as("같은 행에 대한 UPDATE 먼저 전략은 X-Lock 대기로 직렬화되고 데드락 없이 끝나야 한다.")
                    .isEmpty();
        } finally {
            executorService.shutdownNow();
        }
    }

    private Throwable mergeInOppositeOrder(
            Long firstId,
            Long secondId,
            LocalTime firstStartAt,
            LocalTime secondStartAt,
            CountDownLatch firstMergeDone
    ) {
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            connection.setAutoCommit(false);
            setLockTimeout(connection);

            mergeReservationTime(connection, firstId, firstStartAt);
            firstMergeDone.countDown();

            if (!firstMergeDone.await(2, TimeUnit.SECONDS)) {
                throw new AssertionError("두 트랜잭션이 첫 번째 MERGE를 끝내지 못했습니다.");
            }

            mergeReservationTime(connection, secondId, secondStartAt);
            connection.commit();
            return null;
        } catch (Throwable throwable) {
            rollback(connection);
            return throwable;
        } finally {
            close(connection);
        }
    }

    private Throwable updateFirstSameRow(Long id, LocalTime startAt, CountDownLatch startGate) {
        Connection connection = null;
        try {
            if (!startGate.await(2, TimeUnit.SECONDS)) {
                throw new AssertionError("동시 시작 신호를 받지 못했습니다.");
            }

            connection = dataSource.getConnection();
            connection.setAutoCommit(false);
            setLockTimeout(connection);

            updateFirstReservationTime(connection, id, startAt);
            connection.commit();
            return null;
        } catch (Throwable throwable) {
            rollback(connection);
            return throwable;
        } finally {
            close(connection);
        }
    }

    private void setLockTimeout(Connection connection) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement("SET LOCK_TIMEOUT 1000")) {
            statement.execute();
        }
    }

    private void mergeReservationTime(Connection connection, Long id, LocalTime startAt) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(
                """
                MERGE INTO reservation_time r
                USING (
                    VALUES (?, ?)
                ) t(id, start_at)
                ON r.id = t.id
                WHEN MATCHED THEN
                    UPDATE SET
                        start_at = t.start_at
                WHEN NOT MATCHED THEN
                    INSERT (start_at)
                    VALUES (t.start_at)
                """
        )) {
            statement.setObject(1, id);
            statement.setObject(2, startAt);
            statement.executeUpdate();
        }
    }

    private void updateFirstReservationTime(Connection connection, Long id, LocalTime startAt) throws SQLException {
        int affectedRows;
        try (PreparedStatement statement = connection.prepareStatement(
                "UPDATE reservation_time SET start_at = ? WHERE id = ?"
        )) {
            statement.setObject(1, startAt);
            statement.setObject(2, id);
            affectedRows = statement.executeUpdate();
        }

        if (affectedRows != 0) {
            return;
        }

        try (PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO reservation_time (id, start_at) VALUES (?, ?)"
        )) {
            statement.setObject(1, id);
            statement.setObject(2, startAt);
            statement.executeUpdate();
        }
    }

    private Throwable getFailure(Future<Throwable> future, long timeout, TimeUnit timeUnit) {
        try {
            return future.get(timeout, timeUnit);
        } catch (Throwable throwable) {
            return throwable;
        }
    }

    private String rootCauseMessage(Throwable throwable) {
        Throwable current = throwable;
        while (current.getCause() != null) {
            current = current.getCause();
        }
        return current.getMessage();
    }

    private void rollback(Connection connection) {
        if (connection == null) {
            return;
        }

        try {
            connection.rollback();
        } catch (SQLException ignored) {
        }
    }

    private void close(Connection connection) {
        if (connection == null) {
            return;
        }

        try {
            connection.close();
        } catch (SQLException ignored) {
        }
    }
}
