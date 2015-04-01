package io.protostuff.parser;

import com.google.common.base.Joiner;
import io.protostuff.proto3.*;
import io.protostuff.proto3.Enum;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.BailErrorStrategy;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Kostiantyn Shchepanovskyi
 */
public class EnumParseListenerTest {

    public static final String ENUM_BLOCK = Joiner.on('\n').join(
            "enum E {",
            "  A = 0;",
            "  B = 0xFF;",
            "  C = 0010;",
            "  D = 1234;",
            "  F = -0xFF;",
            "  G = -0010;",
            "  H = -1234;",
            "}"
    );

    public static final String EMPTY_ENUM_BLOCK = Joiner.on('\n').join(
            "enum E {",
            "}"
    );

    public static final String ENUM_BLOCK_WITH_STANDARD_OPTION = Joiner.on('\n').join(
            "enum E {",
            "  option deprecated = true;",
            "}"
    );

    public static final String ENUM_BLOCK_WITH_CUSTOM_OPTIONS = Joiner.on('\n').join(
            "enum E {",
            "  option (number) = 42;",
            "  option (str) = \"string\";",
            "}"
    );

    public static final String ENUM_BLOCK_WITH_CUSTOM_ENUM_OPTION = Joiner.on('\n').join(
            "enum E {",
            "  option (x) = V;",
            "}"
    );

    public static final String ENUM_BLOCK_WITH_CUSTOM_COMPLEX_OPTIONS = Joiner.on('\n').join(
            "enum E {",
            "  option (core.foo).a = 1;",
            "  option (core.foo).b = 2;",
            "  option (core.foo).c.d = 3;",
            "}"
    );

    public static final String ENUM_BLOCK_WITH_CUSTOM_TEXT_FORMAT_OPTIONS = Joiner.on('\n').join(
            "enum E {",
            "  option (core.foo) = {" +
            "    a: 1" +
            "    b: 2" +
            "    c: {" +
            "      d: 3" +
            "    }" +
            "  };",
            "}"
    );

    public static final String ENUM_BLOCK_WITH_STANDARD_FIELD_OPTION = Joiner.on('\n').join(
            "enum E {",
            "  A = 0 [deprecated=true];",
            "}"
    );

    @Test
    public void parseEnum() throws Exception {
        Enum result = parseEnumBlock(ENUM_BLOCK);
        assertEquals("E", result.getName());
        assertEquals(7, result.getValues().size());
        EnumConstant a = result.getValues().get(0);
        assertEquals("A", a.getName());
        assertEquals(0, a.getValue());
        EnumConstant b = result.getValues().get(1);
        assertEquals("B", b.getName());
        assertEquals(255, b.getValue());
        EnumConstant c = result.getValues().get(2);
        assertEquals("C", c.getName());
        assertEquals(8, c.getValue());
        EnumConstant d = result.getValues().get(3);
        assertEquals("D", d.getName());
        assertEquals(1234, d.getValue());
        EnumConstant f = result.getValues().get(4);
        assertEquals("F", f.getName());
        assertEquals(-255, f.getValue());
        EnumConstant g = result.getValues().get(5);
        assertEquals("G", g.getName());
        assertEquals(-8, g.getValue());
        EnumConstant h = result.getValues().get(6);
        assertEquals("H", h.getName());
        assertEquals(-1234, h.getValue());
    }

    @Test
    public void parseEmptyEnum() throws Exception {
        Enum result = parseEnumBlock(EMPTY_ENUM_BLOCK);
        assertEquals("E", result.getName());
        assertEquals(0, result.getValues().size());
    }

    @Test
    public void parseEnumWithStandardOption() throws Exception {
        Enum result = parseEnumBlock(ENUM_BLOCK_WITH_STANDARD_OPTION);
        assertEquals("E", result.getName());
        assertEquals(1, result.getStandardOptions().size());
        assertEquals(true, result.getStandardOptions().get("deprecated"));
    }

    @Test
     public void parseEnumWithCustomOption() throws Exception {
        Enum result = parseEnumBlock(ENUM_BLOCK_WITH_CUSTOM_OPTIONS);
        assertEquals("E", result.getName());
        assertEquals(2, result.getCustomOptions().size());
        assertEquals(42, result.getCustomOptions().get("number"));
        assertEquals("string", result.getCustomOptions().get("str"));
    }

    @Test
    public void parseEnumWithCustomEnumOption() throws Exception {
        Enum result = parseEnumBlock(ENUM_BLOCK_WITH_CUSTOM_ENUM_OPTION);
        assertEquals("E", result.getName());
        assertEquals(1, result.getCustomOptions().size());
        assertEquals("V", result.getCustomOptions().get("x"));
    }

    @Test
    public void parseEnumWithCustomComplexOption() throws Exception {
        Enum result = parseEnumBlock(ENUM_BLOCK_WITH_CUSTOM_COMPLEX_OPTIONS);
        assertEquals("E", result.getName());
        Map<String, Object> customOptions = result.getCustomOptions();
        assertEquals(1, customOptions.size());
        assertTrue(customOptions.get("core.foo") instanceof Map);
        assertEquals(1, ((Map) customOptions.get("core.foo")).get("a"));
        assertEquals(2, ((Map) customOptions.get("core.foo")).get("b"));
        assertEquals(3, ((Map)((Map) customOptions.get("core.foo")).get("c")).get("d"));
    }

    @Test
    public void parseEnumWithCustomTextFormatOption() throws Exception {
        Enum result = parseEnumBlock(ENUM_BLOCK_WITH_CUSTOM_TEXT_FORMAT_OPTIONS);
        assertEquals("E", result.getName());
        Map<String, Object> customOptions = result.getCustomOptions();
        assertEquals(1, customOptions.size());
        assertTrue(customOptions.get("core.foo") instanceof Map);
        assertEquals(1, ((Map) customOptions.get("core.foo")).get("a"));
        assertEquals(2, ((Map) customOptions.get("core.foo")).get("b"));
        assertEquals(3, ((Map)((Map) customOptions.get("core.foo")).get("c")).get("d"));
    }

    @Test
    public void parseEnumWithStandardFieldOption() throws Exception {
        Enum result = parseEnumBlock(ENUM_BLOCK_WITH_STANDARD_FIELD_OPTION);
        assertEquals("E", result.getName());
        assertEquals(0, result.getStandardOptions().size());
        assertEquals(1, result.getValues().get(0).getStandardOptions().size());
        assertEquals(true, result.getValues().get(0).getStandardOptions().get("deprecated"));
    }

    private Enum parseEnumBlock(String input) {
        CharStream stream = new ANTLRInputStream(input);
        Proto3Lexer lexer = new Proto3Lexer(stream);
        lexer.removeErrorListeners();
        lexer.addErrorListener(TestUtils.ERROR_LISTENER);
        CommonTokenStream tokenStream = new CommonTokenStream(lexer);
        Proto3Parser parser = new Proto3Parser(tokenStream);
        parser.setErrorHandler(new BailErrorStrategy());
        parser.removeErrorListeners();
        parser.addErrorListener(TestUtils.ERROR_LISTENER);
        Context context = new Context();
        FileDescriptor fileDescriptor = new FileDescriptor();
        context.push(fileDescriptor);
        EnumParseListener enumParseListener = new EnumParseListener(context);
        OptionParseListener optionParseListener = new OptionParseListener(context);
        parser.addParseListener(enumParseListener);
        parser.addParseListener(optionParseListener);
        parser.enumBlock();
        return fileDescriptor.getEnums().get(0);
    }
}
