package com.softserve.itacademy.service;

import com.softserve.itacademy.exception.NullEntityReferenceException;
import com.softserve.itacademy.model.Priority;
import com.softserve.itacademy.model.State;
import com.softserve.itacademy.model.Task;
import com.softserve.itacademy.repository.StateRepository;
import com.softserve.itacademy.service.impl.StateServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SpringBootTest
public class StateServiceTests {
    @Mock
    private StateRepository stateRepository;

    @InjectMocks
    private StateServiceImpl stateService;

    private State state = new State();

    @BeforeEach
    public void init() {
        state.setName("New");
        state.setId(1L);

        Mockito.when(stateRepository.findById(1L)).thenReturn(Optional.of(state));
    }

    @Test
    public void createValidState() {
        Mockito.when(stateRepository.save(state)).thenReturn(state);

        Assertions.assertEquals(state,stateService.create(state));
        Mockito.verify(stateRepository,Mockito.times(1)).save(state);
    }

    @Test
    public void createInvalidState() {
        Mockito.when(stateRepository.save(state)).thenThrow(IllegalArgumentException.class);

        Assertions.assertThrows(NullEntityReferenceException.class, () -> { stateService.create(state); });
        Mockito.verify(stateRepository,Mockito.times(1)).save(state);
    }

    @Test
    public void readValidId() {
        Assertions.assertEquals(state,stateService.readById(1L));
        Mockito.verify(stateRepository,Mockito.times(1)).findById(1L);
    }

    @Test
    public void readInvalidId() {
        Mockito.when(stateRepository.findById(2L)).thenReturn(Optional.empty());

        Assertions.assertThrows(EntityNotFoundException.class, () -> { stateService.readById(2L); });
        Mockito.verify(stateRepository,Mockito.times(1)).findById(2L);
    }

    @Test
    public void updateValidState() {
         State updatedState = new State();
         updatedState.setName("Done");
         updatedState.setId(2L);
         Mockito.when(stateRepository.findById(2L)).thenReturn(Optional.of(updatedState));

        Mockito.when(stateRepository.save(updatedState)).thenReturn(updatedState);
        Assertions.assertEquals(updatedState,stateService.update(updatedState));
        Mockito.verify(stateRepository,Mockito.times(1)).save(updatedState);
    }

    @Test
    public void updateInvalidState() {
        Mockito.when(stateRepository.findById(2L)).thenReturn(Optional.empty());
        State updatedState = new State();
        updatedState.setName("Done");
        updatedState.setId(2L);

        Assertions.assertThrows(EntityNotFoundException.class, () -> { stateService.update(updatedState); });
        Mockito.verify(stateRepository,Mockito.times(1)).findById(2L);
    }

    @Test
    public void updateNullState() {
        Assertions.assertThrows(NullEntityReferenceException.class, () -> { stateService.update(null); });
    }

    @Test
    public void deleteValidState() {
        Assertions.assertDoesNotThrow(() -> { stateService.delete(1L); });
        Mockito.verify(stateRepository,Mockito.times(1)).findById(1L);
    }

    @Test
    public void deleteInvalidState() {
        Mockito.when(stateRepository.findById(2L)).thenReturn(Optional.empty());

        Assertions.assertThrows(EntityNotFoundException.class, () -> { stateService.delete(2L); });
        Mockito.verify(stateRepository,Mockito.times(1)).findById(2L);
    }

    @Test
    public void getByValidName() {
        Mockito.when(stateRepository.getByName("New")).thenReturn(state);

        Assertions.assertEquals(state,stateService.getByName("New"));
        Mockito.verify(stateRepository,Mockito.times(1)).getByName("New");
    }

    @Test
    public void getByInvalidName() {
        Mockito.when(stateRepository.getByName("To Do")).thenReturn(null);

        Assertions.assertThrows(EntityNotFoundException.class, () -> { stateService.getByName("To Do"); });
        Mockito.verify(stateRepository,Mockito.times(1)).getByName("To Do");
    }

    @Test
    public void getAll() {
        List<State> states = new ArrayList<>();
        states.add(state);
        Mockito.when(stateRepository.getAll()).thenReturn(states);

        Assertions.assertEquals(states,stateService.getAll());
        Mockito.verify(stateRepository,Mockito.times(1)).getAll();
    }

    @Test
    public void getAllEmptyList() {
        Mockito.when(stateRepository.getAll()).thenReturn(new ArrayList<>());

        Assertions.assertTrue(stateService.getAll().isEmpty());
        Mockito.verify(stateRepository,Mockito.times(1)).getAll();
    }
}
