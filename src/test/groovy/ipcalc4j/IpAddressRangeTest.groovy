package ipcalc4j

import spock.lang.Specification


class IpAddressRangeTest extends Specification {
  def "test fromTo"() {
    expect:
    IpAddressRange.fromTo(start, end)
        .filter({ it -> !it.isNetwork() })
        .map({ it -> IpAddress.formatToDottedDecimal(it.getAddress()) })
        .count() == result

    where:
    start                           | end                               || result
    IpAddress.of("192.168.1.0", 24) | IpAddress.of("192.168.1.1", 24)   || 1
    IpAddress.of("192.168.1.1", 24) | IpAddress.of("192.168.1.254", 24) || 254
    IpAddress.of("10.0.0.0", 8)     | IpAddress.of("10.255.255.255", 8) || 16_777_215
  }
}
