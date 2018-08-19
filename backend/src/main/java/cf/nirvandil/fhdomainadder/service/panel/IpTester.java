package cf.nirvandil.fhdomainadder.service.panel;

/**
 * Checks, whether to allow actions with given IP-address.
 */
public interface IpTester {
    boolean isOurNet(String ip);
}
