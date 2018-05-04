package ipcalc4j;

import ipcalc4j.impl.IpAddressImpl;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public interface IpAddress{
  static IpAddress of(final String dottedDecimal, final int cidr){
    return new IpAddressImpl(parseAddress(dottedDecimal), cidrToMask(cidr));
  }

  static IpAddress of(final String dottedDecimal, final String mask){
    return new IpAddressImpl(parseAddress(dottedDecimal), parseAddress(mask));
  }

  int getAddress();
  int getMask();
  boolean isNetwork();
  boolean isBroadcast();
  IpAddress networkAddress();
  IpAddress broadcastAddress();
  Optional<IpAddress> next();
  Optional<IpAddress> forward();
  boolean sameNetwork(final IpAddress that);
  long hostCount();
  Stream<IpAddress> hostsAddresses();

  int[] bases = new int[]{24, 16, 8, 0};
  static int parseAddress(final String dottedDecimal){
    int[] octet = Arrays.stream(dottedDecimal.split("\\.",4))
        .mapToInt(Integer::parseInt)
        .toArray();
    return octet[0] << bases[0] | octet[1] << bases[1] | octet[2] << bases[2] | octet[3] << bases[3];
  }

  static String formatToDottedDecimal(final int address){
    return
        Integer.toString(address >> bases[0]) + "." +
            Integer.toString(address >> bases[1] & 255) + "." +
            Integer.toString(address >> bases[2] & 255) + "." +
            Integer.toString(address >> bases[3] & 255);
  }

  static int cidrToMask(final int cidr) {
    return (-1) << (32 - cidr);
  }

  static int maskToCidr(final int mask){
    return IntStream.rangeClosed(32,0)
        .filter(c -> cidrToMask(c) == mask)
        .findFirst()
        .getAsInt();
  }


}
