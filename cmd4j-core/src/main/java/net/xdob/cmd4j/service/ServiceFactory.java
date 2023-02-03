package net.xdob.cmd4j.service;

import net.xdob.cmd4j.annotation.Rank;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public interface ServiceFactory {
  <T> T getBean(Class<T> clazz);
  <T> List<T> getBeanList(Class<T> clazz);

  /**
   * bean 排序比较器
   * @param <T>
   * @return
   */
  default <T> Comparator<T> beanComparator() {
    return Comparator.comparingInt(e -> (e instanceof RankSupport) ? ((RankSupport) e).rank() : Optional.ofNullable(e.getClass().getAnnotation(Rank.class)).map(r -> r.value()).orElse(0));
  }
}
