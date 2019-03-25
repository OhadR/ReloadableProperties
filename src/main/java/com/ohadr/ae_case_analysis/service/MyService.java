package com.ohadr.ae_case_analysis.service;

import org.springframework.context.annotation.Configuration;

import com.morgan.design.properties.ReloadableProperty;


@Configuration
public class MyService {

	@ReloadableProperty("dynamicProperty.longValue")
	private long primitiveWithDefaultValue = 55;

	@ReloadableProperty("dynamicProperty.stringValue")
	private String stringValue;

//	@ReloadableProperty("dynamicProperty.substitutionProperty")
//	private String stringProperty;
//	
//	@ReloadableProperty("dynamicProperty.compoiteStringValue")
//	private String compsiteStringProperty;


	public String getStringValue()
	{
		return stringValue;
	}
}
