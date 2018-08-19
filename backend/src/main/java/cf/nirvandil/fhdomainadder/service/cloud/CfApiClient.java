package cf.nirvandil.fhdomainadder.service.cloud;

import cf.nirvandil.fhdomainadder.model.cloud.RecordCreationRequest;
import cf.nirvandil.fhdomainadder.model.cloud.RecordCreationResponse;
import cf.nirvandil.fhdomainadder.model.cloud.ZoneCreationRequest;
import cf.nirvandil.fhdomainadder.model.cloud.ZoneCreationResponse;

/**
 * Allows for interaction with Cloudflare API.
 */
public interface CfApiClient {

    ZoneCreationResponse createDomain(ZoneCreationRequest creationRequest);

    RecordCreationResponse createRecord(RecordCreationRequest creationRequest, String zoneId);

}
