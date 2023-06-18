package hr.fer.zemris.java.p12;

import java.beans.PropertyVetoException;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mchange.v2.c3p0.DataSources;

@WebListener
public class Inicijalizacija implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		
		String realPath = sce.getServletContext().getRealPath("/WEB-INF/dbsettings.properties");
		
		Properties prop = new Properties();
		try {
			prop.load(new FileInputStream(realPath));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String host = prop.getProperty("host");
		String port = prop.getProperty("port");
		String dbName = prop.getProperty("name");
		String user = prop.getProperty("user");
		String password = prop.getProperty("password");

		String connectionURL = "jdbc:derby://" + host + ":" + port + "/" + dbName + ";user=" + user + ";password=" + password;

		ComboPooledDataSource cpds = new ComboPooledDataSource();
		try {
			cpds.setDriverClass("org.apache.derby.client.ClientAutoloadedDriver");
		} catch (PropertyVetoException e1) {
			throw new RuntimeException("Pogreška prilikom inicijalizacije poola.", e1);
		}
		cpds.setJdbcUrl(connectionURL);

		sce.getServletContext().setAttribute("hr.fer.zemris.dbpool", cpds);

		// create tables if they don't exist
			
		try (Connection con = cpds.getConnection()){
			DatabaseMetaData metadata = con.getMetaData();
			ResultSet results = metadata.getTables(null, null, "POLLS", null);
			String sql;
			if(!results.next()){
				// tablica polls ne postoji, potrebno ju je napraviti
				sql = """
						CREATE TABLE Polls
							(id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
							title VARCHAR(150) NOT NULL,
							message CLOB(2048) NOT NULL
						)
						""";
				con.prepareStatement(sql).execute();
			}

			results = metadata.getTables(null, null, "POLLOPTIONS", null);

			if(!results.next()){
				// tablica pollOptions ne postoji, potrebno ju je napraviti
				sql = """
					CREATE TABLE PollOptions
						(id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
						optionTitle VARCHAR(100) NOT NULL,
						optionLink VARCHAR(150) NOT NULL,
						pollID BIGINT,
						votesCount BIGINT,
						FOREIGN KEY (pollID) REFERENCES Polls(id)
					)
					""";
				con.prepareStatement(sql).execute();
			}

			// popuni tablicu polls ako je prazna
			results = con.prepareStatement("SELECT * FROM Polls").executeQuery();
			if(!results.next()){
				sql = """
					INSERT INTO Polls (title, message) VALUES
						('Glasanje za omiljeni bend',
						'Od sljedećih bendova, koji Vam je bend najdraži? Kliknite na link kako biste glasali!'),
						('Glasanje za omiljeni obavezni predmet preddiplomskog studija',
						'Od sljedećih preddiplomskih predmeta, koji Vam je predmet bio najdraži? Kliknite na link kako biste glasali!')
					""";
				con.prepareStatement(sql).execute();

				// trenutne opcije u polloptions su mozda na krivim poll id-ovima, obrisi ih pa ih ponovno napravi
				// mozda uopce nema opcija
				con.prepareStatement("DELETE FROM PollOptions").execute();

				// dohvati id od polla za bendove
				results = con.prepareStatement("SELECT id FROM Polls WHERE title='Glasanje za omiljeni bend'").executeQuery();
				results.next();
				long id = results.getLong(1);

				// popuni tablicu pollOptions sa bendovima
				sql = """
						INSERT INTO PollOptions (optionTitle, optionLink, pollID, votesCount) VALUES
							('The Beatles', 'https://www.youtube.com/watch?v=z9ypq6_5bsg', ${id}, 0),
							('The Platters', 'https://www.youtube.com/watch?v=H2di83WAOhU', ${id}, 0),
							('The Beach Boys', 'https://www.youtube.com/watch?v=2s4slliAtQU', ${id}, 0),
							('The Four Seasons', 'https://www.youtube.com/watch?v=y8yvnqHmFds', ${id}, 0),
							('The Marcels', 'https://www.youtube.com/watch?v=qoi3TH59ZEs', ${id}, 0),
							('The Everly Brothers', 'https://www.youtube.com/watch?v=tbU3zdAgiX8', ${id}, 0),
							('The Mamas And The Papas', 'https://www.youtube.com/watch?v=N-aK6JnyFmk', ${id}, 0)
						""".replace("${id}", id + "");
						
				con.prepareStatement(sql).execute();


				// dohvati id od polla za predmete
				results = con.prepareStatement("SELECT id FROM Polls WHERE title='Glasanje za omiljeni obavezni predmet preddiplomskog studija'").executeQuery();
				results.next();
				id = results.getLong(1);

				// popuni tablicu pollOptions sa predmetima
				sql = """
						INSERT INTO PollOptions (optionTitle, optionLink, pollID, votesCount) VALUES
							('Uvod u programiranje', 'https://www.fer.unizg.hr/predmet/uup_a', ${id}, 0),
							('Diskretna matematika 1', 'https://www.fer.unizg.hr/predmet/dismat1_a', ${id}, 0),
							('Linearna algebra', 'https://www.fer.unizg.hr/predmet/linearna', ${id}, 0),
							('Osnove elektrotehnike', 'https://www.fer.unizg.hr/predmet/oe', ${id}, 0),
							('Digitalna Logika', 'https://www.fer.unizg.hr/predmet/linearna', ${id}, 0),
							('Matematička analiza 1', 'https://www.fer.unizg.hr/predmet/matan1', ${id}, 0),
							('Objektno orijentirano programiranje', 'https://www.fer.unizg.hr/predmet/oop', ${id}, 0)
					""".replace("${id}", id + "");
					
				con.prepareStatement(sql).execute();
			}


			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		ComboPooledDataSource cpds = (ComboPooledDataSource)sce.getServletContext().getAttribute("hr.fer.zemris.dbpool");
		if(cpds!=null) {
			try {
				DataSources.destroy(cpds);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

}
