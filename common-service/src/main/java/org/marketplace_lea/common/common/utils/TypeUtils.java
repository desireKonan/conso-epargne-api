package org.marketplace_lea.common.common.utils;

import org.marketplace_lea.common.entities.parameters.AppConfig;
import org.marketplace_lea.common.entities.parameters.ParameterType;

public final class TypeUtils {
    public static boolean booleanValue(AppConfig config) {
        if(ParameterType.BOOLEAN.name().equals(config.getDataType())) {
            return Boolean.parseBoolean(config.getValue());
        }
        return false;
    }

    public static String stringValue(AppConfig config) {
        if (ParameterType.STRING.name().equals(config.getDataType())) {
            return config.getValue();
        }
        return "";
    }

    public static float floatValue(AppConfig config) {
        if (ParameterType.FLOAT.name().equals(config.getDataType())) {
            return Float.parseFloat(config.getValue());
        }
        return 0.0f;
    }

    public static int intValue(AppConfig config) {
        if (ParameterType.FLOAT.name().equals(config.getDataType())) {
            return Integer.parseInt(config.getValue());
        }
        return 0;
    }
}
