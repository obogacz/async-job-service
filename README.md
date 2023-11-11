# Async Job Service

## General info

Async Job Service was created as the module of the SpringBoot application and supports processing actions in asynchronous jobs. If a response to an action is not needed immediately and the action can be processed in the background, then Async Job Service will process it, and store input parameters and a prepared result in a DB.

Async Job Service needs a connection to DB as it stores a record for each corresponding action, its input params and its summary. The service provides the REST controller and a bunch of methods by which you can manage your tasks.

The module was created in response to a need to process simple tasks in the background and keep their parameters for later reading/analysis. All already existing solutions deliver too many features for that simple need. But later more requirements appeared and this service was extended with new functionalities.

## Technologies

- [Java 21](https://openjdk.org/projects/jdk/21/)
- [SpringBoot 3](https://spring.io/projects/spring-boot)
- [H2 Database](https://www.h2database.com/html/main.html)
- [Lombok](https://projectlombok.org)

## Setup

The App was built with SpringBoot 3 and uses in-memory H2 Database to run simple examples. All you need to run the demo app is to run this package as a common SpringBoot app.

The Async Job Service is placed in the `com.richcode.job` package and contains all components which are needed to run the custom logic in an async job. Besides that, the app contains the `com.richcode.foo` package which contains a simple domain `Foo` to show an example usage of the Async Job Service.

The app exposes the Swagger UI under http://localhost:8080/swagger-ui/index.html which gives some opportunities to play around with the implemented functionalities. It provides possibilities to run and re-trigger a simple job, modify the thread pool configuration and get summaries of the async jobs.

## Configuration and usage

The general concept of the Async Job Service is to provide tools to:

- Run a custom logic in a thread (job).
- Monitor the job execution status.
- Separate an inner job logic from its processing.
- Store the input parameters of the job.
- Store the output data of the job if it is needed.
- Allow to re-trigger an already finished job with the same input parameters.

The above points require to creation of some custom components with a usage of definitions delivered by the Async Job Service module. The first is the **Async Job Summary service**. It provides a possibility to store and monitor a summary of a processing job. The second component is the **Async Job Runner** in which we create a logic of a process delegated to be processed asynchronously.

### Async Job Summary

The Async Job Service uses an object of the `AsyncJobSummary` type to monitor and manage the state of asynchronous actions. The summary object contains:
- `contextId` which is delegated to store an identifier of an object in which context we run the action. If we run an import of city inhabitants for a town, it should store the identifier of the town.
- `type` is a job type. It provides information about the purpose/responsibility of the job.
- `status` is the current state of a processing job. Currently, they are 4: QUEUED, PENDING, SUCCESS, FAILURE.
- `username` is a user identifier of a system user who ran the job.
- `parameters` is a field which stores job input parameters in JSON.
- `result` is a field which stores a result/summary of the job in JSON.
- `created` a timestamp when the Async Job Summary was created so when the job was run.
- `updated` a timestamp when the summary was updated.

### Async Job Summary Service

The Async Job Summary Service provides a bunch of methods to manage the state of our async job and job summaries. To create this component we have to inherit from the `AbstractAsyncJobSummaryService` delivered by the Async Job Service. It is the abstract generic class which has methods to create an async job summary, update it and fetch it from a database.

An example of inheritance is available below. By creating it, we also defined the classes which represent params and a result of the job and its type. Classes of the params and results have to implement the `AsyncJobParams` and `AsyncJobResult` interfaces. You can also use the basic implementation of them: `BacisAsyncJobParams` and `BasicAsyncJobResult`.

Because the lifecycle of our job can be different per the job type, `AbstractAsyncJobSumaryService` has methods to create and update the summary and they consume the status into which we would like to turn our job summary state. According to this idea, we can create our summary state lifecycle by exposing the right methods in our summary service as was done below.

The `FooAsyncJobSummaryService` created a summary in the QUEUED status and can turn it into PENDING, SUCCESS or FAILURE. It is a very common and basic implementation but without any obstacles, it can be easily extended.

```java

@Service
public class FooAsyncJobSummaryService extends AbstractAsyncJobSummaryService<FooAsyncJobParams, AsyncJobResult> {

    @Autowired
    public FooAsyncJobSummaryService(AsyncJobSummaryRepository repository) {
        super(repository, AsyncJobType.JOB_TYPE_1, FooAsyncJobParams.class, AsyncJobResult.class);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public AsyncJobSummaryDto<FooAsyncJobParams, AsyncJobResult> queued(FooAsyncJobParams params) {
        return super.create(params.contextId(), AsyncJobStatus.QUEUED, params, null);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void pending(Long jobId) {
        super.update(jobId, AsyncJobStatus.PENDING, null);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void success(Long jobId) {
        super.update(jobId, AsyncJobStatus.SUCCESS, BasicAsyncJobResult.completed());
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void failure(Long jobId, String msg) {
        super.update(jobId, AsyncJobStatus.FAILURE, BasicAsyncJobResult.failure(msg));
    }

}
```

### Async Job Runner

To run your first asynchronous action with the Async Job Service, you need to declare your Async Job Runner. The Async Job Runner wraps your custom logic of the asynchronous action and manages the state of your job. It creates the summary, updates it and submits the inner async action. These are its responsibilities.

It has to be declared as a Spring Bean, implements the `AsynJobRunnable` interface and has the `@AsyncJobRunner` annotation over the class with your Async Job Type. The Job Type is an enum value by which you can identify the type/responsibility of the job.

Here is the example of the `FooAsyncJobRunner` declared as a Spring Bean which waits for 10s as the async action. In many cases, runners will look the same. they will have the same shape of managing the summary state but in a few cases, it could be needed to do some checks to not run a duplicated job in terms of the same params or type. This is what the Async Job Runner should do. It produces sometimes boilerplate code but I met many cases of managing the lifecycle of the job that in the end this was the best option.

```java

@Slf4j
@Component
@RequiredArgsConstructor
@AsyncJobRunner(AsyncJobType.JOB_TYPE_1)
public class FooAsyncJobRunner implements AsyncJobRunnable {

    private final AsyncJobProcessor processor;
    private final FooAsyncJobSummaryService summaryService;

    @Override
    public Long run(String parametersJson) {
        return run(AsyncJobSummaryJsonMapper.fromJson(parametersJson, FooAsyncJobParams.class));
    }

    public Long run(FooAsyncJobParams params) {
        final AsyncJobSummaryDto<FooAsyncJobParams, AsyncJobResult> job = summaryService.queued(params);
        try {
            processor.submit(() -> process(job));
        } catch (Throwable throwable) {
            summaryService.failure(job.id(), throwable.getMessage());
            log.error("[ jobId={} ] Job failed due to: {}", job.id(), throwable.getMessage());
        }
        return job.id();
    }

    private void process(AsyncJobSummaryDto<FooAsyncJobParams, AsyncJobResult> job) {
        log.info("Started processing job [id={}, type={}, contextId={}]", job.id(), job.type(), job.contextId());
        try {
            summaryService.pending(job.id());

            Thread.sleep(10_000L); // logic to process

            summaryService.success(job.id());
            log.info("Finished processing job [id={}, type={}, contextId={}]", job.id(), job.type(), job.contextId());
        } catch (Throwable throwable) {
            summaryService.failure(job.id(), throwable.getMessage());
            log.error("Job failed due to: {} [id={}, type={}, contextId={}]",
                throwable.getMessage(), job.id(), job.type(), job.contextId());
        }
    }

}

```

Async Job Service has the register of all declared Async Job Runners declared to each Async Job Type. It scans the whole project and finds Spring Beans which were declared as a runner. Then it is easier to run an action using only the type of the job and input params that deliver e.g. the `AsyncJobService`.

### Custom Thread Pool Configuration

The basic implementation of Async Job Service uses only one thread pool under the hood. It is configurable via `application.yaml` and is managed by the `AsyncJobProcessor`. Definitions of thread pools are stored in the `AsyncJobConfiguration` and they are declared as Spring Beans.

If you would like to have a dedicated thread pool for the specific Async Job Type, these are the places where you should put it and manage it.

```java
@Component
public class AsyncJobProcessor {

    private final ThreadPoolTaskExecutor commonTaskExecutor;
    private final ThreadPoolTaskExecutor myCustomTaskExecutor; // custom thread pool

    @Autowired
    public AsyncJobProcessor(@Qualifier("commonAsyncJobExecutor") ThreadPoolTaskExecutor commonTaskExecutor) {
        this.commonTaskExecutor = commonTaskExecutor;
    }

    public void submit(@NonNull Runnable job) {
        commonTaskExecutor.execute(job);
    }

    // Submit to the custom thread pool
    public void submitMyCustomJob(@NonNull Runnable job) {
        myCustomTaskExecutor.execute(job);
    }

    public AsyncJobConfigurationDto getConfiguration() {
        return AsyncJobConfigurationDto.builder()
            .commonExecutorCorePoolSize(commonTaskExecutor.getCorePoolSize())
            .commonExecutorMaxPoolSize(commonTaskExecutor.getMaxPoolSize())
            .build();
    }

    public AsyncJobConfigurationDto updateConfiguration(AsyncJobConfigurationDto config) {
        commonTaskExecutor.setCorePoolSize(config.commonExecutorCorePoolSize());
        commonTaskExecutor.setMaxPoolSize(config.commonExecutorMaxPoolSize());
        return getConfiguration();
    }

}
```

## TODO List

- Store a context of application node which runs an async job
  - When application node is stopped, the current implementation wants to re-trigger unfinished job at the next start.
  - With multiple nodes of application, it could happen that one node will remove a summary object of a job processing by the other node.
  - Check the logic of the `AsyncJobNecromancer` for more context.
- Mechanism of not accepting further jobs for processing
  - It should be possible to stop submitting the next jobs until that signal is not removed.  

## Code examples

### Async jobs executor
```java
@Service
@RequiredArgsConstructor
public class FooAsyncJobExecutor {

    private final FooAsyncJobRunner fooAsyncJobRunner;

    public Long run(FooAsyncJobParams params) {
        return fooAsyncJobRunner.run(params);
    }

}
```

### Execution of async jobs
```java
@RestController
@RequestMapping("/v1/foo")
@RequiredArgsConstructor
public class FooController {

    private final FooAsyncJobExecutor executor;

    @PostMapping(value = "/run", produces = APPLICATION_JSON_VALUE)
    public AsyncJobApiResponse rerun() {
        executor.run(FooAsyncJobParams.builder()
            .contextId("1")
            .username("me")
            .fooParam("foo")
            .build());
        return AsyncJobApiResponse.ok();
    }

}
```

### Custom Async Job input parameter
```java
@Builder
public record FooAsyncJobParams(
    String contextId,
    String username,
    String fooParam
) implements AsyncJobParams {
}
```