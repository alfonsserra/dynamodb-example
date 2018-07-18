package com.systelab.dynamodb;

import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.TableCollection;
import com.amazonaws.services.dynamodbv2.model.*;

import java.util.ArrayList;
import java.util.List;

public class DynamoDBTableManager extends DynamoDBManager {

    public DynamoDBTableManager(boolean isLocal) {
        super(isLocal);
    }

    public void getTables() {
        try {
            TableCollection<ListTablesResult> tables = dynamoDB.listTables();
            tables.forEach(System.out::println);
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
        DynamoDBTableManager tableManagement = new DynamoDBTableManager(true);
        tableManagement.createTable("ProductCatalog", 1L, 1L);
    }
}
