package org.example.rabbitreddis.services;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.example.rabbitreddis.configs.RedisLock;
import org.example.rabbitreddis.domain.CrazyTask;
import org.example.rabbitreddis.rabbit.CrazyTaskSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.UUID;
@Log4j2
@RequiredArgsConstructor
@Service
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class CrazyTaskInitializerService {
    RedisLock redisLock;
    CrazyTaskSender crazyTaskSender;
    public static final String SERVER_ID = generateShortId();

    private static String generateShortId() {
        return UUID.randomUUID().toString().substring(0, 4);
    }

    public static final long ONE_MINUTE_IN_MILLIS = 1000 * 60;
    public static final String GENERATE_CRAZY_TASKS_KEY = "generate:crazy:tasks";
    @Scheduled(cron = "0/15 * * * * *") // Запуск каждую секунду
    public void generateCrazyTasks() {

        if(redisLock.acquireLock(ONE_MINUTE_IN_MILLIS,GENERATE_CRAZY_TASKS_KEY)){

//            redisLock.releaseLock(GENERATE_CRAZY_TASKS_KEY);
            log.info(String.format("Server \"%s\" start generate tasks",SERVER_ID));
            for (int i = 0; i < 100; i++) {
                crazyTaskSender.sendCrazyTask(
                        CrazyTask.builder()
                                .id(generateShortId())
                                .fromServer(SERVER_ID)
                                .build()
                );
            }
        }
    }
}
