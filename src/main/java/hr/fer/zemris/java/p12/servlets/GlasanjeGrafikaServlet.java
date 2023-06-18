package hr.fer.zemris.java.p12.servlets;

import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.chart.util.Rotation;
import org.jfree.data.general.DefaultPieDataset;
import hr.fer.zemris.java.p12.dao.DAOProvider;
import hr.fer.zemris.java.p12.model.PollOption;

import org.jfree.chart.ChartUtils;

@WebServlet("/servleti/glasanje-grafika")
public class GlasanjeGrafikaServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("image/png");
		resp.setStatus(HttpServletResponse.SC_OK);
		
		JFreeChart chart = getChart(req, resp);
		if (chart == null) return;
		int width = 500;
		int height = 500;
		
		ChartUtils.writeChartAsPNG(resp.getOutputStream(), chart, width, height);
	}
	
	
	//https://www.avajava.com/tutorials/lessons/how-do-i-return-a-dynamically-generated-pie-chart-from-a-servlet.html
	public JFreeChart getChart(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		/// prvo ucitamo sve opcije
		long pollID = Long.parseLong(req.getParameter("pollID"));
		List<PollOption> options = DAOProvider.getDao().dohvatiOpcijeZaPoll(pollID);

		if (options == null) return null;
		
		int ukupnoGlasova = 0;
		for(PollOption po : options) {
			ukupnoGlasova += po.getVotesCount();
		}
		
		DefaultPieDataset<String> dataset = new DefaultPieDataset<>();
		for(PollOption po : options) {
			dataset.setValue(po.getOptionTitle(), (double)po.getVotesCount()/ukupnoGlasova);
		}

		boolean legend = true;
		boolean tooltips = true;
		boolean urls = false;

		JFreeChart chart = ChartFactory.createPieChart3D("Options votes", dataset, legend, tooltips, urls);

		PiePlot3D plot = (PiePlot3D) chart.getPlot();
        plot.setStartAngle(290);
        plot.setDirection(Rotation.CLOCKWISE);
        //plot.setForegroundAlpha(0.5f);

		return chart;
	}
}

