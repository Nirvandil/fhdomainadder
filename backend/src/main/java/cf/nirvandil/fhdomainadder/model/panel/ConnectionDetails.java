package cf.nirvandil.fhdomainadder.model.panel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Contains all data, that need for connect to an server through SSH protocol.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConnectionDetails {
    private String ip;
    private String password;
    private int port;
}
