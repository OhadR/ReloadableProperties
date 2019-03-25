package com.ohadr.ae_case_analysis.service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.NoSuchElementException;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ohadr.ae_case_analysis.repository.AERepository;
import com.opencsv.CSVWriter;

import static com.ohadr.ae_case_analysis.repository.AERepository.*; 

@Service
public class ExtractionMetricsWriterService {

	private static Logger logger = Logger.getLogger(ExtractionMetricsWriterService.class);

	private static final String PATH_TO_WORK_DIR = System.getProperty("user.dir") + File.separator 
			+ "src\\main\\webapp\\outputs" + File.separator;

	@Autowired
	private AERepository storage;

	@PostConstruct
    void postConstruct()
	{
		// create folder for the files:
		File dir = new File(PATH_TO_WORK_DIR );
		if (!dir.exists())
		{
			dir.mkdirs();
			logger.info("Folder successfully created. folderName: " + dir.getAbsolutePath());
		}
    }

	/**
	 * 
	 * @param caseId
	 * @return the saved file-name, so it can be used by controller to download.
	 * @throws IOException
	 */
	public String write(String caseId) throws IOException 
	{
		String fileName = PATH_TO_WORK_DIR + caseId + ".csv";
		Writer writer = new FileWriter(fileName);
		CSVWriter csvWriter = new CSVWriter(writer);
		String headerFields = "name,Decoding Duration,Sanitizing Duration,Parse Duration,Process-Persons Duration,documentAnalytics Duration";
		csvWriter.writeNext(headerFields.split(","));

		validateCaseIdExists(caseId);
		List<Document> extractions = storage.getExtractionMetricsForCase(caseId);
		
		for(Document extraction : extractions)
		{
			Document metrics = extraction.get(METRICS_FIELD_NAME, Document.class);
			String[] nextLine = {extraction.getString("name"),
					convertMillisStrToSecondsStr(metrics.getString(DECODING_DURATION_FIELD_NAME)),
					convertMillisStrToSecondsStr(metrics.getString(SANITIZING_DURATION_FIELD_NAME)),
					convertMillisStrToSecondsStr(metrics.getString(PARSE_DURATION_FIELD_NAME)),
					convertMillisStrToSecondsStr(metrics.getString(PROCESS_PERSONS_DURATION_FIELD_NAME)),
					convertMillisStrToSecondsStr(metrics.getString(DOCUMENT_ANALYTICS_DURATION_FIELD_NAME))};
			csvWriter.writeNext(nextLine);
		}

		csvWriter.flushQuietly();
		csvWriter.close();
		writer.close();
		return fileName;
	}

	private void validateCaseIdExists(String caseId) throws IOException 
	{
		try
		{
			storage.getCaseById(caseId);
		}
		catch (NoSuchElementException infe)
		{
			logger.error("could not find case with id: " + caseId);
			throw new IOException(infe);
		}
	}
	
	/**
	 * 
	 * @param millisStr string that represents milliseconds
	 * @return divide the input by 1000.
	 */
	private String convertMillisStrToSecondsStr(String millisStr)
	{
		if(millisStr == null || millisStr.isEmpty())
			return "";
		
		Integer millis;
		try
		{
			millis = Integer.valueOf(millisStr);
		}
		catch(NumberFormatException nfe)
		{
			return "";
		}
		
		Integer secs = millis / 1000;		
		return secs.toString();		
	}

}
