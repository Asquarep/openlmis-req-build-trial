package org.openlmis.requisition.service;

import org.apache.commons.codec.Charsets;
import org.apache.commons.codec.binary.Base64;
import org.openlmis.requisition.exception.EncodingException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.util.UriUtils;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;


public abstract class BaseCommunicationService {
  protected static final String ACCESS_TOKEN = "access_token";

  protected RestTemplate restTemplate;

  @Value("${auth.server.clientId}")
  private String clientId;

  @Value("${auth.server.clientSecret}")
  private String clientSecret;

  @Value("${auth.server.authorizationUrl}")
  private String authorizationUrl;

  public BaseCommunicationService() {
    this.restTemplate = new RestTemplate();
  }

  protected String obtainAccessToken() {
    String plainCreds = clientId + ":" + clientSecret;
    byte[] plainCredsBytes = plainCreds.getBytes();
    byte[] base64CredsBytes = Base64.encodeBase64(plainCredsBytes);
    String base64Creds = new String(base64CredsBytes);

    HttpHeaders headers = new HttpHeaders();
    headers.add("Authorization", "Basic " + base64Creds);

    HttpEntity<String> request = new HttpEntity<>(headers);

    Map<String, Object> params = new HashMap<>();
    params.put("grant_type", "client_credentials");

    ResponseEntity<?> response = restTemplate.exchange(
        buildUri(authorizationUrl, params), HttpMethod.POST, request, Object.class);


    return ((Map<String, String>) response.getBody()).get(ACCESS_TOKEN);
  }

  protected URI buildUri(String url, Map<String, ?> params) {
    UriComponentsBuilder builder = UriComponentsBuilder.newInstance().uri(URI.create(url));

    params.entrySet().forEach(e -> {
      try {
        builder.queryParam(e.getKey(),
            UriUtils.encodeQueryParam(String.valueOf(e.getValue()), Charsets.UTF_8.name()));
      } catch (UnsupportedEncodingException ex) {
        throw new EncodingException(ex);
      }
    });

    return builder.build(true).toUri();
  }
}
