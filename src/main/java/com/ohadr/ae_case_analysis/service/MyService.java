package com.ohadr.ae_case_analysis.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import com.morgan.design.properties.ReloadableProperty;


@Configuration
public class MyService {

	@ReloadableProperty("dynamicProperty.longValue")
	private long primitiveWithDefaultValue = 55;
	
	@ReloadableProperty("dynamicProperty.substitutionValue")
	private String stringProperty;
	
	@ReloadableProperty("dynamicProperty.compoiteStringValue")
	private String compsiteStringProperty;


	public String getStringValue()
	{
		return stringProperty;
	}
}
