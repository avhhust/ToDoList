package com.softserve.itacademy.service;

import com.softserve.itacademy.exception.NullEntityReferenceException;
import com.softserve.itacademy.model.Role;
import com.softserve.itacademy.repository.RoleRepository;
import com.softserve.itacademy.service.impl.RoleServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@SpringBootTest
public class RoleServiceTests {
    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleServiceImpl roleService;

    private Role role = new Role();

    @BeforeEach
    public void init() {
        role.setName("New");
        role.setId(1L);

        Mockito.when(roleRepository.findById(1L)).thenReturn(Optional.of(role));
    }

    @Test
    public void createValidRole() {
        Mockito.when(roleRepository.save(role)).thenReturn(role);

        Assertions.assertEquals(role, roleService.create(role));
        Mockito.verify(roleRepository,Mockito.times(1)).save(role);
    }

    @Test
    public void createInvalidRole() {
        Mockito.when(roleRepository.save(role)).thenThrow(IllegalArgumentException.class);

        Assertions.assertThrows(NullEntityReferenceException.class, () -> { roleService.create(role); });
        Mockito.verify(roleRepository,Mockito.times(1)).save(role);
    }

    @Test
    public void readValidId() {
        Assertions.assertEquals(role, roleService.readById(1L));
        Mockito.verify(roleRepository,Mockito.times(1)).findById(1L);
    }

    @Test
    public void readInvalidId() {
        Mockito.when(roleRepository.findById(2L)).thenReturn(Optional.empty());

        Assertions.assertThrows(NoSuchElementException.class, () -> { roleService.readById(2L); });
        Mockito.verify(roleRepository,Mockito.times(1)).findById(2L);
    }

    @Test
    public void updateValidRole() {
        Role updatedRole = new Role();
        updatedRole.setName("New role");
        updatedRole.setId(2L);
        Mockito.when(roleRepository.findById(2L)).thenReturn(Optional.of(updatedRole));

        Mockito.when(roleRepository.save(updatedRole)).thenReturn(updatedRole);
        Assertions.assertEquals(updatedRole, roleService.update(updatedRole));
        Mockito.verify(roleRepository,Mockito.times(1)).save(updatedRole);
    }

    @Test
    public void updateInvalidRole() {
        Mockito.when(roleRepository.findById(2L)).thenReturn(Optional.empty());
        Role updatedRole = new Role();
        updatedRole.setName("New role");
        updatedRole.setId(2L);

        Assertions.assertThrows(NoSuchElementException.class, () -> { roleService.update(updatedRole); });
        Mockito.verify(roleRepository,Mockito.times(1)).findById(2L);
    }

    @Test
    public void updateNullRole() {
        Assertions.assertThrows(NullEntityReferenceException.class, () -> { roleService.update(null); });
    }

    @Test
    public void deleteValidRole() {
        Assertions.assertDoesNotThrow(() -> { roleService.delete(1L); });
        Mockito.verify(roleRepository,Mockito.times(1)).findById(1L);
    }

    @Test
    public void deleteInvalidRole() {
        Mockito.when(roleRepository.findById(2L)).thenReturn(Optional.empty());

        Assertions.assertThrows(NoSuchElementException.class, () -> { roleService.delete(2L); });
        Mockito.verify(roleRepository,Mockito.times(1)).findById(2L);
    }

    @Test
    public void getAll() {
        List<Role> roles = new ArrayList<>();
        roles.add(role);
        Mockito.when(roleRepository.findAll()).thenReturn(roles);

        Assertions.assertEquals(roles, roleService.getAll());
        Mockito.verify(roleRepository,Mockito.times(1)).findAll();
    }

    @Test
    public void getAllEmptyList() {
        Mockito.when(roleRepository.findAll()).thenReturn(new ArrayList<>());

        Assertions.assertTrue(roleService.getAll().isEmpty());
        Mockito.verify(roleRepository,Mockito.times(1)).findAll();
    }
}
