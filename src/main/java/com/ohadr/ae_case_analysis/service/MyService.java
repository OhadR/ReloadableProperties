package com.ohadr.ae_case_analysis.service;

import java.io.IOException;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoIterable;

@Configuration
public class MyService {

	@Value("${mongo.user}")
    private String user; // the user name
	@Value("${mongo.db.name}")
	private String database; // the name of the database in which the user is defined
	@Value("${mongo.pwd}")
	private char[] password; // the password as a character array


	private MongoClient mongoClient;


	public MongoClient connect(String seed) throws IOException
	{
		if(mongoClient != null)
			mongoClient.close();
		
		//credentials:
		MongoCredential credential = MongoCredential.createCredential(user, "admin", "q1w2e3r4!".toCharArray());

		MongoClientOptions options = MongoClientOptions.builder()
		        .connectTimeout(10000)				//Default is 10,000
		        .maxConnectionIdleTime(600000)		//10 minutes
		        .maxConnectionLifeTime(1800000)		//30 minutes
		        .maxWaitTime(120000)				//Default is 120,000. The maximum wait time that a thread may wait for a connection to become available
		        .socketTimeout(0)				//Default is 0 and means no timeout. The socket timeout. It is used for I/O socket read and write operations
		        .build();

		
		int mongoPort = 27017;
		String[] hostPort = seed.split(":");
        if(hostPort.length == 2 && !StringUtils.isEmpty( hostPort[1] ))
        {
            mongoPort = Integer.parseInt(hostPort[1]);
        }
        ServerAddress serverAddress = new ServerAddress( hostPort[0], mongoPort );
		
        mongoClient = new MongoClient(
				serverAddress,
		        Arrays.asList(credential)
		        ,options
		        );
        
        try 
        {
			//just test the connection:
			MongoIterable<String> databaseNames = mongoClient.listDatabaseNames();
		} catch (Throwable th) {
			// TODO: handle exception
			throw new IOException("moshe");
		}
		return mongoClient;
	}
	
	public MongoClient getClient()
    {
        return mongoClient;
    }
}
