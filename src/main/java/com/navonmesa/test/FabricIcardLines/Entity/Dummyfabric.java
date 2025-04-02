package com.realnet.FabricIcardLines.Entity;

import javax.persistence.Lob;

import lombok.Data;

@Data
public class Dummyfabric {


	@Lob
	private String json;

	@Lob
	private String xml;

	

}