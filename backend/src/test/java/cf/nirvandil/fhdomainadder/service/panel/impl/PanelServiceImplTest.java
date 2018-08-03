package cf.nirvandil.fhdomainadder.service.panel.impl;

import cf.nirvandil.fhdomainadder.model.panel.ConnectionDetails;
import cf.nirvandil.fhdomainadder.model.panel.DomainCreationRequest;
import cf.nirvandil.fhdomainadder.service.panel.PanelService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.ReflectionUtils;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class PanelServiceImplTest {

    private static final String TEST_IP = System.getProperty("test_ip");
    private static final String TEST_PASS = System.getProperty("test_pass");
    private static final int TEST_PORT = 3333;

    @Autowired
    private PanelService panelService;

    @Test
    public void createDomainWithIndex() {
        DomainCreationRequest request = new DomainCreationRequest(
                new ConnectionDetails(TEST_IP, TEST_PASS, TEST_PORT),
                "testdomain.ru", "admin", true
        );
        String answer = panelService.createDomainWithIndex(request, new ByteArrayWrappingMultipartFile(new byte[]{1, 2, 3}));
        log.info(answer);
        panelService.deleteDomain(request);
    }
}