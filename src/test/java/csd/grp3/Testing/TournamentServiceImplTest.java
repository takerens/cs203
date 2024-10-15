// package csd.grp3.Testing;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyChar;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import csd.grp3.tournament.InvalidTournamentStatus;
import csd.grp3.tournament.Tournament;
import csd.grp3.tournament.TournamentNotFoundException;
import csd.grp3.tournament.TournamentRepository;
import csd.grp3.tournament.TournamentServiceImpl;
import csd.grp3.user.User;
import csd.grp3.user.UserServiceImpl;
import csd.grp3.match.Match;
import csd.grp3.match.MatchRepository;
import csd.grp3.match.MatchServiceImpl;
import csd.grp3.round.Round;
import csd.grp3.round.RoundServiceImpl;
import csd.grp3.tournament.PlayerAlreadyRegisteredException;
import csd.grp3.usertournament.UserTournamentServiceImpl;
import csd.grp3.usertournament.UserTournament;
import csd.grp3.usertournament.UserTournamentId;
import csd.grp3.usertournament.UserTournamentRepository;
import csd.grp3.usertournament.UserTournamentService;

// @ExtendWith(MockitoExtension.class)
// public class TournamentServiceImplTest {

    @Mock
    private TournamentRepository tournamentRepository;

    @Mock
    private UserTournamentRepository utRepository;

    @Mock
    private MatchRepository matchRepository;

    @InjectMocks
    private TournamentServiceImpl tournamentService;

    @Mock
    private RoundServiceImpl roundService;

    @Mock
    private UserTournamentServiceImpl userTournamentService;

    @Mock
    private UserServiceImpl userService;

    @Mock
    private MatchServiceImpl matchService;

//     private Tournament tournament;
//     private User player;

//     @BeforeEach
//     void setUp() {
//         MockitoAnnotations.openMocks(this);
//         tournament = new Tournament();
//         tournament.setId(1L);
//         tournament.setTitle("Test Tournament");
//         tournament.setSize(2);
//         // tournament.setUserTournaments(new ArrayList<>());
        
//         player = new User("testUser", "testPassword123");  // Username and password
//         player.setAuthorities("ROLE_USER"); // Set specific authorities
//     }

//     @Test
//     void listTournaments_NoTournaments_ReturnEmptyList() {
//         List<Tournament> tournamentsList = new ArrayList<>();

//         // mock the getAllTournaments()
//         when(tournamentRepository.findAll()).thenReturn(tournamentsList);

//         List<Tournament> result = tournamentService.listTournaments();

//         assertNotNull(result);
//         assertEquals(0, result.size());
//         verify(tournamentRepository).findAll();
//     }

//     @Test
//     void listTournaments_HasTournaments_ReturnListOfTournaments() {
//         // Arrange
//         List<Tournament> tournamentsList = new ArrayList<>();
//         tournamentsList.add(tournament);
        
//         // mock getAllTournaments()
//         when(tournamentRepository.findAll()).thenReturn(tournamentsList);

//         List<Tournament> result = tournamentService.listTournaments();

//         assertNotNull(result);
//         assertEquals(1, result.size());
//         verify(tournamentRepository).findAll();
//         // assertEquals("Test Tournament", result.get(0).getTitle());
//         // verify(tournamentRepository, times(1)).findAll();
//     }

//     @Test
//     void getTournament_NoTournament_ReturnTournamentNotFoundException() {
//         // Mock findById to return an empty Optional
//         when(tournamentRepository.findById(any(Long.class))).thenReturn(Optional.empty());

//         // Act & Assert: Expect TournamentNotFoundException to be thrown
//         TournamentNotFoundException exception = assertThrows(TournamentNotFoundException.class, () -> {
//             tournamentService.getTournament(1L);
//         });

//         // Verify that the exception message is correct
//         assertEquals("Could not find tournament 1", exception.getMessage());

//         // Verify that findById was called with the correct argument
//         verify(tournamentRepository).findById(1L);
//     }

//     @Test
//     void getTournament_TournamentFound_ReturnTournament() {
//         // Mock findById to return the tournament
//         when(tournamentRepository.findById(any(Long.class))).thenReturn(Optional.of(tournament));

//         // Act
//         Tournament foundTournament = tournamentService.getTournament(1L);

//         // Assert
//         assertNotNull(foundTournament);
//         assertEquals(1L, foundTournament.getId());
//         assertEquals("Test Tournament", foundTournament.getTitle());
//         verify(tournamentRepository).findById(1L);
//     }

