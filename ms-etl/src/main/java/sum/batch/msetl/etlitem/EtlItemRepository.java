package sum.batch.msetl.etlitem;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sum.batch.etlcore.dto.EtlRequestDTO;
import sum.batch.etlcore.entity.EtlItem;

@Repository
public interface EtlItemRepository extends JpaRepository<EtlItem, Long> {

    @Modifying
    @Transactional
    @Query(value = EtlItemQuery.INITIATE_ETL, nativeQuery = true)
    void initiate(@Param("request") EtlRequestDTO extractionRequestDTO);

}
