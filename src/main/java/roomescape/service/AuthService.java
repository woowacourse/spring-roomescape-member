package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.common.exception.UnauthorizedException;
import roomescape.dao.CustomerDao;
import roomescape.dto.customer.LoginRequestDto;
import roomescape.model.Customer;
import roomescape.util.JwtProvider;

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
}
