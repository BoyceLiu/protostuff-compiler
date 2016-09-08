package io.protostuff.generator;

/**
 * @author Kostiantyn Shchepanovskyi
 */
public interface PropertyProvider<ObjectT> {

    boolean hasProperty(String propertyName);

    Object getProperty(ObjectT object, String propertyName);

    void register(String property, ComputableProperty<ObjectT, ?> function);
}
