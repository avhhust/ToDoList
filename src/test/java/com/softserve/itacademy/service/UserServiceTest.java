package com.softserve.itacademy.service;


import com.softserve.itacademy.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
public class UserServiceTest {
    @Autowired
    UserService userService;

    private static User user;

    @BeforeAll
    public static void init(){

        user = new User();
        user.setFirstName("Anna");
        user.setLastName("Stoun");
        user.setPassword("777fhA99");
        user.setEmail("Anna@gmail.com");
    }

    @Test
    @Transactional
    public void getAllUsersTest() {
        List<User> users = userService.getAll();
        Assertions.assertTrue(3 == users.size());
    }

    @Test
    @Transactional
    public void createUserTest() {
        user = userService.create(user);
        Assertions.assertTrue(userService.getAll().contains(user));
    }

    @Test
    @Transactional
    public void readUserByIdTest() {
        Assertions.assertEquals("Nick",userService.readById(5).getFirstName());
    }

    @Test
    @Transactional
    public void updateUserTest() {
        User updateUser = userService.readById(4L);
        updateUser.setEmail("AnnaStoun@gmail.com");
        userService.update(updateUser);
        Assertions.assertEquals(userService.readById(4L), updateUser);
    }

    @Test
    @Transactional
    public void deleteUserTest() {
        String email = userService.readById(4L).getEmail();
        userService.delete(4L);
        Assertions.assertNull(userService.getAll().stream().filter(e->e.getEmail().equals(email)).findFirst().orElse(null));
    }
}