//     // @Test
//     // void addTournament_NewTitle_ReturnSavedTournament() {
//     //     when(tournamentRepository.save(any(Tournament.class))).thenReturn(tournament);



//     //     assertEquals(tournament, result);
//     //     verify(tournamentRepository).save(tournament);
//     // }

//     @Test
//     void addTournament_SameTitle_ReturnSavedTournamentWithDifferentID() {
//         // Arrange
//         Tournament tournament2 = tournament;
//         tournament2.setId(2L);
//         when(tournamentRepository.save(any(Tournament.class))).thenReturn(tournament);

//         // Act
//         tournamentService.addTournament(tournament);
//         Tournament result = tournamentService.addTournament(tournament2);

//         // Assert
//         assertEquals(2L, result.getId());
//         assertEquals("Test Tournament", result.getTitle());
//         verify(tournamentRepository, times(2)).save(tournament);
//     }

//     @Test
//     void updateTournament_NotFound_ReturnTournamentNotFoundException() {
//         // Arrange
//         when(tournamentRepository.findById(1L)).thenReturn(Optional.empty());
        
//         // Act
//         TournamentNotFoundException exception = assertThrows(TournamentNotFoundException.class, () -> {
//             tournamentService.updateTournament(1L, tournament);
//         });

//         // Assert
//         assertEquals("Could not find tournament 1", exception.getMessage());
//         verify(tournamentRepository).findById(1L);
//     }

    @Test
    void updateTournament_UpdatedTournament_ReturnUpdatedTournament() {
        // Arrange
        Tournament newTournamentInfo = new Tournament(1L, null, "Updated Tournament", 0, 0, null, 0, 10, false, null);
        when(tournamentRepository.findById(1L)).thenReturn(Optional.of(tournament));

//         // Act
//         Tournament updatedTournament = tournamentService.updateTournament(1L, newTournamentInfo);

        // Assert
        assertNotNull(updatedTournament);
        assertEquals("Updated Tournament", updatedTournament.getTitle());
        verify(tournamentRepository).findById(1L);
    }

//     @Test
//     void deleteTournament_DeleteSuccess_ReturnDeletedTournament() {
//         // mock getById method to return a Tournament
//         when(tournamentRepository.findById(tournament.getId())).thenReturn(Optional.of(tournament));

//         // mock the deleteById method to do nothing (since its a void method)
//         doNothing().when(tournamentRepository).deleteById(tournament.getId());

//         // act & assert
//         assertDoesNotThrow(() -> tournamentService.deleteTournament(tournament.getId()));

//         // verify delete called once with correct tournament ID
//         verify(tournamentRepository, times(1)).deleteById(1L);
//     }

//     @Test
//     void deleteTournament_TournamentNotFound_ReturnTournamentNotFoundException() {
//         // mock deleteById method to do nothing (since its a void method)
//         when(tournamentRepository.findById(tournament.getId())).thenReturn(Optional.empty());

//         // Act & Assert: Expect TournamentNotFoundException to be thrown
//         TournamentNotFoundException exception = assertThrows(TournamentNotFoundException.class, () -> {
//             tournamentService.deleteTournament(tournament.getId());
//         });

//         // Verify that the exception message is correct
//         assertEquals("Could not find tournament 1", exception.getMessage());

