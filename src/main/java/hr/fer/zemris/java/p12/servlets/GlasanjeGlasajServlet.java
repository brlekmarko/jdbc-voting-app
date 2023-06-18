package hr.fer.zemris.java.p12.servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import hr.fer.zemris.java.p12.dao.DAOException;
import hr.fer.zemris.java.p12.dao.DAOProvider;


@WebServlet("/servleti/glasanje-glasaj")
public class GlasanjeGlasajServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		resp.setContentType("text/html; charset=utf-8");
		resp.setStatus(HttpServletResponse.SC_OK);

		long optionID = Long.parseLong(req.getParameter("optionID"));
		long pollID = Long.parseLong(req.getParameter("pollID"));
		
		try {
			DAOProvider.getDao().glasajZa(optionID);
			resp.sendRedirect(req.getContextPath() + "/servleti/glasanje-rezultati?pollID=" + pollID);
		}catch(DAOException e) {
			req.getRequestDispatcher("/WEB-INF/pages/invalid.jsp").forward(req, resp);
		}
	}
	
}
