package io.protostuff.compiler.model;

import java.util.List;

/**
 * @author Kostiantyn Shchepanovskyi
 */
public interface EnumContainer {

    List<Enum> getEnums();

    void addEnum(Enum e);
}
