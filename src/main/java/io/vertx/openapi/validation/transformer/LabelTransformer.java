package io.vertx.openapi.validation.transformer;

import io.vertx.openapi.contract.Parameter;

import static io.vertx.openapi.validation.ValidatorException.createInvalidValueFormat;

/**
 * +--------+---------+--------+-------------+------------------------------+-------------------------------+
 * | style  | explode | empty  | string      | array                        | object                        |
 * +--------+---------+--------+-------------+------------------------------+-------------------------------+
 * | label  | false   | .      | .blue       | .blue,black,brown (RFC-6570) | .R,100,G,200,B,150            |
 * +--------+---------+--------+-------------+------------------------------+-------------------------------+
 * | label  | true    | .      | .blue       | .blue.black.brown            | .R=100.G=200.B=150            |
 * +--------+---------+--------+-------------+------------------------------+-------------------------------+
 */
public class LabelTransformer extends ParameterTransformer {

  private static boolean startsWithDot(String value) {
    return '.' == value.charAt(0);
  }

  @Override
  public Object transform(Parameter parameter, String rawValue) {
    if (!rawValue.isEmpty() && startsWithDot(rawValue)) {
      return super.transform(parameter, rawValue.substring(1));
    } else {
      throw createInvalidValueFormat(parameter);
    }
  }

  @Override protected String[] getArrayValues(Parameter parameter, String rawValue) {
    return parameter.isExplode() ? rawValue.split("\\.") : rawValue.split(",");
  }

  @Override protected String[] getObjectKeysAndValues(Parameter parameter, String rawValue) {
    return parameter.isExplode() ? rawValue.split("[=|.]") : rawValue.split(",");
  }
}
