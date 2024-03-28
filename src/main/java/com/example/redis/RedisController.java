package com.example.redis;

import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RedisController {
	
	@Autowired
    private StringRedisTemplate stringRedisTemplate;
	
    @PostMapping("/redisTest")
    public ResponseEntity<?> addRedisKey(@RequestBody Map<String, String> map) {
        ValueOperations<String, String> valueOperations = stringRedisTemplate.opsForValue();
        map.forEach((key, value) -> valueOperations.set(key, value));
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

	@GetMapping("/redisTest/{key}")
	public ResponseEntity<?> getRedisKey(@PathVariable("key") String key) {
		ValueOperations<String, String> vop = stringRedisTemplate.opsForValue();
		String value = vop.get(key);
		return new ResponseEntity<>(value, HttpStatus.OK);
	}
	
    @GetMapping("/redisTest/keys")
    public Set<String> getAllKeys() {
    	 Set<String> keys = stringRedisTemplate.keys("*");
         return keys;
    }

}
