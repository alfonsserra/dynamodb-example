# DynamoDB example

## Download DynamoDB

The [downloadable version of DynamoDB](https://docs.aws.amazon.com/amazondynamodb/latest/developerguide/DynamoDBLocal.html) is provided as an executable .jar file. The application runs on Windows, Linux, macOS X, and other platforms that support Java.

## Setup the credentials

```bash
aws configure
```

And setup your **AWS Access Key ID**, **AWS Secret Access Key**, **Default region name** for example eu-central-1, and the **Default output format**, for example json.

## Run DynamoDB

```bash
java -Djava.library.path=./DynamoDBLocal_lib -jar DynamoDBLocal.jar -sharedDb
```

### Command Line Options
You can use the following command line options with the downloadable version of DynamoDB:

-help — Prints a usage summary and options.

-cors value — Enables support for cross-origin resource sharing (CORS) for JavaScript. You must provide a comma-separated "allow" list of specific domains. The default setting for -cors is an asterisk (*), which allows public access.

-dbPath value — The directory where DynamoDB writes its database file. If you don't specify this option, the file is written to the current directory. You can't specify both -dbPath and -inMemory at once.

-delayTransientStatuses — Causes DynamoDB to introduce delays for certain operations. DynamoDB (Downloadable Version) can perform some tasks almost instantaneously, such as create/update/delete operations on tables and indexes. However, the DynamoDB service requires more time for these tasks. Setting this parameter helps DynamoDB running on your computer simulate the behavior of the DynamoDB web service more closely. (Currently, this parameter introduces delays only for global secondary indexes that are in either CREATING or DELETING status.)

-inMemory — DynamoDB runs in memory instead of using a database file. When you stop DynamoDB, none of the data is saved. You can't specify both -dbPath and -inMemory at once.

-optimizeDbBeforeStartup — Optimizes the underlying database tables before starting DynamoDB on your computer. You also must specify -dbPath when you use this parameter.

-port value — The port number that DynamoDB uses to communicate with your application. If you don't specify this option, the default port is 8000.

-sharedDb — If you specify -sharedDb, DynamoDB uses a single database file instead of separate files for each credential and region.

## Working with the local DynamoDB

Get the tables:

```bash
aws dynamodb list-tables --endpoint-url http://localhost:8000
```
