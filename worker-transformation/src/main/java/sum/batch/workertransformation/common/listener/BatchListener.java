package sum.batch.workertransformation.common.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ChunkListener;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@Slf4j
public class BatchListener implements ChunkListener {
    private Integer chunkIndex = 0;

    @Override
    public void beforeChunk(ChunkContext context) {
        chunkIndex++;
        log.info("🚀 Starting chunk #{}", chunkIndex);
    }

    @Override
    public void afterChunk(ChunkContext context) {
        StepExecution execution = context.getStepContext().getStepExecution();
        Long readCount = execution.getReadCount();
        Long writeCount = execution.getWriteCount();

        log.info("✅ Chunk #{} completed. Total read: {}, written: {}", chunkIndex, readCount, writeCount);
    }

    @Override
    public void afterChunkError(ChunkContext context) {
        String errorMessage = context.getStepContext()
            .getStepExecution()
            .getFailureExceptions()
            .stream()
            .map(Throwable::toString)
            .collect(Collectors.joining("; "));
        log.error("❌ Error occurred in chunk #{}. Reason: {}", chunkIndex, errorMessage);
    }
}
