# Basic Migration

## Summary
In this case I want to copy some table from main database into worker database, run
stored procedure in worker database and write back to main database the result. Of course this 
includes side effect like error, notification and database flagging.

This pattern allow main database to focus on serving data to client while worker database can run
heavy query or aggregation.

## Setup
- Database connector: JDBC only
- Spring Batch Metadata: H2
- Databases: main-database(sql-server), worker-database(sql-server)
- Initiator: Kafka Listener
- Executor: ThreadPoolTaskExecutor(dynamic async)
- Kafka topic: 'TRANSFORM' with only one partition(well for now)

## Flow
1. A service called ms-etl has API `POST /transforms` that send requests 
to kafka with topic 'TRANSFORM'.
2. A consumer called worker-transformer listen to 'TRANSFORM' topic. And convert the message
into POJO.
3. Listener method find a job based on message received and launch the job using JobLauncher instance.
4. A job launched!. First step set status of EtlItem into RUNNING.
5. Next, read data from source, map the result and write to destination by chunking.
6. A listener was set to log each chunk activity.
7. A job listener was set and has functionalities:
   - beforeJob: find out how many row will be processed and set the result into job parameters.
   - afterJob: set EtlItem based on the result(SUCCESS/FAILED), record EtlItem into history table and send notification via kafka.

## What Did I Find Out
1. 
