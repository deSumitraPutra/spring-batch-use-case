package sum.batch.msetl.etlitem;

public class EtlItemQuery {
    public static final String INITIATE_ETL =
        "UPDATE etl_item " +
        "SET " +
            "business_date = CAST(:#{#request.businessDate} AS DATE), " +
            "status = 'INITIATED', " +
            "start_time = GETDATE() AT TIME ZONE 'SE Asia Standard Time' AT TIME ZONE 'UTC', " +
            "updated_at = GETDATE() AT TIME ZONE 'SE Asia Standard Time' AT TIME ZONE 'UTC', " +
            "end_time = NULL, " +
            "duration = 0, " +
            "message = NULL " +
        "WHERE " +
            "job_name = :#{#request.jobName} AND " +
            "(status IS NULL OR CAST(status AS VARCHAR(100)) NOT IN ('INITIATED', 'RUNNING'))";
}
