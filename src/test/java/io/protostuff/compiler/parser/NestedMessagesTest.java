package io.protostuff.compiler.parser;

import com.google.inject.Guice;
import com.google.inject.Injector;
import io.protostuff.compiler.ParserModule;
import io.protostuff.compiler.model.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test for nested messages and enums.
 * See {@code test/nested_messages/test.proto}
 *
 * @author Kostiantyn Shchepanovskyi
 */
public class NestedMessagesTest {

    private Injector injector;

    @Before
    public void setUp() throws Exception {
        injector = Guice.createInjector(new ParserModule());
    }

    @Test
    public void test() throws Exception {
        Importer importer = injector.getInstance(Importer.class);
        ProtoContext context = importer.importFile("protostuff_unittest/messages_sample.proto");
        Proto proto = context.getProto();
        assertNotNull(proto);
        assertEquals("protostuff_unittest/messages_sample.proto", proto.getFilename());
        assertEquals("messages_sample", proto.getName());
        assertNotNull(proto.getSyntax());
        assertEquals("proto3", proto.getSyntax().getValue());
        assertEquals("protostuff_unittest", proto.getPackage().getValue());

        Message a = proto.getMessage("A");
        assertNotNull(a);
        assertEquals("A", a.getName());
        assertEquals(".protostuff_unittest.A", a.getFullName());
        assertTrue(proto == a.getProto());
        assertFalse(a.isNested());
        assertTrue(a.getParent() == proto);

        io.protostuff.compiler.model.Enum e1 = proto.getEnum("E");
        assertNotNull(e1);
        assertEquals("E", e1.getName());
        assertEquals(".protostuff_unittest.E", e1.getFullName());
        assertTrue(proto == e1.getProto());
        assertFalse(e1.isNested());
        assertTrue(e1.getParent() == proto);

        Message b = a.getMessage("B");
        assertNotNull(b);
        assertEquals("B", b.getName());
        assertEquals(".protostuff_unittest.A.B", b.getFullName());
        assertTrue(proto == b.getProto());
        assertTrue(b.isNested());
        assertTrue(b.getParent() == a);

        Message c = b.getMessage("C");
        assertNotNull(c);
        assertEquals("C", c.getName());
        assertEquals(".protostuff_unittest.A.B.C", c.getFullName());
        assertTrue(proto == c.getProto());
        assertTrue(c.isNested());
        assertTrue(c.getParent() == b);

        Message d = c.getMessage("D");
        assertNotNull(d);
        assertEquals("D", d.getName());
        assertEquals(".protostuff_unittest.A.B.C.D", d.getFullName());
        assertTrue(proto == d.getProto());
        assertTrue(d.isNested());
        assertTrue(d.getParent() == c);

        io.protostuff.compiler.model.Enum e = d.getEnum("E");
        assertNotNull(e);
        assertEquals("E", e.getName());
        assertEquals(".protostuff_unittest.A.B.C.D.E", e.getFullName());
        assertTrue(proto == e.getProto());
        assertTrue(e.isNested());
        assertTrue(e.getParent() == d);
    }
}
