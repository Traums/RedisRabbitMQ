package org.example.rabbitreddis.rabbit;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.example.rabbitreddis.domain.CrazyTask;
import org.springframework.amqp.rabbit.core.RabbitMessagingTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Log4j2
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Component
public class CrazyTaskSender {
    RabbitMessagingTemplate rabbitMessagingTemplate;
    public void sendCrazyTask(CrazyTask payload){
        rabbitMessagingTemplate.convertAndSend(
                CrazyTaskListener.CRAZY_TASKS_EXCHANGE,
                null,
                payload);
    }
}
