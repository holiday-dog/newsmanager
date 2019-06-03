package com.code.spider.executors;

import com.code.spider.plugin.ClientPlugin;
import com.code.spider.plugin.XinhuaEduPlugin;
import com.code.spider.plugin.XinhuaSciencePlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

@Component
public class MainExecutor implements ApplicationRunner {
    private static ThreadFactory threadFactory = new NameThreadFactory();
    private ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(10, threadFactory);
    private static final int period = 24 * 3600;
    private static final String timedExpression = "18:05:00";
    private Logger logger = LoggerFactory.getLogger(MainExecutor.class);

    public void executor() {
        Long initDelay = 0L;
        initDelay = initDelayTime() / 1000;
        logger.info("{} seconds later start crawling", initDelay);

        List<ClientPlugin> clientPlugins = new ArrayList<>();
        clientPlugins.add(new XinhuaEduPlugin());
        clientPlugins.add(new XinhuaSciencePlugin());
//        clientPlugins.add(new XinhuaRecommendPlugin());
//        clientPlugins.add(new XinhuaTravelPlugin());
//        clientPlugins.add(new RenminRecommendPlugin());
//        clientPlugins.add(new RenminTravelPlugin());
//        clientPlugins.add(new RenminEduPlugin());

        for (ClientPlugin clientPlugin : clientPlugins) {
            executorService.scheduleAtFixedRate(new Runnable() {
                @Override
                public void run() {
                    clientPlugin.spiderProcess(null);
                }
            }, initDelay, 10, TimeUnit.SECONDS);
        }
    }

    public static Long initDelayTime() {
        String[] times = timedExpression.split(":");
        if (times == null && times.length < 3) {
            return 0L;
        }
        LocalDateTime timed = LocalDateTime.of(LocalDate.now(), LocalTime.parse(timedExpression));
        LocalDateTime nowTime = LocalDateTime.now();

        if (nowTime.isEqual(timed)) {
            return 0L;
        } else if (nowTime.isBefore(timed)) {
            return Duration.between(nowTime, timed).toMillis();
        } else if (nowTime.isAfter(timed)) {
            timed = timed.plusDays(1);
            return Duration.between(nowTime, timed).toMillis();
        } else {
            return 0L;
        }
    }

    @Override
    public void run(ApplicationArguments args) {
        logger.info("---------------");
        executor();
    }
}
