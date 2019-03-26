package com.ohadr.ae_case_analysis.web;

import java.io.FileInputStream;
import java.io.IOException;

import javax.activation.MimetypesFileTypeMap;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ohadr.ae_case_analysis.service.MyService;


@Controller
@RequestMapping(value = "/properties")
public class PropertiesWebApi 
{
	
	@Autowired
	private MyService service;

	private static Logger log = Logger.getLogger(PropertiesWebApi.class);


	@ResponseBody
	@RequestMapping(method = RequestMethod.GET,
					produces = MediaType.APPLICATION_JSON_VALUE)
	public String getProperties(
//			@RequestParam("property") String property
			) throws IOException 
	{
		log.info("received GET:getProperties");
		
		String val = service.getStringValue();
		String response = /*JsonUtils.convertToJson*/(val);
		log.debug("extractions of caseId: " + response);

		return val;
	}

	@ResponseBody
	@RequestMapping(method = RequestMethod.POST,
					produces = MediaType.APPLICATION_JSON_VALUE)
	public void writeExtractionsMetricsToFile(
			@RequestParam("caseId") String caseId,
//			@RequestBody String json
			HttpServletResponse response) throws IOException 
	{
		log.info("received POST:extractions");
		
		String fileName = null;
		log.info("write extractions' metrics status: " + fileName);

        response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
        MimetypesFileTypeMap mimetypesFileTypeMap = new MimetypesFileTypeMap();
        String contentType = mimetypesFileTypeMap.getContentType(fileName);
		log.debug("contentType: " + contentType);
        response.setContentType(contentType);		//i expect "application/octet-stream"
        
        try (ServletOutputStream out = response.getOutputStream();
        		FileInputStream fileIS = new FileInputStream(fileName)) 
        {
        	IOUtils.copy(fileIS, out);
        }
	}
}
