package cf.nirvandil.fhdomainadder.service.panel.impl;

import cf.nirvandil.fhdomainadder.service.panel.IpTester;
import org.springframework.stereotype.Component;

@Component
public class SimpleIpTester implements IpTester {
    @Override
    public boolean isAllowedAddress(String ip) {
        return true;
    }
}
