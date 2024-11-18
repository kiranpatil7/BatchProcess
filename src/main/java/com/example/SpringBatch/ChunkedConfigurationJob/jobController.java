package com.example.SpringBatch.ChunkedConfigurationJob;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/invoke"})
public class jobController {

    @Autowired
    JobLauncher jobLauncher;
    @Autowired
    Job firstJob;

    @GetMapping({"startJob/{jobName}"})
    public String handle(@PathVariable String jobName) throws Exception {
        JobParameters jobParameters = (new JobParametersBuilder()).addLong("time", System.currentTimeMillis()).toJobParameters();
        if (jobName.equals("job2")) {
            this.jobLauncher.run(this.firstJob, jobParameters);
        }

        return "Batch job has been invoked";
    }
}
