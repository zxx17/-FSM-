package org.zxx17.logistics.common.util.str;


import java.util.Random;

/**
 * .
 *
 * @author Xinxuan Zhuo
 * @version 1.0.0
 * @since 2024/6/12
 **/
public class StringUtils {

  private static final String[] NAMES = {
      "张三", "李四", "王五", "赵六",
      "孙七", "周八", "吴九", "郑十", "王十一", "钱十二"
  };


  private static final Random RANDOM = new Random();

  public static String randomName() {
    return NAMES[RANDOM.nextInt(NAMES.length)];
  }

}
