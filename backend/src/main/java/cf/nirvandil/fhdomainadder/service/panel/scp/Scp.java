package cf.nirvandil.fhdomainadder.service.panel.scp;

import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import lombok.Setter;

import java.io.File;
import java.io.IOException;

public class Scp extends SSHBase {

    @Setter
    private String remoteDirectory = ".";

    public Scp(String host) {
        this(host, DEFAULT_SSH_PORT);
    }

    public Scp(String host, int port) {
        super(host, port);
    }

    /**
     * Upload file using SCP connection.
     *
     * @param file File to upload to destination
     * @throws IOException
     * @throws JSchException
     */
    public void upload(File file) throws IOException, JSchException {
        Session session = null;
        try {
            session = openSession();
            ScpToMessage message = new ScpToMessage(session, file, remoteDirectory, isVerbose());
            listener.ifPresent(message::setListener);
            message.execute();
        } finally {
            if (session != null) {
                session.disconnect();
            }
        }
    }

}
