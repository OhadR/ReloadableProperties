package com.ohadr.ae_case_analysis.repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Projections;
import com.ohadr.ae_case_analysis.service.MongoClientWrapper;
import static com.mongodb.client.model.Filters.eq;

@Repository
public class AERepository {
	
	public static final String METRICS_FIELD_NAME = "metrics";

	public static final String DOCUMENT_ANALYTICS_DURATION_FIELD_NAME = "documentAnalytics Duration (MILLISECONDS)";
	public static final String PROCESS_PERSONS_DURATION_FIELD_NAME = "Process-Persons Duration (MILLISECONDS)";
	public static final String PARSE_DURATION_FIELD_NAME = "Parse Duration (MILLISECONDS)";
	public static final String SANITIZING_DURATION_FIELD_NAME = "Sanitizing Duration (MILLISECONDS)";
	public static final String DECODING_DURATION_FIELD_NAME = "Decoding Duration (MILLISECONDS)";

	private static final String EXTRACTIONS_COLL_NAME = "extraction";
	private static final String CASES_COLL_NAME = "case";
	

	@Autowired
	private MongoClientWrapper mongoWrapper;
	
    @Value("${mongo.db.name}")
    private String dbName;

    public void connectToMongoServer(String seed) throws IOException
    {
    	mongoWrapper.connect(seed);
    }
    
    // Return a list of cases
    public List<Document> getAllCases()
    {
    	MongoCollection<Document> casesCollection = mongoWrapper.getClient().getDatabase(dbName).getCollection(CASES_COLL_NAME);
        List<Document> posts = null;

//    	Bson filter = eq("PERMALINK_KEY", "permalink");

//        Bson sortByDate = Sorts.descending( DATE_KEY );

        posts = (List<Document>) casesCollection.find()
        		.projection(Projections.fields( Projections.include("name", "status")))
            	.into(new ArrayList<Document>());
        return posts;
    }

	public List<Document> getExtractionMetricsForCase(String caseId) 
	{
    	MongoCollection<Document> extractionsCollection = mongoWrapper.getClient().getDatabase(dbName).getCollection(EXTRACTIONS_COLL_NAME);

    	Bson filter = eq("caseId", caseId);

        List<Document> posts = (List<Document>) extractionsCollection.find(filter)
        		.filter(filter)
        		.projection(Projections.fields( Projections.include("name",
        				METRICS_FIELD_NAME + "." + DECODING_DURATION_FIELD_NAME,
        				METRICS_FIELD_NAME + "." + SANITIZING_DURATION_FIELD_NAME,
        				METRICS_FIELD_NAME + "." + PARSE_DURATION_FIELD_NAME,
        				METRICS_FIELD_NAME + "." + PROCESS_PERSONS_DURATION_FIELD_NAME,
        				METRICS_FIELD_NAME + "." + DOCUMENT_ANALYTICS_DURATION_FIELD_NAME)))
            	.into(new ArrayList<Document>());
        return posts;
	}

	public Document getCaseById(String caseId) throws NoSuchElementException
	{
    	MongoCollection<Document> casesCollection = mongoWrapper.getClient().getDatabase(dbName).getCollection(CASES_COLL_NAME);
    	Bson filter = eq(DBCollection.ID_FIELD_NAME, caseId);
    	
    	List<Document> cases = (List<Document>) casesCollection
    			.find(filter)
    			.into(new ArrayList<Document>());
    	
    	if(cases.size() == 0)
    		throw new NoSuchElementException();
    	
    	return cases.get(0);
		
	}

}
