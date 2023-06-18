package hr.fer.zemris.java.p12.servlets;

import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import hr.fer.zemris.java.p12.dao.DAOProvider;
import hr.fer.zemris.java.p12.model.PollOption;

@WebServlet("/servleti/glasanje-xls")
public class GlasanjeXlsServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// https://stackoverflow.com/questions/2937465/what-is-correct-content-type-for-excel-files
		resp.setContentType("application/vnd.ms-excel");
		resp.setStatus(HttpServletResponse.SC_OK);
		
		
		resp.setHeader("Content-Disposition", "attachment; filename=\"glasovi.xls\"");
		
		/// prvo ucitamo sve opcije
		long pollID = Long.parseLong(req.getParameter("pollID"));
		List<PollOption> options = DAOProvider.getDao().dohvatiOpcijeZaPoll(pollID);
		
		HSSFWorkbook hwb = new HSSFWorkbook();

		HSSFRow row;
		HSSFSheet sheet =  hwb.createSheet("Glasovi");
		
		row = sheet.createRow((short)0);
		row.createCell((short) 0).setCellValue("Option name");
		row.createCell((short) 1).setCellValue("Number of votes");
			
		int counter = 1;
		for(PollOption po : options) {
			row = sheet.createRow(counter); // header je red 0
			row.createCell((short) 0).setCellValue(po.getOptionTitle());
			row.createCell((short) 1).setCellValue(po.getVotesCount());
			counter++;
		}
		
		sheet.autoSizeColumn(0);
		sheet.autoSizeColumn(1);
		
		hwb.write(resp.getOutputStream());
		hwb.close();
		
		resp.getOutputStream().flush();
	}
}
