package roomescape.service;

import java.util.Optional;
import org.springframework.stereotype.Service;
import roomescape.common.exception.NotFoundException;
import roomescape.common.exception.UnauthorizedException;
import roomescape.dao.CustomerDao;
import roomescape.dto.auth.LoginRequestDto;
import roomescape.dto.customer.CustomerResponseDto;
import roomescape.model.Customer;
import roomescape.common.util.JwtProvider;

@Service
public class AuthService {

    private final CustomerDao customerDao;
    private final JwtProvider jwtProvider;

    public AuthService(CustomerDao customerDao, JwtProvider jwtProvider) {
        this.customerDao = customerDao;
        this.jwtProvider = jwtProvider;
    }

    public String loginAndGenerateToken(LoginRequestDto loginRequestDto){
        Customer customer = loginRequestDto.convertToUser();
        boolean isExists = customerDao.existsByEmailAndPassword(customer);
        if (!isExists) {
            throw new UnauthorizedException("존재하지 않는 email 혹은 틀린 password 입니다.");
        }
        return jwtProvider.createToken(customer);
    }

    public CustomerResponseDto findByToken(String token) {
        Long customerId = jwtProvider.getSubjectFromToken(token);
        Optional<Customer> customer = customerDao.findById(customerId);
        if (customer.isEmpty()) {
            throw new NotFoundException("존재하지 않는 user 정보입니다.");
        }
        return CustomerResponseDto.from(customer.get());
    }
}
