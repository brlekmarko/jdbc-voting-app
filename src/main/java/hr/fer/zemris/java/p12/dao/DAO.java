package hr.fer.zemris.java.p12.dao;

import java.util.List;

import hr.fer.zemris.java.p12.model.Poll;
import hr.fer.zemris.java.p12.model.PollOption;

/**
 * Sučelje prema podsustavu za perzistenciju podataka.
 * 
 * @author Marko Brlek
 *
 */
public interface DAO {

	public Poll dohvatiPoll(long id);
	
	public List<Poll> dohvatiSvePollove();
	
	public List<PollOption> dohvatiOpcijeZaPoll(long id);
	
	public boolean glasajZa(long pollOptionId);
	
}