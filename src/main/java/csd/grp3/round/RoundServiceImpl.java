package csd.grp3.round;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import csd.grp3.tournament.Tournament;

@Service
public class RoundServiceImpl implements RoundService {

    @Autowired
    private RoundRepository rounds;

    @Override
    public Round createRound(Tournament tournament) {
        return rounds.save(new Round(tournament));
    }

    @Override
    public Round getRound(Long id) {
        return rounds.findById(id)
            .orElseThrow(() -> new RoundNotFoundException());
    }
}
