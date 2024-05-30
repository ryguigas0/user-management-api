package dev.guiga.proj1.user_management;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.nimbusds.jose.shaded.gson.Gson;

import dev.guiga.proj1.user_management.transfer.FailLoginTO;

@Service
public class FailLoginTopicService {
    @Value("${topic.name.producer}")
    private String topicName;

    private static final Logger log = LoggerFactory.getLogger(FailLoginTopicService.class);

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void send(FailLoginTO to) {
        Gson gson = new Gson();
        kafkaTemplate.send(topicName, gson.toJson(to));
        log.info("ENVIANDO MENSAGEM PARA TOPICO " + topicName);
    }
}