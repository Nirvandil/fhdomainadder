package cf.nirvandil.fhdomainadder.web;

import cf.nirvandil.fhdomainadder.model.cloud.RecordCreationRequest;
import cf.nirvandil.fhdomainadder.model.cloud.RecordCreationResponse;
import cf.nirvandil.fhdomainadder.model.cloud.ZoneCreationRequest;
import cf.nirvandil.fhdomainadder.model.cloud.ZoneCreationResponse;
import cf.nirvandil.fhdomainadder.service.cloud.CfApiClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/cf")
public class CfApiController {
    private final CfApiClient cfApiClient;

    @Autowired
    public CfApiController(CfApiClient cfApiClient) {
        this.cfApiClient = cfApiClient;
    }

    @PostMapping("/zone")
    public synchronized ZoneCreationResponse createZone(@RequestBody ZoneCreationRequest request) {
        log.info(request.toString());
        ZoneCreationResponse response = cfApiClient.createDomain(request);
        log.info(response.toString());
        return response;
    }

    @PostMapping("/zone/{zoneId}")
    public synchronized RecordCreationResponse createDnsRecord(@RequestBody RecordCreationRequest request, @PathVariable String zoneId) {
        log.info(request.toString());
        RecordCreationResponse response = cfApiClient.createRecord(request, zoneId);
        log.info(response.toString());
        return response;
    }
}
