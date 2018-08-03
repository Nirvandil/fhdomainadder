package cf.nirvandil.fhdomainadder.service.panel.scp;

import com.jcraft.jsch.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Optional;
import java.util.logging.Level;


public class SSHBase {

    static final int DEFAULT_SSH_PORT = 22;
    Optional<LogListener> listener = Optional.empty();
    /**
     * If true output more verbose logging
     */
    @Getter @Setter
    private boolean verbose;
    /**
     * User authentication details
     */
    @Getter
    private SSHUserInfo userInfo;
    /**
     * The path to the file that has the identities of
     * all known hosts.  This is used by SSH protocol to validate
     * the identity of the host.  The default is
     * <i>${user.home}/.ssh/known_hosts</i>.
     *
     */
    @Setter
    private String knownHosts;
    /**
     * Remote host, either DNS name or IP.
     */
    @Getter
    @Setter
    private String host;
    private int port;

    SSHBase(String host, int port) {
        this.host = host;
        this.port = port;
        this.userInfo = new SSHUserInfo();
    }

    /**
     * Username known to remote host.
     *
     * @param username The new username value
     */
    public void setUsername(String username) {
        userInfo.setName(username);
    }


    /**
     * Sets the password for the user.
     *
     * @param password The new password value
     */
    public void setPassword(String password) {
        userInfo.setPassword(password);
    }

    /**
     * Sets the keyfile for the user.
     *
     * @param keyfile The new keyfile value
     */
    public void setKeyfile(String keyfile) {
        userInfo.setKeyFile(keyfile);
    }

    /**
     * Sets the passphrase for the users key.
     *
     * @param passphrase The new passphrase value
     */
    public void setPassphrase(String passphrase) {
        userInfo.setPassphrase(passphrase);
    }

    /**
     * Setting this to true trusts hosts whose identity is unknown.
     *
     * @param yesOrNo if true trust the identity of unknown hosts.
     */
    public void setTrust(boolean yesOrNo) {
        userInfo.setTrust(yesOrNo);
    }

    /**
     * Open an ssh seession.
     *
     * @return the opened session
     * @throws JSchException on error
     */
    Session openSession() throws JSchException {
        JSch jsch = new JSch();
        if (verbose && listener.isPresent()) {
            JSch.setLogger(new Logger() {
                public boolean isEnabled(int level) {
                    return true;
                }

                public void log(int level, String message) {
                    listener.get().log(Level.FINE, message);
                }
            });
        }
        if (null != userInfo.getKeyFile()) {
            jsch.addIdentity(userInfo.getKeyFile());
        }

        if (!userInfo.getTrust() && knownHosts != null) {
            listener.ifPresent(logListener -> logListener.log(Level.FINE, "Using known hosts: " + knownHosts));
            jsch.setKnownHosts(knownHosts);
        }

        Session session = jsch.getSession(userInfo.getName(), host, port);
        session.setUserInfo(userInfo);
        listener.ifPresent(logListener -> logListener.log(Level.INFO, "Connecting to " + host + ":" + port));
        session.connect();
        return session;
    }

    /**
     * Configure logging listener.
     *
     * @param listener
     */
    public void setListener(LogListener listener) {
        this.listener = Optional.of(listener);
    }
}
