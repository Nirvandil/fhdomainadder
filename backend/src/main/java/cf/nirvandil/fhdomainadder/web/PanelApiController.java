package cf.nirvandil.fhdomainadder.web;

import cf.nirvandil.fhdomainadder.model.panel.CheckCgiRequest;
import cf.nirvandil.fhdomainadder.model.panel.ConnectionDetails;
import cf.nirvandil.fhdomainadder.model.panel.DomainCreationRequest;
import cf.nirvandil.fhdomainadder.model.panel.YesNo;
import cf.nirvandil.fhdomainadder.service.panel.PanelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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

    @PostMapping("/add")
    public synchronized ResponseEntity<String> createDomain(@RequestBody DomainCreationRequest request) {
        log.info(request.toString());
        String response = panelService.createDomain(request);
        log.info(response);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/check")
    public ResponseEntity<YesNo> checkCgi(@RequestBody CheckCgiRequest request) {
        YesNo answer = panelService.checkCgi(request);
        log.info(answer.toString());
        return ResponseEntity.ok(answer);
    }

    @PostMapping("/delete")
    public synchronized ResponseEntity<String> deleteDomains(@RequestBody DomainCreationRequest request) {
        log.info(request.toString());
        //String response =  panelService.deleteDomains(request);
        // log.info(response);
        return ResponseEntity.ok().build();
    }
}
