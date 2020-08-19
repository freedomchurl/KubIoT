package vaninside.kubiot.dataanalyzer.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfiguration {
	
	@Value("${spring.redis.host}")
    private String redisHostName;

    @Value("${spring.redis.password}")
    private String redisPassword;

    @Value("${spring.redis.port}")
    private int redisPort;

	 @Bean
	    public RedisConnectionFactory redisConnectionFactory() {
		 //System.out.println(redisHostName);
		 //System.out.println(redisPort);
	        LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionFactory();
	        lettuceConnectionFactory.setHostName(redisHostName);
	        lettuceConnectionFactory.setPort(redisPort);
	        lettuceConnectionFactory.setPassword(redisPassword);
	        return lettuceConnectionFactory;
	    }
	 
	    @Bean
	    public RedisTemplate<String, Object> redisTemplate() {
	        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
	        redisTemplate.setConnectionFactory(redisConnectionFactory());
	        redisTemplate.setKeySerializer(new StringRedisSerializer());  
	        redisTemplate.setValueSerializer(new GenericToStringSerializer<>(Double.class));
	        return redisTemplate;
	    }
}
