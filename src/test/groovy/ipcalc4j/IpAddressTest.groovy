package ipcalc4j

import ipcalc4j.impl.IpAddressImpl
import spock.lang.Specification


class IpAddressTest extends Specification {
  def "test IsNetwork"() {

    expect:
    IpAddress.of(ip, mask).isNetwork() == result

    where:
    ip            | mask  || result
    "192.168.1.1" | 24    || false
    "192.168.1.0" | 24    || true
    "192.168.2.0" | 16    || false
    "192.168.1.1" | "255.255.255.0" || false
    "192.168.1.0" | "255.255.255.0" || true
    "192.168.2.0" | "255.255.0.0"   || false
  }

  def "test NetworkAddress"() {
    expect:
    IpAddress.of(ip, mask).networkAddress().equals(result)

    where:
    ip            | mask  || result
    "192.168.1.1" | 24    || IpAddress.of("192.168.1.0", 24)
    "192.168.1.0" | 24    || IpAddress.of("192.168.1.0", 24)
    "192.168.2.0" | 16    || IpAddress.of("192.168.0.0", 16)
    "192.168.1.1" | "255.255.255.0" || IpAddress.of("192.168.1.0", 24)
    "192.168.1.0" | "255.255.255.0" || IpAddress.of("192.168.1.0", 24)
    "192.168.2.0" | "255.255.0.0"   || IpAddress.of("192.168.0.0", 16)

  }

  def "test BroadcastAddress"() {
    expect:
    IpAddress.of(ip, mask).broadcastAddress().equals(result)

    where:
    ip            | mask  || result
    "192.168.1.1" | 24    || IpAddress.of("192.168.1.255", 24)
    "192.168.1.0" | 24    || IpAddress.of("192.168.1.255", 24)
    "192.168.2.0" | 16    || IpAddress.of("192.168.255.255", 16)
    "192.168.1.1" | "255.255.255.0" || IpAddress.of("192.168.1.255", 24)
    "192.168.1.0" | "255.255.255.0" || IpAddress.of("192.168.1.255", 24)
    "192.168.2.0" | "255.255.0.0"   || IpAddress.of("192.168.255.255", 16)
  }

  def "test Next"() {
    expect:
    IpAddress.of(ip, mask).next().equals(result)

    where:
    ip            | mask  || result
    "192.168.1.1" | 24    || Optional.of(IpAddress.of("192.168.1.2", 24))
    "192.168.1.0" | 24    || Optional.of(IpAddress.of("192.168.1.1", 24))
    "192.168.2.0" | 16    || Optional.of(IpAddress.of("192.168.2.1", 16))
    "192.168.1.1" | "255.255.255.0" || Optional.of(IpAddress.of("192.168.1.2", 24))
    "192.168.1.0" | "255.255.255.0" || Optional.of(IpAddress.of("192.168.1.1", 24))
    "192.168.2.0" | "255.255.0.0"   || Optional.of(IpAddress.of("192.168.2.1", 16))
    "192.168.2.255" | 16    || Optional.of(IpAddress.of("192.168.3.0", 16))
    "192.168.1.255" | 24    || Optional.empty()
  }

  def "test Forward"() {
    expect:
    IpAddress.of(ip, mask).forward().equals(result)

    where:
    ip            | mask  || result
    "192.168.1.1" | 24    || Optional.of(IpAddress.of("192.168.1.0", 24))
    "192.168.1.0" | 24    || Optional.empty()
    "192.168.2.0" | 16    || Optional.of(IpAddress.of("192.168.1.255", 16))
    "192.168.1.1" | "255.255.255.0" || Optional.of(IpAddress.of("192.168.1.0", 24))
    "192.168.1.0" | "255.255.255.0" || Optional.empty()
    "192.168.2.0" | "255.255.0.0"   || Optional.of(IpAddress.of("192.168.1.255", 16))
    "192.168.2.255" | 16    || Optional.of(IpAddress.of("192.168.2.254", 16))
  }

  def "test HostCount"() {
    expect:
    IpAddress.of(ip, mask).hostCount() == result

    where:
    ip            | mask  || result
    "192.168.1.1" | 24    || 254
    "192.168.1.0" | 24    || 254
    "192.168.2.0" | 16    || 65534
    "192.168.1.1" | "255.255.255.0" || 254
    "192.168.1.0" | "255.255.255.0" || 254
    "192.168.2.0" | "255.255.0.0"   || 65534
    "192.168.2.255" | 16    || 65534
  }
}
