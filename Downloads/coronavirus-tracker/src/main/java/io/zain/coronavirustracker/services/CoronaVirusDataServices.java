package io.zain.coronavirustracker.services;
import java.awt.List;
import java.io.StringReader;
import java.net.URI;
import java.net.http.*;
import java.util.ArrayList;
//import io.zain.coronavirustracker.models;

import javax.annotation.PostConstruct;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import io.zain.coronavirustracker.models.locationStats;
@Service
public class CoronaVirusDataServices  {
	private static String VIRUS_DATA_URL = "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_confirmed_global.csv";
     
	private ArrayList<locationStats> allStats = new ArrayList<locationStats>();
	@PostConstruct
	@Scheduled(cron = "* * 1 * * *")
	public void fetchVirusData()throws Exception
      {
		 ArrayList<locationStats> newStats = new ArrayList<locationStats>();
    	  HttpClient client = HttpClient.newHttpClient();
    	 HttpRequest request = HttpRequest.newBuilder()
    			 .uri(URI.create(VIRUS_DATA_URL))
    			 .build();
    	HttpResponse<String> httpResponse = client.send(request, HttpResponse.BodyHandlers.ofString());
    //	System.out.println(httpResponse.body());
    	
    	StringReader csvBodyReader = new StringReader(httpResponse.body());
    	Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(csvBodyReader);
    	
    	for (CSVRecord record : records) {
    		locationStats locationstat = new locationStats();
    		locationstat.setState(record.get("Province/State"));
    		locationstat.setCountry(record.get("Country/Region"));
    		locationstat.setLatestTotalCases(Integer.parseInt(record.get((record.size() - 1))));
    		
    		
    	    
    	    System.out.println(locationstat);
    	    
    	    newStats.add(locationstat);
    	    //String customerNo = record.get("CustomerNo");
    	   // String name = record.get("Name");
    	}
    	this.allStats = newStats;
      }
}
