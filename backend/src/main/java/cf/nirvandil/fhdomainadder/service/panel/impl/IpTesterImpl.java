package cf.nirvandil.fhdomainadder.service.panel.impl;

import cf.nirvandil.fhdomainadder.service.panel.IpTester;
import org.apache.commons.net.util.SubnetUtils;
import org.springframework.stereotype.Component;

/**
 * Implementation of {@link IpTester}, that checks given IP for belonging to specific subnet.
 * Based on apache {@link SubnetUtils}.
 */
@Component
public class IpTesterImpl implements IpTester {
    private String[] ourNets = {
            "91.90.192.0/24",
            "91.90.193.0/24",
            "91.90.194.0/24",
            "91.90.195.0/24",
            "91.210.164.0/24",
            "91.210.165.0/24",
            "91.210.166.0/24",
            "91.210.167.0/24",
            "91.215.152.0/24",
            "91.215.153.0/24",
            "91.215.154.0/24",
            "91.215.155.0/24",
            "91.223.123.0/24",
            "91.247.36.0/23",
            "104.200.128.0/24",
            "185.198.164.0/22",
            "195.28.182.0/23",
            "195.245.112.0/23",
            "46.28.68.0/24"
    };

    @Override
    public boolean isOurNet(String ip) {
        for (final String subnet : ourNets) {
            SubnetUtils.SubnetInfo subnetInfo = (new SubnetUtils(subnet)).getInfo();
            if (subnetInfo.isInRange(ip))
                return true;
        }
        return false;
    }
}
