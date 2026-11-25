package com.example.hua;

import com.example.hua.service.impl.AppUserServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class HuaMusicApplicationTests {

    @Autowired
    private AppUserServiceImpl appUserService;

    @Test
    public void appUserListSmokeTest()
    {
    System.out.println(appUserService.allUser());
    }
}
