package cf.nirvandil.fhdomainadder.service.panel.impl;

import cf.nirvandil.fhdomainadder.service.panel.IpTester;
import org.junit.Test;

import static org.junit.Assert.*;

public class SimpleIpTesterTest {
    private final IpTester ipTester = new SimpleIpTester();
    @Test
    public void isAllowedAddress() {
        assertTrue(ipTester.isAllowedAddress("46.148.188.78"));
    }
}