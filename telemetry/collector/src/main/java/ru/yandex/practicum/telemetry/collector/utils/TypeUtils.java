package ru.yandex.practicum.telemetry.collector.utils;

import lombok.experimental.UtilityClass;
import ru.yandex.practicum.telemetry.collector.service.handler.EventHandler;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

@UtilityClass
public class TypeUtils {

    public static Class<?> getEventTypeFromHandler(EventHandler<?, ?, ?> handler) {
        Class<?> eventTypeClass = handler.getClass();
        while (eventTypeClass != null && eventTypeClass != Object.class) {
            Type genericSuperclass = eventTypeClass.getGenericSuperclass();

            if (genericSuperclass instanceof ParameterizedType parameterizedType) {
                Type rawType = parameterizedType.getRawType();

                if (rawType instanceof Class<?> rawClass &&
                        EventHandler.class.isAssignableFrom(rawClass)) {
                    Type firstType = parameterizedType.getActualTypeArguments()[0];

                    if (firstType instanceof Class<?> firstClass) {
                        return firstClass;
                    }

                    if (firstType instanceof ParameterizedType pt) {
                        Type raw = pt.getRawType();
                        if (raw instanceof Class<?> rawFirstClass) {
                            return rawFirstClass;
                        }
                    }
                }

            }

            eventTypeClass = eventTypeClass.getSuperclass();
        }
        throw new IllegalStateException("Cannot determine event type for handler: " + handler.getClass());
    }
}
