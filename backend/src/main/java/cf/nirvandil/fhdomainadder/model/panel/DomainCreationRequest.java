package cf.nirvandil.fhdomainadder.model.panel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request for creating domain on server through panel API.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DomainCreationRequest {
    ConnectionDetails connectionDetails;
    private String domain;
    private String userName;
    private boolean cgi;
}
