# Spring Batch Use Case

## Background
Here I try to explore the feature of Spring Batch. I also want to explore some use case that can solved with Spring Batch.
Maybe in the future I'll also explore about the performance of my implementation here. The reason behind this exploration is
that my team struggle to utilize SSIS of SQL server database for processing data. Not because of setting up the SSIS but there is a
potential bottleneck that could happen.

## What I'm About To Explore
1. Basic data migration. [Coming Soon!]()
   - Table A to table B same db
   - Table A to table A same dialect different instance
   - Table A to table A different dialect
2. Asynchronous migration. [Coming Soon!]()
3. Data transformation by separating transformation load. [Coming Soon!]()
   - Transformation in different DB
   - Strict job queueing using additional table
   - String job queueing using kafka
