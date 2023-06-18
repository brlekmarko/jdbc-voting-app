package hr.fer.zemris.java.p12.servlets;


import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import hr.fer.zemris.java.p12.dao.DAOProvider;
import hr.fer.zemris.java.p12.model.Poll;


@WebServlet("/servleti/index.html")
public class LinksToPollsServlet extends HttpServlet{

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		resp.setContentType("text/html; charset=utf-8");
		resp.setStatus(HttpServletResponse.SC_OK);
		
		try {
			List<Poll> polls = DAOProvider.getDao().dohvatiSvePollove();
			req.setAttribute("polls", polls);
			req.getRequestDispatcher("/WEB-INF/pages/listaPollova.jsp").forward(req, resp);
		} catch (Exception e) {
			req.getRequestDispatcher("/WEB-INF/pages/invalid.jsp").forward(req, resp);
			return;
		}
	}
}