//         // Verify that deleteById was never called with the correct argument
//         verify(tournamentRepository, never()).deleteById(1L);
//     }

    @Test
    void registerPlayer_RegisterToUserListSuccess_ReturnUserTournamentListSizeMoreByOne() {
        // Arrange
        List<User> userList = new ArrayList<>();
        List<User> waitingList = new ArrayList<>();
        UserTournamentId UTId = new UserTournamentId(tournament.getId(), player.getUsername());
        UserTournament userTournament = new UserTournament(UTId, tournament, player, null, 0, 0);
        tournament.setSize(10);

        // retrieve mock tournament
        when(tournamentRepository.findById(tournament.getId())).thenReturn(Optional.of(tournament));
        when(userTournamentService.getPlayers(tournament.getId())).thenReturn(userList);
        when(userTournamentService.getWaitingList(tournament.getId())).thenReturn(waitingList);
        when(userService.findByUsername(player.getUsername())).thenReturn(player);

        doAnswer(invocation -> {
            tournament.getUserTournaments().add(userTournament); // Directly add the user tournament to the tournament's list
            return null; // Since add returns void
        }).when(userTournamentService).add(any(Tournament.class), any(User.class), anyChar());
        // mock UTService
        // when(userTournamentRepository.findRegisteredUsersByTournamentId(1L)).thenReturn(userList);
        // when(userTournamentRepository.findWaitlistUsersByTournamentId(1L)).thenReturn(userList);

        // act
        tournamentService.registerUser(player, 1L);

        // assert
        assertEquals(1, tournament.getUserTournaments().size());
        assertEquals(userTournament, tournament.getUserTournaments().get(0));
        verify(userTournamentService, times(1)).add(tournament, player, 'r');
        verify(userTournamentService).getPlayers(tournament.getId());
        verify(userTournamentService).getWaitingList(tournament.getId());
    }

    @Test
    void registerPlayer_PlayerAlreadyRegisteredUserList_ReturnPlayerAlreadyRegisteredException() {
        // Arrange
        List<User> userList = new ArrayList<>();
        userList.add(player);

        // Retrieve empty mock tournament
        when(tournamentRepository.findById(tournament.getId())).thenReturn(Optional.of(tournament));
        when(userTournamentService.getPlayers(tournament.getId())).thenReturn(userList);
        when(userService.findByUsername(player.getUsername())).thenReturn(player);

        // Act & Assert: Expect TournamentNotFoundException to be thrown
        PlayerAlreadyRegisteredException exception = assertThrows(PlayerAlreadyRegisteredException.class, () -> {
            tournamentService.registerUser(player, tournament.getId());
        });

        // Verify that the exception message is correct
        assertEquals("Player has already registered for this tournament.", exception.getMessage());

        // Verify that deleteById was never called with the correct argument
        verify(tournamentRepository).findById(tournament.getId());
        verify(userTournamentService).getWaitingList(tournament.getId());
    }

    @Test
    void registerPlayer_PlayerAlreadyRegisteredWaitingListList_ReturnPlayerAlreadyRegisteredException() {
        // Arrange
        List<User> waitingList = new ArrayList<>();
        waitingList.add(player);

        // Retrieve empty mock tournament
        when(tournamentRepository.findById(tournament.getId())).thenReturn(Optional.of(tournament));
        when(userTournamentService.getWaitingList(tournament.getId())).thenReturn(waitingList);
        when(userService.findByUsername(player.getUsername())).thenReturn(player);

        // Act & Assert: Expect TournamentNotFoundException to be thrown
        PlayerAlreadyRegisteredException exception = assertThrows(PlayerAlreadyRegisteredException.class, () -> {
            tournamentService.registerUser(player, tournament.getId());
        });

        // Verify that the exception message is correct
        assertEquals("Player has already registered for this tournament.", exception.getMessage());

        // Verify that deleteById was never called with the correct argument
        verify(tournamentRepository).findById(tournament.getId());
        verify(userTournamentService).getWaitingList(tournament.getId());
    }

//     // @Test
//     // void registerPlayer_NoTournamentFound_ReturnTournamentNotFoundException() {
//     //     // Retrieve empty mock tournament
//     //     when(tournamentRepository.findById(tournament.getId())).thenReturn(Optional.empty());

//     //     // Act & Assert: Expect TournamentNotFoundException to be thrown
//     //     TournamentNotFoundException exception = assertThrows(TournamentNotFoundException.class, () -> {
//     //         tournamentService.registerUser(player, tournament.getId());
//     //     });

//     //     // Verify that the exception message is correct
//     //     assertEquals("Could not find tournament 1", exception.getMessage());

