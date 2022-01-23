package com.amanowicz.movieweb.service;

import com.amanowicz.movieweb.exception.UserAlreadyExistsException;
import com.amanowicz.movieweb.model.User;
import com.amanowicz.movieweb.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.crypto.password.PasswordEncoder;

import static com.amanowicz.movieweb.utils.TestUtils.TEST_USER;
import static com.amanowicz.movieweb.utils.TestUtils.getTestUser;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    public void shouldThrowExceptionWhenRegisteringUserWithExistingUsername() {
        User existingUser = getTestUser();
        when(userRepository.findByUsername(TEST_USER)).thenReturn(existingUser);

        assertThrows(UserAlreadyExistsException.class, () -> userService.register(TEST_USER, "pass"));
    }

    @Test
    public void shouldThrowExceptionWhenTryingToLogWithWrongPassword() {
        User existingUser = getTestUser();
        when(userRepository.findByUsername(TEST_USER)).thenReturn(existingUser);
        when(passwordEncoder.matches("123", existingUser.getPassword())).thenReturn(false);

        assertThrows(AuthenticationServiceException.class, () -> userService.login(TEST_USER, "123"));
    }

}