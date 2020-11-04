package ru.volkov.batch.sotialuko.tasklet;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

public class LinesReader implements Tasklet {

    @Override
    public RepeatStatus execute(
            StepContribution stepContribution,
            ChunkContext chunkContext) throws Exception {
        return null;
    }
}
