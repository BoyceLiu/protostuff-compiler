package io.protostuff.compiler.parser;

import io.protostuff.compiler.model.*;
import io.protostuff.compiler.model.Enum;
import io.protostuff.compiler.model.Package;
import org.antlr.v4.runtime.ANTLRErrorListener;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import javax.inject.Inject;
import java.util.*;
import java.util.function.Consumer;

/**
 * @author Kostiantyn Shchepanovskyi
 */
public class FileDescriptorLoaderImpl implements FileDescriptorLoader {

    private final ANTLRErrorListener errorListener;
    private final Importer importer;
    private final Set<ProtoContextPostProcessor> postProcessors;

    @Inject
    public FileDescriptorLoaderImpl(Importer importer, ANTLRErrorListener errorListener, Set<ProtoContextPostProcessor> postProcessors) {
        this.errorListener = errorListener;
        this.importer = importer;
        this.postProcessors = postProcessors;
    }

    @Override
    public ProtoContext load(String name, CharStream stream) {
        ProtoContext context = parse(name, stream);

        for (Import anImport : context.getProto().getImports()) {
            ProtoContext importedContext = importer.importFile(anImport.getValue());
            if (anImport.isPublic()) {
                context.addPublicImport(importedContext);
            } else {
                context.addImport(importedContext);
            }
            anImport.setProto(importedContext.getProto());
        }

        for (ProtoContextPostProcessor postProcessor : postProcessors) {
            postProcessor.process(context);
        }

        context.setInitialized(true);
        return context;
    }

    private ProtoContext parse(String filename, CharStream stream) {
        ProtoLexer lexer = new ProtoLexer(stream);
        lexer.removeErrorListeners();
        lexer.addErrorListener(errorListener);
        CommonTokenStream tokenStream = new CommonTokenStream(lexer);
        ProtoParser parser = new ProtoParser(tokenStream);
        parser.removeErrorListeners();
        parser.addErrorListener(errorListener);

        ProtoParser.ProtoContext tree = parser.proto();
        ProtoContext context = new ProtoContext(filename);
        ProtoParserListener composite = CompositeParseTreeListener.create(ProtoParserListener.class,
                new ProtoParseListener(context),
                new MessageParseListener(context),
                new EnumParseListener(context),
                new OptionParseListener(context),
                new ServiceParseListener(context)
        );
        ParseTreeWalker.DEFAULT.walk(composite, tree);
        int numberOfSyntaxErrors = parser.getNumberOfSyntaxErrors();
        if (numberOfSyntaxErrors > 0) {
            String format = "Could not parse %s: %d syntax errors found";
            throw new ParserException(format, filename, numberOfSyntaxErrors);
        }
        return context;
    }

}