//     //     // Verify that deleteById was never called with the correct argument
//     //     verify(tournamentRepository).findById(1L);
//     // }

    @Test
    void withdrawPlayer_UserListWithdrawSuccess_ReturnUserListSmallerByOne() {
        // Arrange
        List<User> userList = new ArrayList<>();
        List<User> waitingList = new ArrayList<>();
        UserTournamentId UTId = new UserTournamentId(tournament.getId(), player.getUsername());
        UserTournament userTournament = new UserTournament(UTId, tournament, player, null, 0, 0);
        LocalDateTime time = LocalDateTime.of(2014, Month.JANUARY, 1, 10, 10, 30);
        userList.add(player);
        tournament.setDate(time);
        tournament.setSize(10);

        // retrieve mock tournament
        when(tournamentRepository.findById(tournament.getId())).thenReturn(Optional.of(tournament));
        when(userTournamentService.getPlayers(tournament.getId())).thenReturn(userList);
        when(userTournamentService.getWaitingList(tournament.getId())).thenReturn(waitingList);
        when(userService.findByUsername(player.getUsername())).thenReturn(player);

        doAnswer(invocation -> {
            userList.remove(player); // Directly add the user tournament to the tournament's list
            return null; // Since add returns void
        }).when(userTournamentService).delete(tournament, player);

        // mock UTService
        // when(userTournamentRepository.findRegisteredUsersByTournamentId(1L)).thenReturn(userList);
        // when(userTournamentRepository.findWaitlistUsersByTournamentId(1L)).thenReturn(userList);

        // act
        tournamentService.withdrawUser(player, tournament.getId());

        // assert
        assertEquals(0, userList.size());
        verify(userTournamentService, times(1)).getPlayers(tournament.getId());
        verify(userTournamentService, times(1)).getWaitingList(tournament.getId());
        verify(tournamentRepository, times(1)).findById(tournament.getId());
        verify(userService).findByUsername(player.getUsername());
    }

//     // @Test
//     // void withdrawPlayer_TournamentNotFound_ReturnTournamentNotFoundException() {
//     //     // Retrieve empty mock tournament
//     //     when(tournamentRepository.findById(tournament.getId())).thenReturn(Optional.empty());

//     //     // Act & Assert: Expect TournamentNotFoundException to be thrown
//     //     TournamentNotFoundException exception = assertThrows(TournamentNotFoundException.class, () -> {
//     //         tournamentService.withdrawUser(player, tournament.getId());
//     //     });

//     //     // Verify that the exception message is correct
//     //     assertEquals("Could not find tournament 1", exception.getMessage());

//     //     // Verify that deleteById was never called with the correct argument
//     //     verify(tournamentRepository).findById(1L);
//     // }

