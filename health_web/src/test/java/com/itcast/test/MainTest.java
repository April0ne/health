package com.itcast.test;
/*
 *@author April0ne
 *@date 2019/11/25 10:01
 */


import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class MainTest {

    @Test
    public void method01(){


        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        System.out.println(passwordEncoder.encode("admin"));
    }

}
