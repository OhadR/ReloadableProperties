package com.ohadr.ae_case_analysis.web;

import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ohadr.ae_case_analysis.repository.AERepository;
import com.ohadr.common.utils.JsonUtils;

@Controller
@RequestMapping(value = "/connectToMongoServerAndGetCases")
public class CasesWebApi 
{
	@Autowired
	private AERepository storage;
	
	private static Logger log = Logger.getLogger(CasesWebApi.class);


	@ResponseBody
	@RequestMapping(method = RequestMethod.POST,
					produces = MediaType.APPLICATION_JSON_VALUE)
	public String connectToMongoServerAndGetCases(
            @RequestParam("serverAddress") String serverAddress			// <-- this is relevant for "url-form-encoded"
//			@RequestBody String json
			) throws IOException 
	{
		log.debug("received POST:connectToMongoServerAndGetCases");
		
		storage.connectToMongoServer(serverAddress);

		List<Document> cases = storage.getAllCases();
		String response = JsonUtils.convertToJson(cases);
		log.info("response as json: " + response);

		return response;
	}
}