//     // @Test
//     // void withdrawPlayer_PlayerNotFound_ReturnUserTournamentNotFoundException() {
        
    // }

    @Test
    void createPairings_FirstRound() {
        // Arrange
        tournament.setMaxElo(100);
        tournament.setSize(10);
        tournamentService.addTournament(tournament);
        
        User user1 = new User("player1", "player11", "ROLE_PLAYER", 40);
        User user2 = new User("player2", "player22", "ROLE_PLAYER", 35);
        User user3 = new User("player3", "player33", "ROLE_PLAYER", 15);
        User user4 = new User("player4", "player44", "ROLE_PLAYER", 10);
        List<User> userList = java.util.Arrays.asList(user1, user2, user3, user4);

        when(userTournamentService.getPlayers(tournament.getId())).thenReturn(userList);
        when(tournamentRepository.findById(tournament.getId())).thenReturn(Optional.of(tournament));
        when(matchService.getUserMatches(Mockito.any(User.class))).thenReturn(Collections.emptyList());
        when(matchService.getMatchesBetweenTwoUsers(Mockito.any(User.class), Mockito.any(User.class))).thenReturn(Collections.emptyList());
        Round mockRound = new Round(tournament);
        when(roundService.createRound(tournament)).thenReturn(mockRound);
        Match match1 = new Match(user1, user2, mockRound);
        Match match2 = new Match(user3, user4, mockRound);
        when(matchService.createMatch(user1, user2, mockRound)).thenReturn(match1);
        when(matchService.createMatch(user3, user4, mockRound)).thenReturn(match2);

        tournamentService.createPairings(tournament);
        Round returnedRound = tournament.getRounds().get(0);

        assertNotNull(returnedRound);
        assertEquals(2, returnedRound.getMatches().size());
        verify(matchService, times(2)).createMatch(Mockito.any(User.class), Mockito.any(User.class), Mockito.any(Round.class));
    }

    @Test
    void createPairings_SecondRound() {
        // Arrange
        tournament.setMaxElo(100);
        tournament.setSize(10);
        tournamentService.addTournament(tournament);
        
        User user1 = new User("player1", "player11", "ROLE_PLAYER", 40);
        User user2 = new User("player2", "player22", "ROLE_PLAYER", 35);
        User user3 = new User("player3", "player33", "ROLE_PLAYER", 15);
        User user4 = new User("player4", "player44", "ROLE_PLAYER", 10);
        List<User> userList = java.util.Arrays.asList(user1, user2, user3, user4);

        Round firstRound = new Round(tournament);
        Match match1 = new Match(user1, user2, firstRound);
        match1.setResult(1);
        Match match2 = new Match(user3, user4, firstRound);
        match2.setResult(1);
        List<Match> match1List = List.of(match1);
        List<Match> match2List = List.of(match2);
        when(matchService.getUserMatches(user1)).thenReturn(match1List);
        when(matchService.getUserMatches(user2)).thenReturn(match1List);
        when(matchService.getUserMatches(user3)).thenReturn(match2List);
        when(matchService.getUserMatches(user4)).thenReturn(match2List);

        // mock the sorting of users
        when(userTournamentService.getPlayers(tournament.getId())).thenReturn(userList);
        when(userTournamentService.getGamePoints(tournament.getId(),user1.getUsername())).thenReturn(1.0);
        when(userTournamentService.getGamePoints(tournament.getId(),user2.getUsername())).thenReturn(0.0);
        when(userTournamentService.getGamePoints(tournament.getId(),user3.getUsername())).thenReturn(1.0);
        when(userTournamentService.getGamePoints(tournament.getId(),user4.getUsername())).thenReturn(0.0);
        when(tournamentRepository.findById(tournament.getId())).thenReturn(Optional.of(tournament));

        // when(matchService.getMatchesBetweenTwoUsers(user1, user2)).thenReturn(match1List);
        when(matchService.getMatchesBetweenTwoUsers(user1, user3)).thenReturn(Collections.emptyList());
        when(matchService.getMatchesBetweenTwoUsers(user2, user4)).thenReturn(Collections.emptyList());

        Round secondRound = new Round(tournament);
        when(roundService.createRound(tournament)).thenReturn(secondRound);
        Match match3 = new Match(user3, user1, secondRound);
        Match match4 = new Match(user2, user4, secondRound);
        when(matchService.createMatch(user3, user1, secondRound)).thenReturn(match3);
        when(matchService.createMatch(user2, user4, secondRound)).thenReturn(match4);

        tournamentService.createPairings(tournament);
        Round returnedRound = tournament.getRounds().get(0);

        assertNotNull(returnedRound);
        assertEquals(2, returnedRound.getMatches().size());
        verify(matchService, times(2)).createMatch(Mockito.any(User.class), Mockito.any(User.class), Mockito.any(Round.class));
    }

    @Test
    void endTournamentCalculateElo() {
        // arrange
        tournament.setMaxElo(100);
        tournament.setSize(10);
        tournamentService.addTournament(tournament);
        
        User user1 = new User("player1", "player11", "ROLE_PLAYER", 40);
        User user2 = new User("player2", "player22", "ROLE_PLAYER", 35);
        User user3 = new User("player3", "player33", "ROLE_PLAYER", 15);
        User user4 = new User("player4", "player44", "ROLE_PLAYER", 10);
        List<User> userList = java.util.Arrays.asList(user1, user2, user3, user4);

        Round firstRound = new Round(tournament);
        Match match1 = new Match(user1, user2, firstRound);
        match1.setResult(1);
        Match match2 = new Match(user3, user4, firstRound);
        match2.setResult(1);
        firstRound.getMatches().addAll(List.of(match1, match2));

        Round secondRound = new Round(tournament);
        Match match3 = new Match(user3, user1, secondRound);
        match3.setResult(-1);
        Match match4 = new Match(user2, user4, secondRound);
        match4.setResult(-1);
        secondRound.getMatches().addAll(List.of(match3, match4));

        tournament.getRounds().addAll(List.of(firstRound, secondRound));
        // mock the get players in UT service
        when(userTournamentService.getPlayers(tournament.getId())).thenReturn(userList);

        // mock the find tournament
        when(tournamentRepository.findById(tournament.getId())).thenReturn(Optional.of(tournament));

        // act
        tournamentService.endTournament(tournament.getId());
        
        // assert
        assertTrue(user1.getELO()>40);
        assertTrue(user2.getELO()<35);
        assertTrue(user3.getELO()>15);
        assertTrue(user4.getELO()>10);
    }

//     @Test
//     void addRound_TournamentNotFound_ReturnTournamentNotFoundException() {
//         // Retrieve empty mock tournament
//         when(tournamentRepository.findById(tournament.getId())).thenReturn(Optional.empty());

