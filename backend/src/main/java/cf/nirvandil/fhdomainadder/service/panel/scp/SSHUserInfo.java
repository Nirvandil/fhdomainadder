package cf.nirvandil.fhdomainadder.service.panel.scp;

import com.jcraft.jsch.UserInfo;
import lombok.Data;

@Data
public class SSHUserInfo implements UserInfo {

    private String name;
    private String password = null;
    private String keyFile;
    private String passphrase = null;
    private boolean trustAllCertificates;

    /**
     * Constructor for SSHUserInfo.
     */
    public SSHUserInfo() {
        super();
        this.trustAllCertificates = true;
    }

    /**
     * Constructor for SSHUserInfo.
     *
     * @param name the user's name
     */
    public SSHUserInfo(String name) {
        super();
        this.name = name;
        this.trustAllCertificates = true;
    }

    /**
     * Constructor for SSHUserInfo.
     *
     * @param name                 the user's name
     * @param password             the user's password
     * @param trustAllCertificates if true trust hosts whose identity is unknown
     */
    public SSHUserInfo(String name, String password, boolean trustAllCertificates) {
        super();
        this.name = name;
        this.password = password;
        this.trustAllCertificates = trustAllCertificates;
    }

    /**
     * Prompts a string.
     *
     * @param str the string
     * @return whether the string was prompted
     */
    public boolean prompt(String str) {
        return false;
    }

    /**
     * Indicates whether a retry was done.
     *
     * @return whether a retry was done
     */
    public boolean retry() {
        return false;
    }

    /**
     * @return whether to trust or not.
     */
    public boolean getTrust() {
        return this.trustAllCertificates;
    }

    /**
     * Sets the trust.
     *
     * @param trust whether to trust or not.
     */
    public void setTrust(boolean trust) {
        this.trustAllCertificates = trust;
    }

    /**
     * Implement the UserInfo interface.
     *
     * @param message ignored
     * @return true always
     */
    public boolean promptPassphrase(String message) {
        return true;
    }

    /**
     * Implement the UserInfo interface.
     *
     * @param passwordPrompt ignored
     * @return true the first time this is called, false otherwise
     */
    public boolean promptPassword(String passwordPrompt) {
        return true;
    }

    /**
     * Implement the UserInfo interface.
     *
     * @param message ignored
     * @return the value of trustAllCertificates
     */
    public boolean promptYesNo(String message) {
        return trustAllCertificates;
    }

//why do we do nothing?

    /**
     * Implement the UserInfo interface (noop).
     *
     * @param message ignored
     */
    public void showMessage(String message) {
        //log(message, Project.MSG_DEBUG);
    }

}
