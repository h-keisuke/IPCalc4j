package ipcalc4j.impl;

import ipcalc4j.IpAddress;
import ipcalc4j.IpAddressRange;

import java.util.Optional;
import java.util.stream.Stream;

public class IpAddressImpl implements IpAddress,Comparable<IpAddressImpl>{

  private final int address;
  private final int mask;

  public IpAddressImpl(final int address, final int mask){
    this.address = address;
    this.mask = mask;
  }

  public int getAddress(){
    return address;
  }

  public int getMask(){
    return mask;
  }

  public boolean isNetwork(){
    return address == (address & mask);
  }

  @Override
  public boolean isBroadcast() {
    return address == (address | ~mask);
  }

  public boolean sameNetwork(IpAddress that) {
    if( this.networkAddress().equals(that.networkAddress())) return true;
    return false;
  }

  public IpAddress networkAddress(){
    if(isNetwork()){
      return this;
    }else {
      return new IpAddressImpl(address & mask, mask);
    }
  }

  public IpAddress broadcastAddress(){
    return new IpAddressImpl(address | ~mask, mask);
  }

  public Stream<IpAddress> hostsAddresses(){
    return IpAddressRange.fromTo(networkAddress().next().get(),broadcastAddress().forward().get());
  }

  public Optional<IpAddress> next(){
    final IpAddress next = new IpAddressImpl(address+1, mask);
    if(this.sameNetwork(next)) {
      return Optional.of(next);
    }else{
      return Optional.empty();
    }
  }

  public Optional<IpAddress> forward(){
    final IpAddress forward = new IpAddressImpl(address-1, mask);
    if(this.sameNetwork(forward)) {
      return Optional.of(forward);
    }else{
      return Optional.empty();
    }
  }

  public long hostCount(){
    return hostsAddresses().count();
  }

  @Override
  public String toString() {
    return IpAddress.formatToDottedDecimal(address) + "/" + IpAddress.maskToCidr(mask);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    IpAddressImpl ipAddress = (IpAddressImpl) o;

    return address == ipAddress.address && mask == ipAddress.mask;

  }

  @Override
  public int hashCode() {
    int result = address;
    result = 31 * result + mask;
    return result;
  }

  @Override
  public int compareTo(IpAddressImpl that) {
    if(this.mask == that.mask){
      return this.address - that.address;
    }else{
      throw new IllegalArgumentException("not same mask");
    }
  }
}
