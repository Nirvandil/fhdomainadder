package cf.nirvandil.fhdomainadder.service.panel.impl;

import cf.nirvandil.fhdomainadder.errors.*;
import cf.nirvandil.fhdomainadder.model.panel.*;
import cf.nirvandil.fhdomainadder.service.panel.IpTester;
import cf.nirvandil.fhdomainadder.service.panel.PanelService;
import com.jcraft.jsch.*;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.*;
import java.util.*;

import static java.lang.String.format;
import static java.util.Arrays.asList;

/**
 * Implementation of {@link PanelService}, uses JSch for interactions with panel on server.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PanelServiceImpl implements PanelService {

    private static final String NO_PANEL_ANSWER = "no_panel";
    private static final int TEN_SECONDS_TIMEOUT = 10_000;
    private static final int DELAY_BETWEEN_INTERACTIONS = 300;
    private final JSch jSch;
    private final IpTester ipTester;

    @Override
    @SneakyThrows
    public List<String> getUsers(ConnectionDetails connectionDetails) {
        if (!ipTester.isOurNet(connectionDetails.getIp())) {
            throw new HostNotAllowedException();
        }
        ChannelExec channel = createChannel(connectionDetails);
        BufferedReader reader = new BufferedReader(new InputStreamReader(channel.getInputStream()));
        channel.setCommand(Commands.GET_USERS);
        channel.connect();
        List<String> output = getOutput(reader);
        if (StringUtils.isEmpty(output.get(0))) {
            throw new NoSuchUsersException();
        }
        if (output.get(0).equals("no_panel")) {
            throw new PanelDoesNotExistException();
        }
        log.info("Found users [{}]", output);
        channel.disconnect();
        return output;
    }

    @Override
    @SneakyThrows
    public synchronized String createDomain(DomainCreationRequest request) {
        ConnectionDetails connectionDetails = request.getConnectionDetails();
        String ip = connectionDetails.getIp();
        String domain = request.getDomain();
        String userName = request.getUserName();
        ChannelExec channel = createChannel(connectionDetails);
        String createCommand = request.isCgi() ? format(Commands.CREATE_DOMAIN_CGI, userName, domain, ip) :
                format(Commands.CREATE_DOMAIN_MOD, userName, domain, ip);
        log.info("Create domain command : {}", createCommand);
        BufferedReader reader = new BufferedReader(new InputStreamReader(channel.getInputStream()));
        channel.setCommand(createCommand);
        channel.connect();
        List<String> output = getOutput(reader);
        if (!output.isEmpty()) {
            if (StringUtils.isEmpty(output.get(0))) {
                throw new NoSuchUsersException();
            }
            if (output.get(0).equals(NO_PANEL_ANSWER)) {
                throw new PanelDoesNotExistException();
            }
            log.debug("Create domain output: [{}]", output);
        }
        Thread.sleep(DELAY_BETWEEN_INTERACTIONS);
        channel.disconnect();
        return output.toString();
    }

    private List<String> getOutput(BufferedReader reader) throws IOException {
        List<String> output = new ArrayList<>();
        String line;
        while ((line = reader.readLine()) != null) {
            output.addAll(asList(line.trim().split(" ")));
        }
        reader.close();
        return output;
    }

    @Override
    @SneakyThrows
    public YesNo checkCgi(CheckCgiRequest request) {
        ConnectionDetails details = request.getConnectionDetails();
        String user = request.getUser();
        ChannelExec channel = createChannel(details);
        BufferedReader reader = new BufferedReader(new InputStreamReader(channel.getInputStream()));
        String command = format(Commands.CHECK_CGI, user);
        log.info(command);
        channel.setCommand(command);
        channel.connect();
        List<String> output = getOutput(reader);
        String answer = output.get(0);
        channel.disconnect();
        if (answer.equals(NO_PANEL_ANSWER)) {
            throw new PanelDoesNotExistException();
        }
        log.info(YesNo.valueOf(answer).name());
        if (answer.equals("NO")) throw new CgiNotSupportedException();
        return YesNo.valueOf(answer);
    }

    @Override
    @SneakyThrows
    public String deleteDomain(DomainCreationRequest request) {
        ConnectionDetails connectionDetails = request.getConnectionDetails();
        String user = request.getUserName();
        String command = format(Commands.REMOVE_DOMAIN, user, request.getDomain());
        log.info("DELETE domain command is:\n {}", command);
        String output = getCommandOutput(connectionDetails, command);
        if (!output.isEmpty()) {
            if (output.equals(NO_PANEL_ANSWER)) {
                throw new PanelDoesNotExistException();
            }
            log.info("Delete domain output: [{}]", output);
        }
        Thread.sleep(300);
        return output;
    }

    @SneakyThrows
    private ChannelExec createChannel(ConnectionDetails details) {
        Session session = jSch.getSession("root", details.getIp(), details.getPort());
        Properties config = new Properties();
        config.put("StrictHostKeyChecking", "no");
        config.put("PreferredAuthentications", "password");
        session.setConfig(config);
        session.setPassword(details.getPassword());
        session.connect(TEN_SECONDS_TIMEOUT);
        return (ChannelExec) session.openChannel("exec");
    }

    @SneakyThrows
    private String getCommandOutput(ConnectionDetails details, String command) {
        log.debug("Received request for getting command output for command\n {} with connection details\n {}", command, details);
        ChannelExec channel = createChannel(details);
        BufferedReader reader = new BufferedReader(new InputStreamReader(channel.getInputStream()));
        channel.setCommand(command);
        channel.connect();
        List<String> output = getOutput(reader);
        log.debug("Received output is {}", output);
        channel.disconnect();
        if (output.size() == 1) return output.get(0);
        return output.toString();
    }

    private static class Commands {
        private static final String GET_USERS =
                "if [ -d /usr/local/vesta ]; " +
                        "then echo `/bin/ls /usr/local/vesta/data/users/`; " +
                        "elif [ -d /usr/local/ispmgr ]; " +
                        "then echo `/usr/local/ispmgr/sbin/mgrctl user | cut -f 2 -d '='| cut -f 1 -d ' '`; " +
                        "elif [ -d /usr/local/mgr5 ]; " +
                        "then echo `/usr/local/mgr5/sbin/mgrctl -m ispmgr user | cut -f 2 -d '=' | cut -f1 -d ' '`; " +
                        "else echo no_panel; fi";
        private static final String CREATE_DOMAIN_MOD = "if [ -d /usr/local/vesta ]; " +
                "then VESTA=/usr/local/vesta /usr/local/vesta/bin/v-add-web-domain %1$s %2$s %3$s; /usr/local/vesta/bin/v-change-web-domain-tpl %1$s %2$s default; " +
                "elif [ -d /usr/local/ispmgr ]; " +
                "then /usr/local/ispmgr/sbin/mgrctl -m ispmgr wwwdomain.edit " +
                "domain=%2$s alias=www.%2$s docroot=auto owner=%1$s admin=admin@%2$s autosubdomain=asdnone ip=%3$s php=phpmod sok=ok; " +
                "elif [ -d /usr/local/mgr5 ]; " +
                "then /usr/local/mgr5/sbin/mgrctl -m ispmgr webdomain.edit name=%2$s alias=www.%2$s docroot=auto owner=%1$s email=admin@%2$s ipsrc=manual ipaddrs=%3$s " +
                "php=on php_mode=php_mode_mod sok=ok ; else echo no_panel; fi 2>&1";
        private static final String CHECK_CGI = "if [ -d /usr/local/vesta ]; " +
                "then echo YES; " +
                "elif [ -d /usr/local/ispmgr ]; " +
                "then if [ -z \"$(/usr/local/ispmgr/sbin/mgrctl -m ispmgr user elid=%1$s | grep cgi)\" ] ; then echo NO; else echo YES; fi " +
                "elif [ -d /usr/local/mgr5 ]; " +
                "then if [ -z \"$(/usr/local/mgr5/sbin/mgrctl -m ispmgr user elid=%1$s | grep limit_php_mode_cgi)\" ]; then echo NO; else echo YES; fi;" +
                "else echo no_panel; fi";
        private static final String CREATE_DOMAIN_CGI = "if [ -d /usr/local/vesta ]; " +
                "then VESTA=/usr/local/vesta /usr/local/vesta/bin/v-add-web-domain %1$s %2$s %3$s; " +
                "elif [ -d /usr/local/ispmgr ]; " +
                "then /usr/local/ispmgr/sbin/mgrctl -m ispmgr wwwdomain.edit " +
                "domain=%2$s alias=www.%2$s docroot=auto owner=%1$s admin=admin@%2$s autosubdomain=asdnone ip=%3$s php=phpcgi sok=ok; " +
                "elif [ -d /usr/local/mgr5 ]; " +
                "then /usr/local/mgr5/sbin/mgrctl -m ispmgr webdomain.edit name=%2$s alias=www.%2$s docroot=auto owner=%1$s email=admin@%2$s ipsrc=manual ipaddrs=%3$s " +
                "php=on php_mode=php_mode_cgi sok=ok ; else echo no_panel; fi 2>&1";

        private static final String REMOVE_DOMAIN = "if [ -d /usr/local/vesta ]; " +
                "then VESTA=/usr/local/vesta /usr/local/vesta/bin/v-delete-web-domain %1$s %2$s && VESTA=/usr/local/vesta /usr/local/vesta/bin/v-delete-dns-domain %1$s %2$s ; " +
                "elif [ -d /usr/local/ispmgr ]; " +
                "then /usr/local/ispmgr/sbin/mgrctl -m ispmgr wwwdomain.delete elid=%2$s wwwdomain.delete.confirm elid=%2$s sok=ok ; " +
                "elif [ -d /usr/local/mgr5 ]; " +
                "then /usr/local/mgr5/sbin/mgrctl -m ispmgr webdomain.delete elid=%2$s webdomain.delete.confirm elid=%2$s sok=ok ; else echo no_panel; fi 2>&1";
    }
}
