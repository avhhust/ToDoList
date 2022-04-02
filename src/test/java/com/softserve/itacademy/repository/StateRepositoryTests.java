package com.softserve.itacademy.repository;

import com.softserve.itacademy.model.State;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class StateRepositoryTests {

    @Autowired
    private StateRepository stateRepository;

    @Autowired
    private TestEntityManager manager;

    @Test
    public void getByValidName() {
        State state = new State();
        state.setName("Some_state");
        manager.persist(state);

        State getByName = stateRepository.getByName("Some_state");
        Assertions.assertNotNull(getByName);
        Assertions.assertEquals(state.getName(),getByName.getName());
    }

    @Test
    public void getByInvalidName() {
        Assertions.assertNull(stateRepository.getByName("Wrong_name"));
    }

    @Test
    public void getAll() {
        List<State> states = stateRepository.getAll();

        Assertions.assertNotNull(states);
        Assertions.assertTrue(!states.isEmpty());
    }
}