//         // Act & Assert: Expect TournamentNotFoundException to be thrown
//         TournamentNotFoundException exception = assertThrows(TournamentNotFoundException.class, () -> {
//             tournamentService.addRound(tournament.getId());
//         });

//         // Verify that the exception message is correct
//         assertEquals("Could not find tournament 1", exception.getMessage());

        // Verify that deleteById was never called with the correct argument
        verify(tournamentRepository).findById(1L);
    }

    @Test
    void addRound_InvalidTournamentStatusTime_ReturnInvalidTournamentStatus() {
        // Arrange
        LocalDateTime time = LocalDateTime.of(2999, Month.JANUARY, 1, 10, 10, 30);
        tournament.setDate(time);
        tournament.setSize(3);

        // Retrieve empty mock tournament
        when(tournamentRepository.findById(tournament.getId())).thenReturn(Optional.of(tournament));

        // Act & Assert: Expect TournamentNotFoundException to be thrown
        InvalidTournamentStatus tournamentStatus = assertThrows(InvalidTournamentStatus.class, () -> {
            tournamentService.addRound(tournament.getId());
        });

        // Verify that the exception message is correct
        assertEquals("Wait for Tournament Start Date", tournamentStatus.getMessage());

        // Verify that deleteById was never called with the correct argument
        verify(tournamentRepository).findById(1L);
    }

    @Test
    void addRound_InvalidTournamentStatusPlayerSize_ReturnInvalidTournamentStatus() {
        // Arrange
        LocalDateTime time = LocalDateTime.of(2014, Month.JANUARY, 1, 10, 10, 30);
        tournament.setDate(time);

        // Retrieve empty mock tournament
        when(tournamentRepository.findById(tournament.getId())).thenReturn(Optional.of(tournament));
        when(userTournamentService.getPlayers(tournament.getId())).thenReturn(new ArrayList<>());

        // Act & Assert: Expect TournamentNotFoundException to be thrown
        InvalidTournamentStatus tournamentStatus = assertThrows(InvalidTournamentStatus.class, () -> {
            tournamentService.addRound(tournament.getId());
        });

        // Verify that the exception message is correct
        assertEquals("Need at least 2 Players registered", tournamentStatus.getMessage());

        // Verify that deleteById was never called with the correct argument
        verify(tournamentRepository).findById(1L);
    }

    @Test
    void addRound_AddSuccess_ReturnRound() {
        // Arrange 
        LocalDateTime time = LocalDateTime.of(2014, Month.JANUARY, 1, 10, 10, 30);
        tournament.setDate(time);
        tournament.setSize(10);
        tournament.setMaxElo(200);
        tournament.setMinElo(100);
        List<User> playerList = new ArrayList<>();
        User player1 = new User("player1", "player11", "ROLE_PLAYER", 100);
        User player2 = new User("player2", "player21", "ROLE_PLAYER", 200);
        playerList.add(player1);
        playerList.add(player2);

        // Mock findbyId and save
        when(tournamentRepository.findById(tournament.getId())).thenReturn(Optional.of(tournament));
        when(userTournamentService.getPlayers(tournament.getId())).thenReturn(playerList);

        // Act, add 1 round to tournament
        tournamentService.addRound(tournament.getId());

        // Assert
        assertEquals(1, tournament.getRounds().size());
        verify(tournamentRepository, times(2)).findById(tournament.getId());
    }

    // @Test
    // void updateResult_MatchNotEnded_ReturnMatchNotCompletedException() {

    // }
    
    // @Test
    // void updateResult_UpdatedResult_ReturnNothing() {

    // }

    @Test
    void getTournamentAboveMin_TournamentAboveMin_ReturnListOfTournamentAboveMin() {
        // Arrange
        int elo = 100;
        Tournament testTournament = new Tournament(1L, null, null, 200, 300, null, elo, 10, false, null);
        List<Tournament> tournaments = new ArrayList<>();
        tournaments.add(testTournament);

        // Mock repos
        when(tournamentRepository.findAll()).thenReturn(tournaments);

        // Act
        List<Tournament> result = tournamentService.getTournamentAboveMin(elo);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(200, result.get(0).getMinElo());
        verify(tournamentRepository).findAll();
    }

    @Test
    void getTournamentAboveMin_TournamentBelowMin_ReturnEmptyList() {
        // Arrange
        int elo = 100;
        Tournament testTournament = new Tournament(1L, null, null, 50, 75, null, elo, 10, false, null);
        List<Tournament> tournaments = new ArrayList<>();
        tournaments.add(testTournament);

        // Mock repos
        when(tournamentRepository.findAll()).thenReturn(tournaments);

        // Act
        List<Tournament> result = tournamentService.getTournamentAboveMin(elo);

        // Assert
        assertNotNull(result);
        assertEquals(0, result.size());
        verify(tournamentRepository).findAll();
    }

    @Test
    void getTournamentAboveMin_TournamentIsMin_ReturnListOfTournamentAboveMin() {
        // Arrange
        int elo = 100;
        Tournament testTournament = new Tournament(1L, null, null, 100, 300, null, elo, 10, false, null);
        List<Tournament> tournaments = new ArrayList<>();
        tournaments.add(testTournament);

        // Mock repos
        when(tournamentRepository.findAll()).thenReturn(tournaments);

        // Act
        List<Tournament> result = tournamentService.getTournamentAboveMin(elo);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(100, result.get(0).getMinElo());
        verify(tournamentRepository).findAll();
    }

    @Test
    void getTournamentAboveMin_NoTournaments_ReturnEmptyList() {
        // Arrange
        int elo = 100;
        List<Tournament> tournaments = new ArrayList<>();

        // Mock repos
        when(tournamentRepository.findAll()).thenReturn(tournaments);

        // Act
        List<Tournament> result = tournamentService.getTournamentAboveMin(elo);

        // Assert
        assertNotNull(result);
        assertEquals(0, result.size());
        verify(tournamentRepository).findAll();
    }

    @Test
    void getTournamentBelowMax_TournamentBelowMax_ReturnListOfTournamentBelowMax() {
        // Arrange
        int elo = 100;
        Tournament testTournament = new Tournament(1L, null, null, 50, 75, null, elo, 10, false, null);
        List<Tournament> tournaments = new ArrayList<>();
        tournaments.add(testTournament);

        // Mock repos
        when(tournamentRepository.findAll()).thenReturn(tournaments);

        // Act
        List<Tournament> result = tournamentService.getTournamentBelowMax(elo);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(75, result.get(0).getMaxElo());
        verify(tournamentRepository).findAll();
    }

    @Test
    void getTournamentBelowMax_TournamentAboveMax_ReturnEmptyList() {
        // Arrange
        int elo = 100;
        Tournament testTournament = new Tournament(1L, null, null, 50, 200, null, elo, 10, false, null);
        List<Tournament> tournaments = new ArrayList<>();
        tournaments.add(testTournament);

        // Mock repos
        when(tournamentRepository.findAll()).thenReturn(tournaments);

        // Act
        List<Tournament> result = tournamentService.getTournamentBelowMax(elo);

        // Assert
        assertNotNull(result);
        assertEquals(0, result.size());
        verify(tournamentRepository).findAll();
    }

    @Test
    void getTournamentBelowMax_TournamentIsMax_ReturnListOfTournamentBelowMax() {
        // Arrange
        int elo = 100;
        Tournament testTournament = new Tournament(1L, null, null, 50, 100, null, elo, 10, false, null);
        List<Tournament> tournaments = new ArrayList<>();
        tournaments.add(testTournament);

        // Mock repos
        when(tournamentRepository.findAll()).thenReturn(tournaments);

        // Act
        List<Tournament> result = tournamentService.getTournamentBelowMax(elo);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(100, result.get(0).getMaxElo());
        verify(tournamentRepository).findAll();
    }

    @Test
    void getTournamentBelowMax_NoTournaments_ReturnEmptyList() {
        // Arrange
        int elo = 100;
        List<Tournament> tournaments = new ArrayList<>();

        // Mock repos
        when(tournamentRepository.findAll()).thenReturn(tournaments);

        // Act
        List<Tournament> result = tournamentService.getTournamentBelowMax(elo);

        // Assert
        assertNotNull(result);
        assertEquals(0, result.size());
        verify(tournamentRepository).findAll();
    }

    @Test
    void getTournamentAboveMinBelowMax_TournamentAboveMinNotBelowMax_ReturnEmptyList() {
        // Arrange
        int minElo = 100;
        int maxElo = 200;
        Tournament testTournament = new Tournament(1L, null, null, 150, 250, null, 10, 10, false, null);
        List<Tournament> tournaments = new ArrayList<>();
        tournaments.add(testTournament);

        // Mock repos
        when(tournamentRepository.findAll()).thenReturn(tournaments);

        // Act
        List<Tournament> result = tournamentService.getTournamentAboveMinBelowMax(minElo, maxElo);

        // Assert
        assertNotNull(result);
        assertEquals(0, result.size());
        verify(tournamentRepository, times(2)).findAll();
    }

    @Test
    void getTournamentAboveMinBelowMax_TournamentNotAboveMinBelowMax_ReturnEmptyList() {
        // Arrange
        int minElo = 100;
        int maxElo = 200;
        Tournament testTournament = new Tournament(1L, null, null, 50, 150, null, 10, 10, false, null);
        List<Tournament> tournaments = new ArrayList<>();
        tournaments.add(testTournament);

        // Mock repos
        when(tournamentRepository.findAll()).thenReturn(tournaments);

        // Act
        List<Tournament> result = tournamentService.getTournamentAboveMinBelowMax(minElo, maxElo);

        // Assert
        assertNotNull(result);
        assertEquals(0, result.size());
        verify(tournamentRepository, times(2)).findAll();
    }

    @Test
    void getTournamentAboveMinBelowMax_TournamentAboveMinBelowMax_ReturnListOfTournamentAboveMinBelowMax() {
        // Arrange
        int minElo = 100;
        int maxElo = 200;
        Tournament testTournament = new Tournament(1L, null, null, 125, 175, null, 10, 10, false, null);
        List<Tournament> tournaments = new ArrayList<>();
        tournaments.add(testTournament);

        // Mock repos
        when(tournamentRepository.findAll()).thenReturn(tournaments);

        // Act
        List<Tournament> result = tournamentService.getTournamentAboveMinBelowMax(minElo, maxElo);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(125, result.get(0).getMinElo());
        assertEquals(175, result.get(0).getMaxElo());
        verify(tournamentRepository, times(2)).findAll();
    }

    @Test
    void getTournamentAboveMinBelowMax_NoTournament_ReturnEmptyList() {
        // Arrange
        int minElo = 100;
        int maxElo = 200;
        List<Tournament> tournaments = new ArrayList<>();

        // Mock repos
        when(tournamentRepository.findAll()).thenReturn(tournaments);

        // Act
        List<Tournament> result = tournamentService.getTournamentAboveMinBelowMax(minElo, maxElo);

        // Assert
        assertNotNull(result);
        assertEquals(0, result.size());
        verify(tournamentRepository, times(2)).findAll();
    }

    @Test
    void getUserEligibleTournament_Eligible_ReturnTournamentList() {
        // Arrange
        player.setELO(200);
        Tournament tournament = new Tournament(1L, null, null, 100, 300, null, 10, 10, false, null);
        List<Tournament> tournaments = new ArrayList<>();
        tournaments.add(tournament);

        // Mock repository
        when(tournamentRepository.findAll()).thenReturn(tournaments);

        // Act
        List<Tournament> result = tournamentService.getUserEligibleTournament(player.getELO());

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getId());
        verify(tournamentRepository).findAll();
    }

    @Test
    void getUserEligibleTournament_NotEligible_ReturnTournamentList() {
        // Arrange
        player.setELO(0);
        Tournament tournament = new Tournament(1L, null, null, 100, 300, null, 10, 10, false, null);
        List<Tournament> tournaments = new ArrayList<>();
        tournaments.add(tournament);

        // Mock repository
        when(tournamentRepository.findAll()).thenReturn(tournaments);

        // Act
        List<Tournament> result = tournamentService.getUserEligibleTournament(player.getELO());

        // Assert
        assertNotNull(result);
        assertEquals(0, result.size());
        verify(tournamentRepository).findAll();
    }

//     @Test
//     void addRound_AddSuccess_ReturnRound() {
//         // Arrange
//         List<Round> rounds = new ArrayList<>();
//         Round round = new Round(1L, tournament, null);

//         // Mock findbyId and save
//         when(tournamentRepository.findById(tournament.getId())).thenReturn(Optional.of(tournament));
//         when(tournamentRepository.save(tournament)).thenReturn(tournament);

//         // Act
        

//         // Assert

//     }


// //     @Test
// //     void testTournamentExists() {
// //         // when(tournamentRepository.existsById(1L)).thenReturn(true);

// //         // boolean exists = tournamentService.tournamentExists(1L);

// //         // assertTrue(exists);
// //         // verify(tournamentRepository, times(1)).existsById(1L);
// //     }
// }