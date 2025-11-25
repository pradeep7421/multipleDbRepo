package com.multipledb.utils;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import com.multipledb.Entity.ShortURLEntity;
import com.multipledb.Entity.ShortURLModel;

@Component
public class OtherUtility {

	@Autowired
	private RestTemplate restTemplate;
	@Value("${tinyUrl.get.url}")
	private String tinyUrlServiceBaseUrl ;
	@Autowired
	private WebClient.Builder builder;
	public void requestsWithDifferentHttpMethodsByExchangeMethod(ShortURLModel body , String shortKey) {
//		String apiUrl = tinyUrlServiceBaseUrl+shortKey+"?isActive=true&length=2";
		String apiUrl = UriComponentsBuilder.fromUriString(tinyUrlServiceBaseUrl + shortKey)
		        .queryParam("isActive", true)
		        .queryParam("length", 2)
		        .toUriString();
		
		HttpHeaders reqHeaders = new  HttpHeaders();
		reqHeaders.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<ShortURLModel> httpEntity = new HttpEntity<>(body, reqHeaders);

//POST REQUEST WITH or WITHOUT HTTPENTITY and REQUEST BODY						
		ResponseEntity<ShortURLEntity> postResponse =
		        restTemplate.exchange(
		                apiUrl,
		                HttpMethod.POST,
		                httpEntity,
		                ShortURLEntity.class
		        );

//GET REQUEST WITH or WITHOUT HTTPENTITY and REQUEST BODY				
		HttpEntity<Void> httpEntityWithoutBody = new HttpEntity<>(reqHeaders);  // without requestbody
		ResponseEntity<ShortURLEntity> getResponse =
		        restTemplate.exchange(
		                apiUrl,
		                HttpMethod.GET,
		                httpEntityWithoutBody,
		                ShortURLEntity.class
		        );
		ResponseEntity<ShortURLEntity> getResponseWithNullHttpEntity =
		        restTemplate.exchange(
		                apiUrl,
		                HttpMethod.GET,
		                null,
		                ShortURLEntity.class
		        );
//PUT REQUEST WITH HTTPENTITY and REQUEST BODY				
				ResponseEntity<ShortURLEntity> putResponse =
			        restTemplate.exchange(
			                apiUrl,
			                HttpMethod.PUT,
			                httpEntity, // request body is required for put request whivh is embedded in httpEntity
			                ShortURLEntity.class
			        );	
//DELETE REQUEST WITH or WITHOUT HTTPENTITY and REQUEST BODY		
		ResponseEntity<Void> deleteResponse =
		        restTemplate.exchange(
		                apiUrl,
		                HttpMethod.DELETE,
		                null,  // httpEntity is null as in delete reequest generally request body is not rewuired
		                Void.class
		        );
				
		HttpEntity<ShortURLModel> deleteHttpEntity = new HttpEntity<>(body, reqHeaders);
		ResponseEntity<String> response =
		        restTemplate.exchange(
		                apiUrl,
		                HttpMethod.DELETE,
		                deleteHttpEntity,
		                String.class
		        );

//	ShortURLEntity result = response.getBody();
		
		String url = tinyUrlServiceBaseUrl + "{shortKey}?isActive={isActive}&length={length}";

		Map<String, Object> params = new HashMap<>();
		params.put("shortKey", shortKey);
		params.put("isActive", true);
		params.put("length", 2);

		ResponseEntity<ShortURLEntity> response1 =
		        restTemplate.getForEntity(url, ShortURLEntity.class, params);


	}
	
	public void requestsWithDifferentHttpMethodsWithoutExchange(ShortURLModel body , String shortKey) {

//	    String apiUrl = tinyUrlServiceBaseUrl + shortKey + "?isActive=true&length=2";
	    String apiUrl = UriComponentsBuilder.fromUriString(tinyUrlServiceBaseUrl + shortKey)
		        .queryParam("isActive", true)
		        .queryParam("length", 2)
		        .toUriString();
	    HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.APPLICATION_JSON);
	    HttpEntity<ShortURLModel> httpEntity = new HttpEntity<>(body, headers);

	    // POST — response possible
	    ResponseEntity<ShortURLEntity> postResponse =
	            restTemplate.postForEntity(apiUrl, httpEntity, ShortURLEntity.class);

	    ShortURLEntity postObject =
	            restTemplate.postForObject(apiUrl, httpEntity, ShortURLEntity.class);

	    // GET — no headers possible
	    ResponseEntity<ShortURLEntity> getResponse =
	            restTemplate.getForEntity(apiUrl, ShortURLEntity.class);
	 // GET — no headers possible ,with path Variable and request param possible
	    ShortURLEntity getObject =
	            restTemplate.getForObject(apiUrl, ShortURLEntity.class);
	    
	    // PUT — no return possible,with pathvariable and request param possible
	    restTemplate.put(apiUrl, httpEntity);

	    // DELETE — no body possible and no httpEntity possible
	    restTemplate.delete(apiUrl);
	}
	
	
	public void getMethod(String shortKey,ShortURLModel shortURLModel) {
		//xxxxxxxxxxxx------GET METHOD BY WEBLIENT----XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
		builder.build()
        .get()
        .uri(uriBuilder -> uriBuilder
                .scheme("http")
                .host("localhost")
                .port(9090)
                .path("/tinyurl/{shortKey}")
                .queryParam("isActive", true)
                .queryParam("length", 2)
                .build(shortKey)
        )
        .retrieve()
        .bodyToMono(ShortURLEntity.class)
        .doOnSubscribe(s -> System.out.println("GET Request started"))
        .doOnSuccess(r -> System.out.println("GET Response = " + r));
		
		
		
		//xxxxxxxxxxxx------POST METHOD BY WEBLIENT----XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX

		builder.build()
        .post()
        .uri(uriBuilder -> uriBuilder
                .scheme("http")
                .host("localhost")
                .port(9090)
                .path("/tinyurl/create")
                .queryParam("isActive", true)
                .queryParam("length", 2)
                .build()
        )
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(shortURLModel)
        .retrieve()
        .bodyToMono(ShortURLEntity.class)
        .doOnSubscribe(s -> System.out.println("POST Request started")
        )
        .doOnSuccess(r -> System.out.println("POST Response = " + r));

		
		
		//xxxxxxxxxxxx------DELETE METHOD BY WEBLIENT----XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
	
		builder.build()
        .delete()
        .uri(uriBuilder -> uriBuilder
                .scheme("http")
                .host("localhost")
                .port(9090)
                .path("/tinyurl/{shortKey}")
                .queryParam("force", true)
                .build(shortKey)
        )
        .retrieve()
        .bodyToMono(Void.class)
        .doOnSubscribe(s ->System.out.println("DELETE Request started"))
        .doOnSuccess(r -> System.out.println("DELETE completed"));
	}

}
