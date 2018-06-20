package com.systelab.dynamodb;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;

public class DynamoDBManager {

    AmazonDynamoDB client;
    DynamoDB dynamoDB;

    public DynamoDBManager(boolean isLocal) {
        if (isLocal) {
            client = AmazonDynamoDBClientBuilder.standard().withEndpointConfiguration(
                    new AwsClientBuilder.EndpointConfiguration("http://localhost:8000", "eu-central-1"))
                    .build();
        } else {
            client = AmazonDynamoDBClientBuilder.standard()
                    .withRegion(Regions.EU_CENTRAL_1)
                    .build();
        }
        dynamoDB = new DynamoDB(client);
    }

}
