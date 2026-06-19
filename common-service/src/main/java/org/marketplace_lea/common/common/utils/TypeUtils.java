package org.marketplace_lea.common.common.utils;

import org.marketplace_lea.common.entities.parameters.ParameterConfigEntity;
import org.marketplace_lea.common.entities.parameters.ParameterType;

public final class TypeUtils {
    public static boolean booleanValue(ParameterConfigEntity config) {
        if(ParameterType.BOOLEAN.name().equals(config.getDataType())) {
            return Boolean.parseBoolean(config.getValue());
        }
        return false;
    }

    public static String stringValue(ParameterConfigEntity config) {
        if (ParameterType.STRING.name().equals(config.getDataType())) {
            return config.getValue();
        }
        return "";
    }

    public static float floatValue(ParameterConfigEntity config) {
        if (ParameterType.FLOAT.name().equals(config.getDataType())) {
            return Float.parseFloat(config.getValue());
        }
        return 0.0f;
    }

    public static Double doubleValue(ParameterConfigEntity config) {
        if (ParameterType.DOUBLE.name().equals(config.getDataType())) {
            return Double.valueOf(config.getValue());
        }
        return 0.0;
    }

    public static int intValue(ParameterConfigEntity config) {
        if (ParameterType.INT.name().equals(config.getDataType())) {
            return Integer.parseInt(config.getValue());
        }
        return 0;
    }
}
