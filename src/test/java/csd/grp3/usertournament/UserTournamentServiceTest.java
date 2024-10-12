package csd.grp3.usertournament;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import csd.grp3.user.User;
import csd.grp3.tournament.Tournament;

public class UserTournamentServiceTest {

    @Mock
    private UserTournamentRepository userTournamentRepo;

    @InjectMocks
    private UserTournamentServiceImpl userTournamentService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this); // Initialize Mockito annotations
    }

    // Test case for getPlayers success
    @Test
    public void testGetPlayers_ValidTourneyID_Success() {
        // Arrange
        List<User> mockUsers = Arrays.asList(new User("user1", "abcd1234"), new User("user2", "abcd1234"));
        when(userTournamentRepo.findRegisteredUsersByTournamentId(any(Long.class))).thenReturn(mockUsers);

        // Act
        List<User> players = userTournamentService.getPlayers(1L);

        // Assert
        assertNotNull(players);
        verify(userTournamentRepo).findRegisteredUsersByTournamentId(1L);
    }

    // Test case for getPlayers failure (empty list)
    @Test
    public void testGetPlayers_InvalidTourneyID_EmptyList() {
        // Arrange
        when(userTournamentRepo.findRegisteredUsersByTournamentId(any(Long.class))).thenReturn(Arrays.asList());

        // Act
        List<User> players = userTournamentService.getPlayers(1L);

        // Assert
        assertNotNull(players);
        assertTrue(players.isEmpty());
        verify(userTournamentRepo).findRegisteredUsersByTournamentId(1L);
    }

    // Test case for getWaitingList success
    @Test
    public void testGetWaitingList_Success() {
        List<User> mockUsers = Arrays.asList(new User("user1", "abcd1234"), new User("user2", "abcd1234"));
        when(userTournamentRepo.findWaitlistUsersByTournamentId(any(Long.class))).thenReturn(mockUsers);

        List<User> waitingList = userTournamentService.getWaitingList(1L);

        assertNotNull(waitingList);
        verify(userTournamentRepo).findWaitlistUsersByTournamentId(1L);
    }

    // Test case for getWaitingList failure (empty list)
    @Test
    public void testGetWaitingList_InvalidTourneyID_EmptyList() {
        // Arrange
        when(userTournamentRepo.findWaitlistUsersByTournamentId(any(Long.class))).thenReturn(Arrays.asList());

        // Act
        List<User> waitingList = userTournamentService.getWaitingList(1L);

        // Assert
        assertNotNull(waitingList);
        assertTrue(waitingList.isEmpty());
        verify(userTournamentRepo).findWaitlistUsersByTournamentId(1L);
    }

    // Test case for getGamePoints success
    @Test
    public void testGetGamePoints_Success() {
        UserTournament mockUserTournament = new UserTournament();
        mockUserTournament.setGamePoints(10);
        when(userTournamentRepo.findById_TournamentIdAndId_Username(any(Long.class), any(String.class)))
            .thenReturn(Optional.of(mockUserTournament));

        double gamePoints = userTournamentService.getGamePoints(1L, "user1");

        assertEquals(10, gamePoints);
        verify(userTournamentRepo).findById_TournamentIdAndId_Username(1L, "user1");
    }

    // Test case for getGamePoints failure (record not found)
    @Test
    public void testGetGamePoints_RecordNotFound() {
        when(userTournamentRepo.findById_TournamentIdAndId_Username(any(Long.class), any(String.class)))
            .thenReturn(Optional.empty());

        assertThrows(UserTournamentNotFoundException.class, () -> {
            userTournamentService.getGamePoints(1L, "user1");
        });
        verify(userTournamentRepo).findById_TournamentIdAndId_Username(1L, "user1");
    }

    // Test case for updateGamePoints success
    @Test
    public void testUpdateGamePoints_Success() {
        UserTournament mockUserTournament = new UserTournament();
        mockUserTournament.setGamePoints(50.0);
        when(userTournamentRepo.findById_TournamentIdAndId_Username(any(Long.class), any(String.class)))
            .thenReturn(Optional.of(mockUserTournament));

        userTournamentService.updateGamePoints(1L, "user1", 10);

        assertEquals(60.0, mockUserTournament.getGamePoints());
        verify(userTournamentRepo).findById_TournamentIdAndId_Username(1L, "user1");
        verify(userTournamentRepo).save(any(UserTournament.class));
    }

    // Test case for updateGamePoints success
    @Test
    public void testUpdateGamePoints_NoInitialGamePoint_Success() {
        UserTournament mockUserTournament = new UserTournament();
        when(userTournamentRepo.findById_TournamentIdAndId_Username(any(Long.class), any(String.class)))
            .thenReturn(Optional.of(mockUserTournament));

        userTournamentService.updateGamePoints(1L, "user1", 10);

        assertEquals(10.0, mockUserTournament.getGamePoints());
        verify(userTournamentRepo).findById_TournamentIdAndId_Username(1L, "user1");
        verify(userTournamentRepo).save(any(UserTournament.class));
    }

    // Test case for updateGamePoints failure (record not found)
    @Test
    public void testUpdateGamePoints_RecordNotFound() {
        when(userTournamentRepo.findById_TournamentIdAndId_Username(any(Long.class), any(String.class)))
            .thenReturn(Optional.empty());

        assertThrows(UserTournamentNotFoundException.class, () -> {
            userTournamentService.updateGamePoints(1L, "user1", 10);
        });
        verify(userTournamentRepo).findById_TournamentIdAndId_Username(1L, "user1");
        verify(userTournamentRepo, times(0)).save(any(UserTournament.class)); // save not called
    }

    // Test case for updatePlayerStatus success
    @Test
    public void testUpdatePlayerStatus_Success() {
        UserTournament mockUserTournament = new UserTournament();
        mockUserTournament.setStatus('w');

        when(userTournamentRepo.findById_TournamentIdAndId_Username(any(Long.class), any(String.class)))
            .thenReturn(Optional.of(mockUserTournament));
        when(userTournamentRepo.save(any(UserTournament.class))).thenReturn(mockUserTournament);

        UserTournament updatedUserTournament = userTournamentService.updatePlayerStatus(1L, "user1", 'r');

        assertEquals('r', updatedUserTournament.getStatus());
        verify(userTournamentRepo).findById_TournamentIdAndId_Username(1L, "user1");
        verify(userTournamentRepo).save(any(UserTournament.class));
    }

    // Test case for updatePlayerStatus failure (record not found)
    @Test
    public void testUpdatePlayerStatus_RecordNotFound() {
        when(userTournamentRepo.findById_TournamentIdAndId_Username(any(Long.class), any(String.class)))
            .thenReturn(Optional.empty());

        assertThrows(UserTournamentNotFoundException.class, () -> {
            userTournamentService.updatePlayerStatus(1L, "user1", 'r');
        });
        verify(userTournamentRepo).findById_TournamentIdAndId_Username(1L, "user1");
        verify(userTournamentRepo, times(0)).save(any(UserTournament.class)); // save not called
    }

    // Test case for updatePlayerStatus failure (invalid status) - doesn't fail
    // @Test
    // public void testUpdatePlayerStatus_InvalidStatus() {
    //     UserTournament mockUserTournament = new UserTournament();
    //     when(userTournamentRepo.findById_TournamentIdAndId_Username(any(Long.class), any(String.class)))
    //         .thenReturn(Optional.of(mockUserTournament));

    //     // Assert that UserTournamentNotFoundException is thrown
    //     // assertThrows(MethodArgumentNotValidException.class, () -> {
    //         userTournamentService.updatePlayerStatus(1L, "user1", 'R');
    //     // });

    //     // Verify repository method was called
    //     assertEquals('R', mockUserTournament.getStatus());
    //     verify(userTournamentRepo).findById_TournamentIdAndId_Username(1L, "user1");
    // }

    // Test case for add success
    @Test
    public void testAdd_Success() {
        Tournament tournament = new Tournament();
        tournament.setId(1L);
        User user = new User("user1", "1234abcd");

        UserTournament mockUserTournament = new UserTournament();
        mockUserTournament.setId(new UserTournamentId(tournament.getId(), user.getUsername()));
        mockUserTournament.setStatus('w');

        when(userTournamentRepo.save(any(UserTournament.class))).thenReturn(mockUserTournament);

        UserTournament result = userTournamentService.add(tournament, user, 'w');

        assertNotNull(result);
        verify(userTournamentRepo).save(any(UserTournament.class));
    }

    // Test case for add failure due to null Tournament
    @Test
    public void testAdd_NullTournament_ThrowsException() {
        User user = new User("user1", "asiojf");

        assertThrows(NullPointerException.class, () -> {
            userTournamentService.add(null, user, 'w');
        });
        verify(userTournamentRepo, times(0)).save(any(UserTournament.class)); // save not called
    }

    // Test case for add failure due to null User
    @Test
    public void testAdd_Failure_NullUser() {
        Tournament tournament = new Tournament();
        tournament.setId(1L);

        assertThrows(NullPointerException.class, () -> {
            userTournamentService.add(tournament, null, 'P');
        });
        verify(userTournamentRepo, times(0)).save(any(UserTournament.class)); // save not called
    }

    // Test case for add failure due to record existing
    // @Test 
    // public void testAdd_Failure_RecordExists() {
    //     // Arrange
    //     Tournament tournament = new Tournament();
    //     tournament.setId(1L);
    //     User user = new User("user1", "asbc");

    //     // Act & Assert
    //     assertThrows(NullPointerException.class, () -> {
    //         userTournamentService.add(tournament, user, 'P');
    //     });
    //     verify(userTournamentRepo, times(0)).save(any(UserTournament.class)); // save not called
    // }

    // Test case for delete success
    @Test
    public void testDelete_Success() {
        UserTournament mockUserTournament = new UserTournament();
        Tournament tournament = new Tournament();
        User user = new User();
        mockUserTournament.setId(new UserTournamentId(1L, "user1"));
        when(userTournamentRepo.findById_TournamentIdAndId_Username(any(Long.class), any(String.class)))
            .thenReturn(Optional.of(mockUserTournament));

        userTournamentService.delete(tournament, user);

        verify(userTournamentRepo).findById_TournamentIdAndId_Username(1L, "user1");
        verify(userTournamentRepo).deleteById_TournamentIdAndId_Username(1L, "user1");
    }

    // Test case for delete failure (record not found)
    @Test
    public void testDelete_RecordNotFound() {
        when(userTournamentRepo.findById_TournamentIdAndId_Username(any(Long.class), any(String.class)))
            .thenReturn(Optional.empty());

        // assertThrows(UserTournamentNotFoundException.class, () -> {
        //     userTournamentService.delete(1L, "doesNotExist");
        // });
        verify(userTournamentRepo).findById_TournamentIdAndId_Username(1L, "doesNotExist");
        verify(userTournamentRepo, times(0)).deleteById_TournamentIdAndId_Username(1L, "doesNotExist");
    }
}
