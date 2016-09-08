package io.protostuff.compiler.parser;

import io.protostuff.compiler.model.Extension;
import io.protostuff.compiler.model.Import;
import io.protostuff.compiler.model.Proto;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author Kostiantyn Shchepanovskyi
 */
public final class ProtoExtensionRegistry extends AbstractExtensionRegistry {

    private final ExtensionRegistry localExtensionRegistry;
    private final ProtoContext context;
    private final ConcurrentMap<String, Collection<Extension>> extensionCache =
            new ConcurrentHashMap<String, Collection<Extension>>();
    private Proto proto;

    public ProtoExtensionRegistry(ProtoContext context) {
        this.context = context;
        this.proto = context.getProto();
        this.localExtensionRegistry = new LocalExtensionRegistry();
    }

    @Override
    public void registerExtension(Extension extension) {
        super.registerExtension(extension);
        localExtensionRegistry.registerExtension(extension);
        String fullyQualifiedName = extension.getExtendee().getFullyQualifiedName();
        extensionCache.remove(fullyQualifiedName);
    }

    @Override
    public Collection<Extension> getExtensions(String fullMessageName) {
        if (extensionCache.containsKey(fullMessageName)) {
            return extensionCache.get(fullMessageName);
        } else {
            Collection<Extension> result = new ArrayList<Extension>();
            result.addAll(localExtensionRegistry.getExtensions(fullMessageName));
            Deque<Import> queue = new ArrayDeque<Import>();
            queue.addAll(proto.getImports());
            while (!queue.isEmpty()) {
                Import anImport = queue.poll();
                Proto proto = anImport.getProto();
                Collection<Extension> extensions = getExtensions(proto, fullMessageName);
                result.addAll(extensions);
                queue.addAll(proto.getPublicImports());
            }
            extensionCache.put(fullMessageName, result);
            return result;
        }
    }

    private Collection<Extension> getExtensions(Proto proto, String name) {
        ProtoContext context = proto.getContext();
        ExtensionRegistry extensionRegistry = context.getExtensionRegistry();
        return extensionRegistry.getExtensions(name);
    }

}
