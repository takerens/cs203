package csd.grp3.round;

import org.springframework.beans.factory.annotation.Autowired;

public class RoundServiceImpl implements RoundService {

    @Autowired
    private RoundRepository rounds;

    @Override
    public Round getRound(Long id) {
        return rounds.findById(id)
            .orElseThrow(() -> new RoundNotFoundException());
    }
}
