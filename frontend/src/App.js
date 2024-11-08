// src/App.js
import React from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import Registration from './screens/Registration';
import Login from './screens/Login'; 
import TournamentManagement from './screens/Tournaments';
import TournamentRounds from './screens/Rounds';
import TournamentStandings from './screens/Standings';
import AddTournament from './screens/AddTournament';
import UpdateTournament from './screens/UpdateTournament';
import Profile from './screens/Profile'

const App = () => {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<Navigate to="/login" />} /> 
        <Route path="/signup" element={<Registration />} />
        <Route path="/login" element={<Login />} />
        <Route path="/tournaments" element={<TournamentManagement />} />
        {/* Both */}
        <Route path="/tournaments/:tournamentId/rounds/:roundNumber" element={<TournamentRounds />} />
        <Route path="/tournaments/:tournamentId/standings" element={<TournamentStandings />} />
        {/* User */}
        <Route path='/profile' element={<Profile />} />
        {/* Admin */}
        <Route path='/addTournament' element={<AddTournament />}/>
        <Route path='/updateTournament/:tournamentId' element={<UpdateTournament />}/>
        <Route />
      </Routes>
    </Router>
  );
};

export default App;
