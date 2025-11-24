package com.example.CloudStorageDiploma;

import com.example.CloudStorageDiploma.components.JwtUtil;
import com.example.CloudStorageDiploma.dto.LoginRequest;
import com.example.CloudStorageDiploma.dto.LoginResponse;
import com.example.CloudStorageDiploma.entities.User;
import com.example.CloudStorageDiploma.repositories.UserRepository;
import com.example.CloudStorageDiploma.services.HelperService;
import com.example.CloudStorageDiploma.services.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private UserService userService;

    private static final String LOGIN = "john_doe";
    private static final String PASSWORD = "secret123";
    private static final String HASHED_PASSWORD = "$2a$12$abcdefghijklmnopqrstuvwxYz0123456789"; // valid BCrypt hash
    private static final Long USER_ID = 42L;
    private static final String JWT_TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.xxxxx";

    @Test
    void getAuthorities_UserNotFound_ReturnsNull() {
        LoginRequest request = new LoginRequest();
        request.setLogin(LOGIN);
        request.setPassword(PASSWORD);

        when(userRepository.findByLoginAndPassword(LOGIN)).thenReturn(null);
        LoginResponse response = userService.getAuthorities(request);
        assertNull(response);
        verify(userRepository).findByLoginAndPassword(LOGIN);
        verifyNoInteractions(jwtUtil);
    }

    @Test
    void getAuthorities_WrongPassword_ReturnsNull() {
        User user = new User();
        user.setId(USER_ID);
        user.setLogin(LOGIN);
        user.setPassword(HASHED_PASSWORD);

        LoginRequest request = new LoginRequest();
        request.setLogin(LOGIN);
        request.setPassword("wrong-password");

        when(userRepository.findByLoginAndPassword(LOGIN)).thenReturn(user);

        try (MockedStatic<HelperService> helperMock = mockStatic(HelperService.class)) {
            helperMock.when(() -> HelperService.checkPassword("wrong-password", HASHED_PASSWORD))
                    .thenReturn(false);
            LoginResponse response = userService.getAuthorities(request);
            assertNull(response);
            verify(userRepository).findByLoginAndPassword(LOGIN);
            verifyNoInteractions(jwtUtil);
        }
    }

    @Test
    void getAuthorities_CorrectCredentials_GeneratesTokenAndReturnsResponse() {
        User user = new User();
        user.setId(USER_ID);
        user.setLogin(LOGIN);
        user.setPassword(HASHED_PASSWORD);

        LoginRequest request = new LoginRequest();
        request.setLogin(LOGIN);
        request.setPassword(PASSWORD);

        when(userRepository.findByLoginAndPassword(LOGIN)).thenReturn(user);
        when(jwtUtil.generateToken(USER_ID, LOGIN)).thenReturn(JWT_TOKEN);

        try (MockedStatic<HelperService> helperMock = mockStatic(HelperService.class)) {
            helperMock.when(() -> HelperService.checkPassword(PASSWORD, HASHED_PASSWORD))
                    .thenReturn(true);

            LoginResponse response = userService.getAuthorities(request);

            assertNotNull(response);
            assertEquals(JWT_TOKEN, response.getAuthToken());

            verify(userRepository).findByLoginAndPassword(LOGIN);
            verify(jwtUtil).generateToken(USER_ID, LOGIN);
        }
    }

    @Test
    void getAuthorities_TrimLoginIsUsedInRepositoryCall() {
        LoginRequest request = new LoginRequest();
        request.setLogin("   " + LOGIN + "  ");
        request.setPassword(PASSWORD);
        when(userRepository.findByLoginAndPassword(LOGIN)).thenReturn(null);
        userService.getAuthorities(request);
        verify(userRepository).findByLoginAndPassword(LOGIN);
    }
}
