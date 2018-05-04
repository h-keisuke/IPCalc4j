package ipcalc4j;


import ipcalc4j.impl.IpAddressSpliterator;

import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public interface IpAddressRange {

  static Stream<IpAddress> fromTo(final IpAddress start, final IpAddress end){
    if(!start.sameNetwork(end)) throw new IllegalArgumentException();
    return StreamSupport.stream(new IpAddressSpliterator(start.getAddress(),end.getAddress(),start.getMask()), true);
  }

}
