package hr.fer.zemris.java.p12.dao.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import hr.fer.zemris.java.p12.dao.DAO;
import hr.fer.zemris.java.p12.dao.DAOException;
import hr.fer.zemris.java.p12.model.Poll;
import hr.fer.zemris.java.p12.model.PollOption;

/**
 * Ovo je implementacija podsustava DAO uporabom tehnologije SQL. Ova
 * konkretna implementacija očekuje da joj veza stoji na raspolaganju
 * preko {@link SQLConnectionProvider} razreda, što znači da bi netko
 * prije no što izvođenje dođe do ove točke to trebao tamo postaviti.
 * U web-aplikacijama tipično rješenje je konfigurirati jedan filter 
 * koji će presresti pozive servleta i prije toga ovdje ubaciti jednu
 * vezu iz connection-poola, a po zavrsetku obrade je maknuti.
 *  
 * @author Marko Brlek
 */
public class SQLDAO implements DAO {
	
	@Override
	public Poll dohvatiPoll(long id) {
		
		Poll toReturn = null;
		Connection con = SQLConnectionProvider.getConnection();
		PreparedStatement pst = null;
		try {
			pst = con.prepareStatement("select id, title, message from Polls where id=?");
			pst.setLong(1, Long.valueOf(id));
			try {
				ResultSet rs = pst.executeQuery();
				try {
					if(rs!=null && rs.next()) {
						toReturn = new Poll();
						toReturn.setId(rs.getLong(1));
						toReturn.setTitle(rs.getString(2));
						toReturn.setMessage(rs.getString(3));
					}
				} finally {
					try { rs.close(); } catch(Exception ignorable) {}
				}
			} finally {
				try { pst.close(); } catch(Exception ignorable) {}
			}
		} catch(Exception ex) {
			throw new DAOException("Pogreška prilikom dohvata liste pollova.", ex);
		}
		return toReturn;
	}
	

	@Override
	public List<Poll> dohvatiSvePollove() {
		
		List<Poll> toReturn = new ArrayList<>();
		Connection con = SQLConnectionProvider.getConnection();
		PreparedStatement pst = null;
		try {
			pst = con.prepareStatement("select id, title, message from Polls order by id");
			try {
				ResultSet rs = pst.executeQuery();
				try {
					while(rs!=null && rs.next()) {
						Poll poll = new Poll();
						poll.setId(rs.getLong(1));
						poll.setTitle(rs.getString(2));
						poll.setMessage(rs.getString(3));
						toReturn.add(poll);
					}
				} finally {
					try { rs.close(); } catch(Exception ignorable) {}
				}
			} finally {
				try { pst.close(); } catch(Exception ignorable) {}
			}
		} catch(Exception ex) {
			throw new DAOException("Pogreška prilikom dohvata liste pollova.", ex);
		}
		return toReturn;
	}

	@Override
	public List<PollOption> dohvatiOpcijeZaPoll(long id) {
		
		List<PollOption> toReturn = new ArrayList<>();
		
		Connection con = SQLConnectionProvider.getConnection();
		PreparedStatement pst = null;
		try {
			pst = con.prepareStatement("select id, optionTitle, optionLink, pollID, votesCount from PollOptions where pollID=? order by id");
			pst.setLong(1, Long.valueOf(id));
			try {
				ResultSet rs = pst.executeQuery();
				try {
					while(rs!=null && rs.next()) {
						PollOption option = new PollOption();
						option.setId(rs.getLong(1));
						option.setOptionTitle(rs.getString(2));
						option.setOptionLink(rs.getString(3));
						option.setPollID(rs.getLong(4));
						option.setVotesCount(rs.getLong(5));
						toReturn.add(option);
					}
				} finally {
					try { rs.close(); } catch(Exception ignorable) {}
				}
			} finally {
				try { pst.close(); } catch(Exception ignorable) {}
			}
		} catch(Exception ex) {
			throw new DAOException("Pogreška prilikom dohvata liste opcija.", ex);
		}
		return toReturn;
	}

	@Override
	public boolean glasajZa(long pollOptionId) {
		
		Connection con = SQLConnectionProvider.getConnection();
		PreparedStatement pst = null;
		int brojRedaka = 0;
		try {
			pst = con.prepareStatement("update PollOptions SET votesCount = votesCount + 1 where id=?");
			pst.setLong(1, Long.valueOf(pollOptionId));
			try {
				brojRedaka = pst.executeUpdate();

			} finally {
				try { pst.close(); } catch(Exception ignorable) {}
			}
		} catch(Exception ex) {
			throw new DAOException("Pogreška prilikom dodavanja glasa.", ex);
		}
		if (brojRedaka == 0) return false;
		return true;
	}
	
	


}