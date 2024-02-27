package org.example.rabbitreddis.rabbit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.example.rabbitreddis.domain.CrazyTask;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Log4j2
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Component
public class CrazyTaskListener {
    ObjectMapper objectMapper;
    public static final String CRAZY_TASKS_QUEUE = "may.code.crazy.tasks.queue";
    public static final String CRAZY_TASKS_EXCHANGE = "may.code.crazy.tasks.exchange";
    @SneakyThrows
    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(value = CRAZY_TASKS_QUEUE),
                    exchange = @Exchange(value = CRAZY_TASKS_EXCHANGE)
            )
    )
    public void handleCrazyTask(CrazyTask crazyTask){
        log.info("Получено сообщение" + objectMapper.writeValueAsString(crazyTask));
        Thread.sleep(100);
    }
}
