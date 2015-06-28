package io.protostuff.compiler.parser;

import com.google.common.base.Preconditions;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.SetMultimap;
import io.protostuff.compiler.model.Extension;

import java.util.Collection;

/**
 * @author Kostiantyn Shchepanovskyi
 */
public class LocalExtensionRegistry extends AbstractExtensionRegistry {

    private final SetMultimap<String, Extension> extensions = HashMultimap.create();


    @Override
    public void registerExtension(Extension extension) {
        String fullName = extension.getExtendee().getFullName();
        Preconditions.checkNotNull(fullName);
        extensions.put(fullName, extension);
    }

    @Override
    public Collection<Extension> getExtensions(String messageName) {
        return extensions.get(messageName);
    }

}
