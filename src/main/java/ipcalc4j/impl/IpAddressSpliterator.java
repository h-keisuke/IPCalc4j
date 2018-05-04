package ipcalc4j.impl;

import ipcalc4j.IpAddress;

import java.util.Comparator;
import java.util.Spliterator;
import java.util.function.Consumer;

public class IpAddressSpliterator implements Spliterator<IpAddress> {
  private final int end;
  private final int mask;
  private int origin;

  @Override
  public Comparator<? super IpAddress> getComparator() {
    return (t0, t1) -> {
      if(t0.getMask() == t1.getMask()){
        return t0.getAddress() - t1.getAddress();
      }else{
        throw new IllegalArgumentException("not same mask");
      }
    };
  }

  public IpAddressSpliterator(final int start, final int end, final int mask){
    this.end = end;
    this.mask = mask;
    this.origin = start;
  }

  @Override
  public boolean tryAdvance(Consumer<? super IpAddress> action) {
    if(origin <= end){
      action.accept(new IpAddressImpl(origin,mask));
      origin ++;
      return true;
    }else{
      return false;
    }
  }

  @Override
  public Spliterator<IpAddress> trySplit() {
    final int lo = origin;
    final int half = (end - lo) / 2;
    if(half <= 0) return null;
    origin += half + 1;
    return new IpAddressSpliterator(lo, lo+half, mask);
  }

  @Override
  public long estimateSize() {
    return (end - origin) / 2;
  }

  @Override
  public int characteristics() {
    return DISTINCT | NONNULL | ORDERED | SIZED | SORTED | SUBSIZED;
  }

}
