package cf.nirvandil.fhdomainadder.web;

import cf.nirvandil.fhdomainadder.model.cloud.*;
import cf.nirvandil.fhdomainadder.service.cloud.CfApiClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    public ZoneCreationResponse createZone(@RequestBody ZoneCreationRequest request) {
        log.info(request.toString());
        ZoneCreationResponse response = cfApiClient.createDomain(request);
        log.info(response.toString());
        return response;
    }

    @PostMapping("/zone/{zoneId}")
    public RecordCreationResponse createDnsRecord(@RequestBody RecordCreationRequest request, @PathVariable String zoneId) {
        log.info(request.toString());
        RecordCreationResponse response = cfApiClient.createRecord(request, zoneId);
        log.info(response.toString());
        return response;
    }
}
