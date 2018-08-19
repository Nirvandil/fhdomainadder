package cf.nirvandil.fhdomainadder.model.panel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request for checking is CGI supported by selected user.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CheckCgiRequest {
    ConnectionDetails connectionDetails;
    String user;
}
