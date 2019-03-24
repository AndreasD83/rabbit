package de.andi.rabbit.configuration;

import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class RunScheduler {
    private static final Logger LOGGER = LogManager.getLogger(RunScheduler.class.getName());
    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Job job;

    @Scheduled(fixedDelay = 3600000)
    public void run() {

        try {
            String dateParam = new Date().toString();
            LOGGER.debug("run with date {}...", dateParam);

            JobParameters param =
                    new JobParametersBuilder().addString("date", dateParam).toJobParameters();


            JobExecution execution = jobLauncher.run(job, param);
            LOGGER.debug("Exit Status : {}, Job {}", execution.getStatus(), execution.getJobId());

        } catch (Exception e) {
            LOGGER.error(e);
        }

    }
}
