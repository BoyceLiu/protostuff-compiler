package io.protostuff.compiler.generator;

import io.protostuff.compiler.model.Proto;

/**
 * @author Kostiantyn Shchepanovskyi
 */
public interface ProtoCompiler {

    void compile(Proto proto);
}
