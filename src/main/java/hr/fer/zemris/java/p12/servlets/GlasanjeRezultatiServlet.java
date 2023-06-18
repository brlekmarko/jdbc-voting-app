package hr.fer.zemris.java.p12.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import hr.fer.zemris.java.p12.dao.DAOProvider;
import hr.fer.zemris.java.p12.model.PollOption;

@WebServlet("/servleti/glasanje-rezultati")
public class GlasanjeRezultatiServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// https://stackoverflow.com/questions/2937465/what-is-correct-content-type-for-excel-files
		resp.setContentType("text/html; charset=utf-8");
		resp.setStatus(HttpServletResponse.SC_OK);
		
		/// prvo ucitamo sve opcije
		long pollID = Long.parseLong(req.getParameter("pollID"));
		List<PollOption> options = DAOProvider.getDao().dohvatiOpcijeZaPoll(pollID);

		req.setAttribute("pollID", pollID);
		req.setAttribute("options", options);
		
		long maxGlasovi = 0;
		for(PollOption po : options) {
			if(po.getVotesCount() >= maxGlasovi) {
				maxGlasovi = po.getVotesCount();
			}
		}
		
		List<PollOption> pobjednici = new ArrayList<>();
		
		for(PollOption po : options) {
			if(po.getVotesCount() == maxGlasovi) {
				pobjednici.add(po);
			}
		}
		
		req.setAttribute("pobjednici", pobjednici);
		
		req.getRequestDispatcher("/WEB-INF/pages/glasanjeRez.jsp").forward(req, resp);
	}
	
}
