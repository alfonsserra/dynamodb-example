package com.systelab.dynamodb;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.TableCollection;
import com.amazonaws.services.dynamodbv2.model.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DynamoDBTableManagement {

    AmazonDynamoDB client;
    DynamoDB dynamoDB;

    public DynamoDBTableManagement(boolean isLocal) {
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

    public void getTables() {
        try {
            TableCollection<ListTablesResult> tables = dynamoDB.listTables();
            Iterator<Table> iterator = tables.iterator();

            while (iterator.hasNext()) {
                Table table = iterator.next();
                System.out.println(table.getTableName());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public boolean createTable(String tableName, Long readCapacityUnits, Long writeCapacityUnits) {
        try {
            List<AttributeDefinition> attributeDefinitions = new ArrayList<AttributeDefinition>();
            attributeDefinitions.add(new AttributeDefinition().withAttributeName("Id").withAttributeType("N"));

            List<KeySchemaElement> keySchema = new ArrayList<KeySchemaElement>();
            keySchema.add(new KeySchemaElement().withAttributeName("Id").withKeyType(KeyType.HASH));

            CreateTableRequest request = new CreateTableRequest()
                    .withTableName(tableName)
                    .withKeySchema(keySchema)
                    .withAttributeDefinitions(attributeDefinitions)
                    .withProvisionedThroughput(new ProvisionedThroughput()
                            .withReadCapacityUnits(readCapacityUnits)
                            .withWriteCapacityUnits(writeCapacityUnits));

            Table table = dynamoDB.createTable(request);

            table.waitForActive();
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean updateTable(String tableName, Long readCapacityUnits, Long writeCapacityUnits) {
        try {
            Table table = dynamoDB.getTable(tableName);
            ProvisionedThroughput provisionedThroughput = new ProvisionedThroughput()
                    .withReadCapacityUnits(readCapacityUnits)
                    .withWriteCapacityUnits(writeCapacityUnits);
            table.updateTable(provisionedThroughput);

            table.waitForActive();
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean deleteTable(String tableName) {
        try {
            Table table = dynamoDB.getTable(tableName);

            table.delete();
            table.waitForDelete();
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    public static void main(String[] args) {
        DynamoDBTableManagement tableManagement = new DynamoDBTableManagement(true);
        tableManagement.createTable("Table1",1L,1L);
    }
}
