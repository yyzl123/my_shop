package com.qf.my_shop_web_user;

import com.qf.api.user.IUserService;
import com.qf.entity.TUser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootTest
class MyShopWebUserApplicationTests {
    @Autowired
    private BCryptPasswordEncoder encoder;
    private IUserService userService;
    @Test
    void contextLoads() {
    }


    @Test
    public void test(){
        ///String encode = encoder.encode("123456");
       // String encode = encoder.upgradeEncoding("$2a$10$K.7kXCMsyaP4pNQkseKgAe7MGjsfZ1.4zdY11Uy02/s/N3Crv6rvO");
        //System.out.println(encode);
        //boolean matches = encoder.matches("888888", "$2a$10$K.7kXCMsyaP4pNQkseKgAe7MGjsfZ1.4zdY11Uy02/s/N3Crv6rvO");
        //System.out.println(matches);

        TUser user1 = userService.selectByUname("rr");
        System.out.println(user1);
    }
}
