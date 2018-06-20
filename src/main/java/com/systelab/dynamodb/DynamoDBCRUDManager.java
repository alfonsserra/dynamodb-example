package com.systelab.dynamodb;

import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;

import java.util.Arrays;
import java.util.HashSet;

public class DynamoDBCRUDManager extends DynamoDBManager {

    public DynamoDBCRUDManager(boolean isLocal) {
        super(isLocal);
    }

    public void createItem(String tableName) {

        Table table = dynamoDB.getTable(tableName);
        try {

            Item item = new Item().withPrimaryKey("Id", 120)
                    .withString("Title", "Book 120 Title")
                    .withString("ISBN", "120-1111111111")
                    .withStringSet("Authors", new HashSet<String>(Arrays.asList("Author12", "Author22")))
                    .withNumber("Price", 20).withString("Dimensions", "8.5x11.0x.75").withNumber("PageCount", 500)
                    .withBoolean("InPublication", false).withString("ProductCategory", "Book");
            table.putItem(item);

        }
        catch (Exception e) {
            System.err.println("Create items failed.");
            System.err.println(e.getMessage());

        }
    }

    public static void main(String[] args) {
        DynamoDBCRUDManager crudManager = new DynamoDBCRUDManager(true);
        crudManager.createItem("ProductCatalog");
    }
}
