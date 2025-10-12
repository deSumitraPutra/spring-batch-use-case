# Basic Migration

## Summary
In this case I want to copy data from a database source 
to my team database.

## Setup
- Database connector: JDBC only
- Spring Batch Metadata: H2
- Databases: Oracle(source), SQL Server(destination)
- Initiator: Kafka Listener
- Executor: ThreadPoolTaskExecutor
- Kafka topic: 'ETL' with only one partition(well for now)

## Flow
1. A service called ms-etl has API `POST /etls` that can send an ETL requests 
to kafka with topic 'ETL'.
2. A consumer called worker-staging-data listen to 'ETL' topic. And convert the message
into POJO.
3. Listener method find a job based on message received and launch the job using JobLauncher instance.
4. A job launched!. First step set status of EtlItem into RUNNING.
5. Next, read data from source, map the result and write to destination by chunking.
6. A listener was set to log each chunk activity.
7. A job listener was set and has functionalities:
   - beforeJob: find out how many row will be processed and set the result into job parameters.
   - afterJob: set EtlItem based on the result(SUCCESS/FAILED), record EtlItem into history table and send notification via kafka.

## What Did I Find Out
1. In order to connect spring boot app with oracle database you need the schema name as
username. You cannot just use the highest privileged user.
2. Don't declare a step as bean if you plan to make it re-usable.
3. By default, JobLauncher is run synchronously. So you need to configure asynchronous JobLauncher especially 
when there are multiple kafka message arrive.
4. H2 is not recommended for Spring Batch metadata datasource, because it's in-memory database.
