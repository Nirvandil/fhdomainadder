package cf.nirvandil.fhdomainadder.web;

import cf.nirvandil.fhdomainadder.model.panel.*;
import cf.nirvandil.fhdomainadder.service.panel.PanelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;

@Slf4j
@RestController
@RequestMapping("/api/panel")
public class PanelApiController {

    private final PanelService panelService;

    @Autowired
    public PanelApiController(PanelService panelService) {
        this.panelService = panelService;
    }

    @PostMapping("/users")
    public synchronized List<String> getUserNames(@RequestBody ConnectionDetails connectionDetails) {
        log.info(connectionDetails.toString());
        return panelService.getUsers(connectionDetails);
    }

    @PostMapping(value = "/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public synchronized ResponseEntity<String> createDomain(@RequestParam("request") DomainCreationRequest request,
                                                            @RequestParam(value = "indexFile", required = false) MultipartFile indexFile) {
        log.info(request.toString());
        if (indexFile != null) {
            String response = panelService.createDomainWithIndex(request, indexFile);
            log.info("Response for creating domain with index file is {}.", response);
            return ResponseEntity.status(CREATED).body(response);
        } else {
            String response = panelService.createDomain(request);
            log.info(response);
            return ResponseEntity.status(CREATED).body(response);
        }
    }

    @PostMapping("/check")
    public ResponseEntity<YesNo> checkCgi(@RequestBody CheckCgiRequest request) {

        return ResponseEntity.ok(YesNo.YES);
    }

    @PostMapping("/delete")
    public synchronized ResponseEntity<String> deleteDomains(@RequestBody DomainCreationRequest request) {
        log.info(request.toString());
        String response = panelService.deleteDomain(request);
        log.info(response);
        return ResponseEntity.ok().build();
    }
}
