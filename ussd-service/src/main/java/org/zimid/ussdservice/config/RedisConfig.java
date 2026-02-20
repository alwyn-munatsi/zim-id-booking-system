package org.zimid.ussdservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.zimid.ussdservice.model.UssdSession;

@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, UssdSession> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, UssdSession> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        // Use String serializer for keys
        template.setKeySerializer(new StringRedisSerializer());

        // Use Jackson serializer for values
        Jackson2JsonRedisSerializer<UssdSession> serializer =
                new Jackson2JsonRedisSerializer<>(UssdSession.class);
        template.setValueSerializer(serializer);
        template.setHashValueSerializer(serializer);

        template.afterPropertiesSet();
        return template;
    }
}
