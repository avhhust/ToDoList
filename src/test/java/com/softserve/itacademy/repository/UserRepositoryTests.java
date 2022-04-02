package com.softserve.itacademy.repository;
import com.softserve.itacademy.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.transaction.annotation.Transactional;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
public class UserRepositoryTests {
    @Autowired
    UserRepository userRepository;
    @Autowired
    TestEntityManager entityManager;

    @Test
    public void createUser() {
        User user = new User();
        user.setLastName("Eva");
        user.setFirstName("Braun");
        user.setEmail("ee@in.ua");
        user.setPassword("hhhk12Sa");
        user = userRepository.save(user);
        User userNow = userRepository.getUserByEmail("ee@in.ua");
        Assertions.assertEquals(user.getFirstName(), userNow.getFirstName());
    }

    @Test
    public void readUser() {
        Assertions.assertEquals(userRepository.getOne(4L).getEmail(), "mike@mail.com");
    }

    @Transactional
    @Test
    public void updateUser() {
        User user = userRepository.getOne(4L);
        user.setLastName("Danov");
        user = userRepository.save(user);
        Assertions.assertEquals(user.getLastName(), userRepository.getOne(4L).getLastName());
    }
    @Test
    public void deleteUser() {
        long countThen = userRepository.count();
        userRepository.delete(userRepository.getOne(4L));
        Assertions.assertEquals(userRepository.count(), countThen-1);
    }
    @Test
    public void getUserByEmail() {
        User user = new User();
        user.setLastName("Adam");
        user.setFirstName("Greqy");
        user.setEmail("wow@in.ua");
        user.setPassword("hhhk12Sa");
        user = userRepository.save(user);
        User userNow = userRepository.getUserByEmail("wow@in.ua");
        Assertions.assertEquals(user.getId(), userNow.getId());
    }

}
