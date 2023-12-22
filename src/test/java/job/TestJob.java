//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestJob implements Job {
    private Logger log = LoggerFactory.getLogger(TestJob.class);

    public TestJob() {
    }

    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        JobKey key = jobExecutionContext.getJobDetail().getKey();
        System.out.println(key.getGroup() + "   " + key.getName());
        this.log.info("执行了！");
        System.out.println("执行了！");
    }
}
