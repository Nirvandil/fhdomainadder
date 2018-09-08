package cf.nirvandil.fhdomainadder.service.panel.impl;

import cf.nirvandil.fhdomainadder.service.panel.IpTester;
import org.junit.Test;

import static org.junit.Assert.*;

public class FhIpTesterTest {
    private final IpTester ipTester = new FhIpTester();
    @Test
    public void isAllowedAddress() {
        assertTrue(ipTester.isAllowedAddress("195.28.182.231"));
        assertFalse(ipTester.isAllowedAddress("46.148.188.78"));
    }
    @Test
    public void isAllowedAddressWithNull() {
        assertFalse(ipTester.isAllowedAddress(null));
    }

    @Test
    public void isAllowedAddressWithEmpty() {
        assertFalse(ipTester.isAllowedAddress(""));
    }

    @Test(expected = IllegalArgumentException.class)
    public void isAllowedAddressWithNotParsable() {
        ipTester.isAllowedAddress("asdada");
    }
}