package com.tahauddin.syed.configurations;

import com.tahauddin.syed.dto.Input;
import com.tahauddin.syed.dto.Output;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.core.step.tasklet.TaskletStep;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.json.JacksonJsonObjectMarshaller;
import org.springframework.batch.item.json.JacksonJsonObjectReader;
import org.springframework.batch.item.json.JsonFileItemWriter;
import org.springframework.batch.item.json.JsonItemReader;
import org.springframework.batch.item.json.builder.JsonFileItemWriterBuilder;
import org.springframework.batch.item.json.builder.JsonItemReaderBuilder;
import org.springframework.batch.item.support.PassThroughItemProcessor;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class JobConfigurations {

    private final JobBuilderFactory jobBuilderFactory;

    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job job(){

/*        Step stepOne = stepBuilderFactory.get("stepTwo").tasklet((stepContribution, chunkContext) -> {
            Map<String, Object> jobParameters = chunkContext.getStepContext().getJobParameters();
            Object name = jobParameters.get("name");
            log.info("Job Running ...");
            log.info("Name is :: {}", name);
            return RepeatStatus.FINISHED;
        }).build();*/

        return jobBuilderFactory.get("jobOne").start(step()).build();
    }

    @Bean
    public Step step()  {
        return stepBuilderFactory.get("stepOne")
                .chunk(1)
                .reader(reader())
                .processor(new PassThroughItemProcessor<>())
                /*.writer(list -> {
                    log.info("Writing Data to Log :: {}", list);
                    log.info("Writing Done !!");
                })*/
                .writer(writer())
                .build();
    }

    private JsonFileItemWriter<Object> writer() {

        Resource outputResource = new FileSystemResource("classpath:files/output.json");
        return new JsonFileItemWriterBuilder<>()
                .jsonObjectMarshaller(new JacksonJsonObjectMarshaller<>())
                .name("itemWriter")
                .resource(outputResource).build();
    }

    @Bean
    public JsonItemReader<Input> reader() {
        File file = null;
        try{
            file = ResourceUtils.getFile("classpath:files/input.json");
        } catch (FileNotFoundException exception){
            log.error("File Not Found");
        }
        return new JsonItemReaderBuilder<Input>()
                .jsonObjectReader(new JacksonJsonObjectReader<>(Input.class))
                .resource(new FileSystemResource(file))
                .name("jsonItemReader")
                .build();
    }


}
