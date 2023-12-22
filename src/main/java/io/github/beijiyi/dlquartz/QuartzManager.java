//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package io.github.beijiyi.dlquartz;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import org.quartz.Calendar;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.TriggerUtils;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.triggers.CronTriggerImpl;

public class QuartzManager {
    private static final String jobGroupName = "JOB_GROUP_NAME";
    private static final String triggerGroupName = "TRIGGER_GROUP_NAME";
    private static Scheduler sched;
    private static final SchedulerFactory sf = new StdSchedulerFactory();

    public QuartzManager() {
    }

    public static SchedulerFactory getSchedulerFactory() {
        return sf;
    }

    private static Scheduler getScheduler() throws SchedulerException {
        if (sched == null) {
            sched = sf.getScheduler();
        }

        return sched;
    }

    public static void addJob(String jobName, Class cls, String time) {
        try {
            JobDetail jobDetail = JobBuilder.newJob(cls).withIdentity(jobName, "JOB_GROUP_NAME").build();
            CronTrigger trigger = (CronTrigger)TriggerBuilder.newTrigger().withIdentity(jobName, "TRIGGER_GROUP_NAME").withSchedule(CronScheduleBuilder.cronSchedule(time)).build();
            Scheduler scheduler = getScheduler();
            scheduler.scheduleJob(jobDetail, trigger);
            if (!scheduler.isShutdown()) {
                scheduler.start();
            }

        } catch (Exception var6) {
            throw new RuntimeException(var6);
        }
    }

    public static void addJob(String jobName, String clsName, String time) {
        try {
            Class cls = Class.forName(clsName);
            addJob(jobName, cls, time);
        } catch (Exception var4) {
            throw new RuntimeException(var4);
        }
    }

    public static void removeJob(String jobName) throws SchedulerException {
        TriggerKey triggerKey = new TriggerKey(jobName, "TRIGGER_GROUP_NAME");
        JobKey jobKey = new JobKey(jobName, "JOB_GROUP_NAME");
        removeJob(triggerKey, jobKey);
    }

    public static void removeJob(TriggerKey triggerKey, JobKey jobKey) throws SchedulerException {
        Scheduler scheduler = getScheduler();
        scheduler.pauseTrigger(triggerKey);
        scheduler.unscheduleJob(triggerKey);
        scheduler.deleteJob(jobKey);
    }

    public static void updateJobTime(String jobName, String time) throws Exception {
        TriggerKey triggerKey = new TriggerKey(jobName, "TRIGGER_GROUP_NAME");
        Scheduler scheduler = getScheduler();
        CronTrigger trigger = (CronTrigger)scheduler.getTrigger(triggerKey);
        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(time);
        trigger = (CronTrigger)trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();
        scheduler.rescheduleJob(triggerKey, trigger);
    }

    public static void runJob(String jobName) throws Exception {
        Scheduler scheduler = getScheduler();
        JobKey jobKey = new JobKey(jobName, "JOB_GROUP_NAME");
        scheduler.triggerJob(jobKey);
    }

    public static Trigger getTrigger(String jobName) throws Exception {
        TriggerKey triggerKey = new TriggerKey(jobName, "TRIGGER_GROUP_NAME");
        new JobKey(jobName, "JOB_GROUP_NAME");
        return getTrigger(triggerKey);
    }

    public static Trigger getTrigger(TriggerKey triggerKey) throws Exception {
        Trigger trigger = getScheduler().getTrigger(triggerKey);
        return trigger;
    }

    public static JobDetail getJob(String jobName) throws Exception {
        new TriggerKey(jobName, "TRIGGER_GROUP_NAME");
        JobKey jobKey = new JobKey(jobName, "JOB_GROUP_NAME");
        JobDetail jobDetail = getScheduler().getJobDetail(jobKey);
        return jobDetail;
    }

    public Date getNextTime(String cron) {
        List<Date> ds = getNextTimes(cron, 1);
        return ds != null && ds.size() > 0 ? (Date)ds.get(0) : null;
    }

    public static List<Date> getNextTimes(String cron, int sum) {
        CronTriggerImpl cronTriggerImpl = new CronTriggerImpl();

        try {
            cronTriggerImpl.setCronExpression(cron);
        } catch (ParseException var4) {
            var4.printStackTrace();
            return null;
        }

        List<Date> dates = TriggerUtils.computeFireTimes(cronTriggerImpl, (Calendar)null, sum);
        return dates;
    }
}
