package com.multipledb.feign;

import java.security.NoSuchAlgorithmException;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.multipledb.Entity.ShortURLModel;

@FeignClient(name="TINYURL-SERVICE", path="/tinyurl")
public interface BookUserInterface {

	@PostMapping("")
	public ResponseEntity<String> createURL(@RequestBody ShortURLModel shortUrlModel) throws NoSuchAlgorithmException ;
	
	@GetMapping("/{shortKey}")
	public ResponseEntity<String> getShortURL(@PathVariable("shortKey") String shortKey);
	
	@PutMapping("/{shortKey}")
	public ResponseEntity<com.multipledb.Entity.ShortURLEntity> updateShortURLEntity(@RequestBody ShortURLModel shortURLModel,
			@PathVariable("shortKey") String shortKey,@RequestParam boolean isActive,
			@RequestParam int length);
}