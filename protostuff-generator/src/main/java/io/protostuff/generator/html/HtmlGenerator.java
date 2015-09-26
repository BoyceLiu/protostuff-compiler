package io.protostuff.generator.html;

import org.pegdown.Extensions;
import org.pegdown.PegDownProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.stringtemplate.v4.AttributeRenderer;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;

import io.protostuff.compiler.model.Module;
import io.protostuff.generator.CompilerUtils;
import io.protostuff.generator.ObjectExtender;
import io.protostuff.generator.ProtoCompiler;
import io.protostuff.generator.StCompilerFactory;

/**
 * @author Kostiantyn Shchepanovskyi
 */
public class HtmlGenerator implements ProtoCompiler {

    public static final String GENERATOR_NAME = "html";
    public static final String HTML_RESOURCE_BASE = "io/protostuff/generator/html/";
    public static final String[] STATIC_RESOURCES = new String[]{
            "fonts/glyphicons-halflings-regular.woff2",
            "fonts/glyphicons-halflings-regular.svg",
            "fonts/glyphicons-halflings-regular.woff",
            "fonts/glyphicons-halflings-regular.eot",
            "fonts/glyphicons-halflings-regular.ttf",
            "js/jquery.min.js",
            "js/jquery.js",
            "js/jquery.min.map",
            "js/bootstrap.min.js",
            "js/bootstrap.js",
            "js/bootstrap-treeview.js",
            "js/bootstrap-treeview.min.js",
            "js/main.js",
            "css/bootstrap-theme.css",
            "css/bootstrap-theme.min.css",
            "css/bootstrap-theme.css.map",
            "css/bootstrap.min.css",
            "css/bootstrap.css.map",
            "css/bootstrap.css",
            "css/bootstrap-treeview.css",
            "css/bootstrap-treeview.min.css",
            "css/theme.css",
            "scalar-value-types.html",
    };
    private static final Logger LOGGER = LoggerFactory.getLogger(HtmlGenerator.class);
    private final StCompilerFactory compilerFactory;
    private final CompilerUtils compilerUtils;

    @Inject
    public HtmlGenerator(StCompilerFactory compilerFactory, CompilerUtils compilerUtils) {
        this.compilerFactory = compilerFactory;
        this.compilerUtils = compilerUtils;
    }

    @Override
    public String getName() {
        return GENERATOR_NAME;
    }

    @Override
    public void compile(Module module) {
        // TODO generator should not be initialized for each module separately
        MarkdownRenderer renderer = new MarkdownRenderer();
        Map<Class<?>, AttributeRenderer> rendererMap = new HashMap<>();
        rendererMap.put(List.class, renderer);
        rendererMap.put(String.class, renderer);
        Map<Class<?>, ObjectExtender<?>> extenderMap = new HashMap<>();
        ProtoCompiler indexGenerator = compilerFactory.create("io/protostuff/generator/html/index.stg", rendererMap, extenderMap);
        indexGenerator.compile(module);
        ProtoCompiler messageGenerator = compilerFactory.create("io/protostuff/generator/html/message.stg", rendererMap, extenderMap);
        messageGenerator.compile(module);
        ProtoCompiler enumGenerator = compilerFactory.create("io/protostuff/generator/html/enum.stg", rendererMap, extenderMap);
        enumGenerator.compile(module);
        ProtoCompiler serviceGenerator = compilerFactory.create("io/protostuff/generator/html/service.stg", rendererMap, extenderMap);
        serviceGenerator.compile(module);
        ProtoCompiler mainGenerator = compilerFactory.create("io/protostuff/generator/html/main.stg", rendererMap, extenderMap);
        mainGenerator.compile(module);
        for (String staticResourceName : STATIC_RESOURCES) {
            String source = HTML_RESOURCE_BASE + "static/" + staticResourceName;
            //noinspection UnnecessaryLocalVariable
            String destination = module.getOutput() + "/" + staticResourceName;
            LOGGER.info("Write {}", destination);
            compilerUtils.copyResource(source, destination);
        }
    }

    public static class MarkdownRenderer implements AttributeRenderer {

        @Override
        @SuppressWarnings("unchecked")
        public String toString(Object o, String s, Locale locale) {
            if ("markdown2html".equals(s)) {
                if (o == null) {
                    return "";
                }
                int options = 0;
                options |= Extensions.SMARTYPANTS;
                options |= Extensions.AUTOLINKS;
                options |= Extensions.TABLES;
                options |= Extensions.FENCED_CODE_BLOCKS;
                // TODO enable when fix for https://github.com/sirthias/pegdown/issues/161 released
                // options |= Extensions.WIKILINKS;
                // options |= Extensions.STRIKETHROUGH;
                options |= Extensions.SUPPRESS_ALL_HTML;
                PegDownProcessor processor = new PegDownProcessor(options);
                return processor.markdownToHtml(o.toString());
            }
            return String.valueOf(o);
        }
    }
}
