package cf.nirvandil.fhdomainadder.service.panel;

import cf.nirvandil.fhdomainadder.model.panel.CheckCgiRequest;
import cf.nirvandil.fhdomainadder.model.panel.ConnectionDetails;
import cf.nirvandil.fhdomainadder.model.panel.DomainCreationRequest;
import cf.nirvandil.fhdomainadder.model.panel.YesNo;

import java.util.List;

public interface PanelService {
    List<String> getUsers(ConnectionDetails connectionDetails);

    String createDomain(DomainCreationRequest request);

    YesNo checkCgi(CheckCgiRequest request);

    String deleteDomain(DomainCreationRequest request);
}
