package com.example.redis;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

	@Value("${spring.data.redis.host}")
	private String redisHost;
	
	@Value("${spring.data.redis.port}")
	private String redisPort;
	
	/**
	 * 스프링 부트는 Lettuce 라이브러리를 기본으로 사용
	 * Lettuce 는 내부에 Netty 프레임워크를 포함하고 있어서 비동기 논블로킹으로 구현되어 있다.
	 * 그래서 스프링 애플리케이션과 레디스 사이에 커넥션 풀이 필요없다.
	 * 즉, 하나의 커넥션을 맺고 사용하면 된다.
	 * 비동기 논블로킹이므로 스프링 부트 애플리케이션이 멀티 스레드를 사용하더라도
	 * 멀티 스레드에 안전한 프로그래밍을 할 수 있다.
	 * 
	 * Redis 자체가 싱글 스레드 기반이기 때문에 여러 커넥션을 사용한다고 해서 성능이 좋아지지 않는다.
	 * 단, 레디스의 트랜잭션 기능을 사용한다면 커넥션 풀을 설정해서 사용하는 것이 좋다.
	 */
	@Bean
	public RedisConnectionFactory redisConnetionFactory() {
	
		RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
//		RedisClusterConfiguration config = new RedisClusterConfiguration();
//		RedisSentinelConfiguration config = new RedisSentinelConfiguration();
		config.setHostName(redisHost);
		config.setPort(Integer.parseInt(redisPort));
		
		return new LettuceConnectionFactory(config);
	}
	
	@Bean
	public RedisTemplate<String, Object> redisTemplate() {
		RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(redisConnetionFactory());
		redisTemplate.setKeySerializer(new StringRedisSerializer());
		redisTemplate.setValueSerializer(new StringRedisSerializer());
		return redisTemplate;
	}

}
