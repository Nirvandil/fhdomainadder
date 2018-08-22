package cf.nirvandil.fhdomainadder.service.cloud.impl;

import cf.nirvandil.fhdomainadder.model.cloud.*;
import cf.nirvandil.fhdomainadder.service.cloud.CfApiClient;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class CfApiClientImpl implements CfApiClient {
    @Value("${cloud.api.url}")
    private String CF_API_ENDPOINT;
    private final RestTemplate restTemplate;

    @Override
    @SneakyThrows
    public ZoneCreationResponse createDomain(ZoneCreationRequest creationRequest) {
        Thread.sleep(300); // throttling.
        HttpHeaders headers = createHeaders(creationRequest.getApiKey(), creationRequest.getEmail());
        HttpEntity<ZoneCreationRequest> request = new HttpEntity<>(creationRequest, headers);
        return restTemplate.postForObject(CF_API_ENDPOINT + "zones", request, ZoneCreationResponse.class);
    }

    @Override
    @SneakyThrows
    public RecordCreationResponse createRecord(RecordCreationRequest creationRequest, String zoneId) {
        Thread.sleep(300); // workaround: get time for Cloudflare to detect zone.
        String url = CF_API_ENDPOINT + "zones/" + zoneId + "/dns_records";
        HttpHeaders headers = createHeaders(creationRequest.getApiKey(), creationRequest.getEmail());
        HttpEntity<RecordCreationRequest> request = new HttpEntity<>(creationRequest, headers);
        return restTemplate.postForObject(url, request, RecordCreationResponse.class);
    }

    private HttpHeaders createHeaders(String apiKey, String email) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Auth-Key", apiKey);
        headers.add("X-Auth-Email", email);
        return headers;
    }

}
