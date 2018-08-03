package cf.nirvandil.fhdomainadder.web;

import cf.nirvandil.fhdomainadder.model.panel.DomainCreationRequest;
import cf.nirvandil.fhdomainadder.service.panel.PanelService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static cf.nirvandil.fhdomainadder.web.TestUtils.toBytes;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.internal.verification.VerificationModeFactory.atLeastOnce;
import static org.mockito.internal.verification.VerificationModeFactory.only;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(PanelApiController.class)
public class PanelApiControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private PanelService panelService;

    @Test
    public void getUserNames() {

    }

    @Test
    public void createDomain() throws Exception {
        MockMultipartFile multipartFile = new MockMultipartFile("indexFile", "index.php", "text/plain", "some other type".getBytes());
        mockMvc.perform(multipart("/api/panel/add")
                .file(multipartFile)
                .content(toBytes(new DomainCreationRequest()))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
        )
                .andExpect(status().isCreated());
        verify(panelService, atLeastOnce()).createDomainWithIndex(any(), any());
    }

    @Test
    public void checkCgi() {
    }

    @Test
    public void deleteDomains() {
    }
}