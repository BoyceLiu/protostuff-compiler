package io.protostuff.parser;

import com.google.common.base.MoreObjects;
import com.google.common.collect.ImmutableList;
import com.google.common.reflect.AbstractInvocationHandler;
import com.google.common.reflect.Reflection;
import org.antlr.v4.runtime.tree.ParseTreeListener;

import javax.annotation.ParametersAreNonnullByDefault;
import java.lang.reflect.Method;

/**
 * @author Kostiantyn Shchepanovskyi
 */
public class CompositeParseTreeListenerFactory {

    @SafeVarargs
    public static <T extends ParseTreeListener> T create(Class<T> type, T... delegates) {
        ImmutableList<T> listeners = ImmutableList.copyOf(delegates);
        return Reflection.newProxy(type, new AbstractInvocationHandler() {

            @Override
            @ParametersAreNonnullByDefault
            protected Object handleInvocation(Object proxy, Method method, Object[] args) throws Throwable {
                for (T listener : listeners) {
                    method.invoke(listener, args);
                }
                return null;
            }

            @Override
            public String toString() {
                return MoreObjects.toStringHelper("CompositeParseTreeListener")
                        .add("listeners", listeners)
                        .toString();
            }
        });

    }
}
