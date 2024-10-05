//package com.kirana.register.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.PropertySource;
//import org.springframework.data.redis.cache.RedisCacheConfiguration;
//import org.springframework.data.redis.cache.RedisCacheManager;
//import org.springframework.data.redis.connection.RedisConnectionFactory;
//import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
//import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
//import org.springframework.data.redis.serializer.RedisSerializationContext;
//
//import java.time.Duration;
//
//@Configuration
//@EnableRedisRepositories
//public class RedisConfig {
//
//    // This bean defines the RedisCacheManager that manages cache settings.
//    @Bean
//    public RedisCacheManager redisCacheManager(RedisTemplate<String, Object> redisTemplate) {
//        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
//                .entryTtl(Duration.ofDays(2)) // Set the default expiry time to 2 days
//                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(redisTemplate.getValueSerializer()));
//
//        return RedisCacheManager.builder(redisTemplate.getConnectionFactory())
//                .cacheDefaults(config)
//                .build();
//    }
//
//    // This bean defines the RedisTemplate needed for caching operations.
//
//    @Bean
//    RedisTemplate<String, String> redisTemplate(RedisConnectionFactory connectionFactory) {
//
//        RedisTemplate<String, String> template = new RedisTemplate<>();
//        template.setConnectionFactory(connectionFactory);
//        return template;
//    }
//}
