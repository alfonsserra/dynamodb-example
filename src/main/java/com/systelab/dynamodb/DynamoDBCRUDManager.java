package com.systelab.dynamodb;

import com.amazonaws.services.dynamodbv2.document.DeleteItemOutcome;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.UpdateItemOutcome;
import com.amazonaws.services.dynamodbv2.document.spec.DeleteItemSpec;
import com.amazonaws.services.dynamodbv2.document.spec.UpdateItemSpec;
import com.amazonaws.services.dynamodbv2.document.utils.NameMap;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.amazonaws.services.dynamodbv2.model.ReturnValue;

public class DynamoDBCRUDManager extends DynamoDBManager {

    public DynamoDBCRUDManager(boolean isLocal) {
        super(isLocal);
    }

    public void createItem(String tableName, String json) {

        Table table = dynamoDB.getTable(tableName);
        try {
            Item item = new Item().withPrimaryKey("Id", 120)
                    .withJSON("Representation", json);
            table.putItem(item);
        } catch (Exception e) {
            System.err.println("Create items failed.");
            System.err.println(e.getMessage());
        }
    }

    public void retrieveItem(String tableName, Object id) {
        Table table = dynamoDB.getTable(tableName);

        try {
            Item item = table.getItem("Id", id, "Representation", null);

            System.out.println("Printing item after retrieving it....");
            System.out.println(item.getJSON("Representation"));
        } catch (Exception e) {
            System.err.println("GetItem failed.");
            System.err.println(e.getMessage());
        }
    }

    public void updateAddNewAttribute(String tableName, Object id, String json) {
        Table table = dynamoDB.getTable(tableName);

        try {

            UpdateItemSpec updateItemSpec = new UpdateItemSpec().withPrimaryKey("Id", id)
                    .withUpdateExpression("set #na = :val1").withNameMap(new NameMap().with("#na", "Representation"))
                    .withValueMap(new ValueMap().withJSON(":val1", json)).withReturnValues(ReturnValue.ALL_NEW);

            UpdateItemOutcome outcome = table.updateItem(updateItemSpec);

            // Check the response.
            System.out.println("Printing item after adding new attribute...");
            System.out.println(outcome.getItem().getJSON("Representation"));

        } catch (Exception e) {
            System.err.println("Failed to add new attribute in " + tableName);
            System.err.println(e.getMessage());
        }
    }

    public void deleteItem(String tableName, Object id) {

        Table table = dynamoDB.getTable(tableName);

        try {

            DeleteItemSpec deleteItemSpec = new DeleteItemSpec().withPrimaryKey("Id", id).withReturnValues(ReturnValue.ALL_OLD);

            DeleteItemOutcome outcome = table.deleteItem(deleteItemSpec);

            // Check the response.
            System.out.println("Printing item that was deleted...");
            System.out.println(outcome.getItem().getJSON("Representation"));

        } catch (Exception e) {
            System.err.println("Error deleting item in " + tableName);
            System.err.println(e.getMessage());
        }
    }

    public static void main(String[] args) {
        DynamoDBCRUDManager crudManager = new DynamoDBCRUDManager(true);
        String json = "{\n" +
                "    \"name\":\"John\",\n" +
                "    \"age\":30,\n" +
                "    \"cars\": [\n" +
                "        { \"name\":\"Ford\", \"models\":[ \"Fiesta\", \"Focus\", \"Mustang\" ] },\n" +
                "        { \"name\":\"BMW\", \"models\":[ \"320\", \"X3\", \"X5\" ] },\n" +
                "        { \"name\":\"Fiat\", \"models\":[ \"500\", \"Panda\" ] }\n" +
                "    ]\n" +
                " }";

        String json1 = "{\n" +
                "\"name\":\"John\",\n" +
                "\"age\":30,\n" +
                "\"cars\":[ \"Ford\", \"BMW\", \"Fiat\" ]\n" +
                "}";

        crudManager.createItem("ProductCatalog", json);
        crudManager.retrieveItem("ProductCatalog", 120);
        crudManager.updateAddNewAttribute("ProductCatalog", 120, json1);
        crudManager.retrieveItem("ProductCatalog", 120);

        crudManager.deleteItem("ProductCatalog", 120);
    }
}
