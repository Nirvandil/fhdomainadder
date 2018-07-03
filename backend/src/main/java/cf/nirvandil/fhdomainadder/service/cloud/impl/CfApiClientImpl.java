package cf.nirvandil.fhdomainadder.service.cloud.impl;

import cf.nirvandil.fhdomainadder.model.cloud.RecordCreationRequest;
import cf.nirvandil.fhdomainadder.model.cloud.RecordCreationResponse;
import cf.nirvandil.fhdomainadder.model.cloud.ZoneCreationRequest;
import cf.nirvandil.fhdomainadder.model.cloud.ZoneCreationResponse;
import cf.nirvandil.fhdomainadder.service.cloud.CfApiClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collection;
import java.util.Collections;

@Service
public class CfApiClientImpl implements CfApiClient {
    @Value("${cloud.api.url}")
    private String CF_API_ENDPOINT;
    private final RestTemplate restTemplate;

    @Autowired
    public CfApiClientImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public ZoneCreationResponse createDomain(ZoneCreationRequest creationRequest) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Auth-Key", creationRequest.getApiKey());
        headers.add("X-Auth-Email", creationRequest.getEmail());
        HttpEntity<ZoneCreationRequest> request = new HttpEntity<>(creationRequest, headers);
        return restTemplate.postForEntity(CF_API_ENDPOINT + "zones", request, ZoneCreationResponse.class)
                .getBody();
    }

    @Override
    public RecordCreationResponse createRecord(RecordCreationRequest creationRequest, String zoneId) {
        String url = CF_API_ENDPOINT + "zones/" + zoneId + "/dns_records";
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Auth-Key", creationRequest.getApiKey());
        headers.add("X-Auth-Email", creationRequest.getEmail());
        HttpEntity<RecordCreationRequest> request = new HttpEntity<>(creationRequest, headers);
        return restTemplate.postForEntity(url, request, RecordCreationResponse.class).getBody();
    }

}
