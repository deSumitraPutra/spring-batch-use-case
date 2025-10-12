package sum.batch.workerstagingdata.common.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class JobName {
    public static final String PRODUCT = "PRODUCT_STAGING_JOB";
    public static final String TRANSACTION = "TRANSACTION_STAGING_JOB";
    public static final String PRODUCT_TRANSACTION = "PRODUCT_TRANSACTION_STAGING_JOB";
}
