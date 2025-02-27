package com.example.demo;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
//@DynamoDbBean
//@see https://github.com/awspring/spring-cloud-aws/pull/957
public class CustomEntity {
	private String id;
	private String title;

	//@DynamoDbPartitionKey
	public String getId() {
		return id;
	}

	//@DynamoDbAttribute("title")
	public String getTitle() {
		return title;
	}


}
