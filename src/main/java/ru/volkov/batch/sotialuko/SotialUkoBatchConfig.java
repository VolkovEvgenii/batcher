package ru.volkov.batch.sotialuko;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.volkov.batch.sotialuko.tasklet.LinesReader;
import ru.volkov.batch.sotialuko.tasklet.LinesWriter;

@Configuration
@EnableBatchProcessing
public class SotialUkoBatchConfig {

    private JobBuilderFactory jobs;
    private StepBuilderFactory steps;

    public SotialUkoBatchConfig(JobBuilderFactory jobs, StepBuilderFactory steps) {
        this.jobs = jobs;
        this.steps = steps;
    }

    @Bean
    public LinesReader linesReader() {
        return new LinesReader();
    }

    @Bean
    public LinesWriter linesWriter() {
        return new LinesWriter();
    }

    @Bean
    protected Step readLines() {
        return steps
                .get("readLines")
                .tasklet(linesReader())
                .build();
    }

    @Bean
    protected Step writeLines() {
        return steps
                .get("writeLines")
                .tasklet(linesWriter())
                .build();
    }


}
