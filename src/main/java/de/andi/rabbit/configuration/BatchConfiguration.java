package de.andi.rabbit.configuration;


import de.andi.rabbit.domain.DataValue;
import de.andi.rabbit.processor.QueueToInfluxProcessor;
import de.andi.rabbit.writer.InfluxDBWriter;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.amqp.AmqpItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.PropertySource;

import javax.validation.constraints.NotNull;

@Configuration
@PropertySource("classpath:application.properties")
@EnableBatchProcessing
public class BatchConfiguration {
    private static final Logger LOGGER = LogManager.getLogger(BatchConfiguration.class.getName());

    @Value("${rabbitmq.queueName}")
    @NotNull
    String queueName;

    @Autowired
    private ConnectionFactory rabbitConnectionFactory;

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;


    @Bean
    public Job importUserJob(JobCompletionNotificationListener listener, Step step1) {
        LOGGER.debug("importUserJob...");
        return jobBuilderFactory.get("importUserJob")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(step1)
                .end()
                .build();
    }


    // this will create a new queue if it doesn't exist; otherwise, it'll use the existing one of the same name
    // ...the second argument means to make the queue 'durable'
    @Bean
    public Queue myQueue() {
        LOGGER.debug("init queue: {}", queueName);
        return new Queue(queueName, false);
    }
    // this is necessary for operations with Spring AMQP
    @Bean
    public RabbitTemplate getMyQueueTemplate() {
        RabbitTemplate template = new RabbitTemplate(this.rabbitConnectionFactory);
        template.setQueue(queueName);
        return template;
    }

    @Bean
    public Step getMyJobStep() {
        LOGGER.debug("getMyJobStep...");
        return this.stepBuilderFactory.get("myJobStep")
                .<String, DataValue>chunk(100)
                .reader(this.getMyReader())
                .processor(this.getMyProcessor())
                .writer(this.getMyWriter())
                .build();
    }
    @Bean
    public ItemReader<String> getMyReader() {
        return new AmqpItemReader<String>(this.getMyQueueTemplate());
    }
    @Bean
    public ItemProcessor<Object, DataValue> getMyProcessor() {
        return new QueueToInfluxProcessor();
    }
    @Bean
    public ItemWriter<DataValue> getMyWriter() {
        return new InfluxDBWriter();
    }
}
