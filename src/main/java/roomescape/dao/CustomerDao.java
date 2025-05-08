package roomescape.dao;

import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import roomescape.model.Customer;

@Repository
public class CustomerDao {

    private final RowMapper<Customer> customerRowMapper = (resultSet, rowNum) -> {
        return new Customer(
                resultSet.getLong("id"),
                resultSet.getString("email"),
                resultSet.getString("name"),
                resultSet.getString("password"));
    };

    private final JdbcTemplate jdbcTemplate;

    public CustomerDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public boolean existsByEmailAndPassword(Customer customer){
        String sql = "SELECT EXISTS(SELECT 1 FROM customer WHERE email = ? AND password = ?)";
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(sql, Boolean.class, customer.getEmail(), customer.getPassword()));
    }

    public Optional<Customer> findById(Long customerId) {
        String sql = "SELECT * FROM customer WHERE id = ?";
        return jdbcTemplate.query(sql, customerRowMapper, customerId)
                .stream().
                findFirst();
    }
}
