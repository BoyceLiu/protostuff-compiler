package io.protostuff.it.java;

import io.protostuff.LinkedBuffer;
import io.protostuff.ProtobufIOUtil;
import io.protostuff.Schema;
import io.protostuff.it.enum_test.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Kostiantyn Shchepanovskyi
 */
@SuppressWarnings("ThrowableResultOfMethodCallIgnored")
public class EnumTest {


    @Test
    public void defaultFieldValue() throws Exception {
        ParentEnumMsg msg = ParentEnumMsg.newBuilder().build();
        assertEquals(NestedEnum.ZERO, msg.getNestedEnum());
    }

    @Test
    public void failWhenSetEnumValueToUnknownInBuilder_singluar() throws Exception {
        assertThrows(IllegalArgumentException.class, () -> {
            ParentEnumMsg.newBuilder()
                    .setNestedEnum(NestedEnum.UNRECOGNIZED)
                    .build();
        });
    }

    @Test
    public void failWhenSetEnumValueToUnknownInBuilder_repeated_add() throws Exception {
        assertThrows(IllegalArgumentException.class, () -> {
            ParentEnumMsg.newBuilder()
                    .addNestedRepeatedEnum(NestedEnum.UNRECOGNIZED)
                    .build();
        });
    }

    @Test
    public void failWhenSetEnumValueToUnknownInBuilder_repeated_set() throws Exception {
        ParentEnumMsg.Builder builder = ParentEnumMsg.newBuilder()
                .addNestedRepeatedEnum(NestedEnum.FIRST);
        assertThrows(IllegalArgumentException.class, () -> {
            builder.setNestedRepeatedEnum(0, NestedEnum.UNRECOGNIZED);
        });
    }

    @Test
    public void failWhenSetEnumValueToUnknownInBuilder_repeated_add_all() throws Exception {
        assertThrows(IllegalArgumentException.class, () -> {
            ParentEnumMsg.newBuilder()
                    .addAllNestedRepeatedEnum(Collections.singletonList(NestedEnum.UNRECOGNIZED))
                    .build();
        });
    }

    @Test
    public void failWhenSetEnumValueToUnknownInBuilder_oneof() throws Exception {
        assertThrows(IllegalArgumentException.class, () -> {
            ParentEnumMsg.newBuilder()
                    .setFirst(NestedEnum.UNRECOGNIZED)
                    .build();
        });
    }

    @Test
    public void enumValueTest_normalField() throws Exception {
        ParentEnumMsg msg = ParentEnumMsg.newBuilder()
                .setNestedEnumValue(42)
                .build();
        assertTrue(msg.hasNestedEnum());
        assertEquals(NestedEnum.UNRECOGNIZED, msg.getNestedEnum());
        assertEquals(42, msg.getNestedEnumValue());
    }

    @Test
    public void enumValueTest_oneofField() throws Exception {
        ParentEnumMsg msg = ParentEnumMsg.newBuilder()
                .setFirstValue(42)
                .build();
        assertTrue(msg.hasFirst());
        assertEquals(NestedEnum.UNRECOGNIZED, msg.getFirst());
        assertEquals(42, msg.getFirstValue());
    }

    @Test
    public void enumValueTest_repeated_adder() throws Exception {
        ParentEnumMsg msg = ParentEnumMsg.newBuilder()
                .addNestedRepeatedEnumValue(42)
                .build();
        assertEquals(NestedEnum.UNRECOGNIZED, msg.getNestedRepeatedEnum(0));
        assertEquals(42, msg.getNestedRepeatedEnumValue(0));
    }

    @Test
    public void enumValueTest_repeated_addAll() throws Exception {
        ParentEnumMsg msg = ParentEnumMsg.newBuilder()
                .addAllNestedRepeatedEnumValue(Collections.singletonList(42))
                .build();
        assertEquals(NestedEnum.UNRECOGNIZED, msg.getNestedRepeatedEnum(0));
        assertEquals(42, msg.getNestedRepeatedEnumValue(0));
    }

    @Test
    public void enumValueTest_repeated_setByIndex() throws Exception {
        ParentEnumMsg msg = ParentEnumMsg.newBuilder()
                .addNestedRepeatedEnum(NestedEnum.FIRST)
                .setNestedRepeatedEnumValue(0, 42)
                .build();
        assertEquals(NestedEnum.UNRECOGNIZED, msg.getNestedRepeatedEnum(0));
        assertEquals(42, msg.getNestedRepeatedEnumValue(0));
    }

    @Test
    public void ordinals() throws Exception {
        assertEquals(0, NestedEnum.ZERO.ordinal());
        assertEquals(1, NestedEnum.FIRST.ordinal());
        assertEquals(2, NestedEnum.SECOND.ordinal());
        assertEquals(3, NestedEnum.HUNDRED.ordinal());
        // Unrecognized goes last
        assertEquals(4, NestedEnum.UNRECOGNIZED.ordinal());
    }

    @Test
    public void proto2_enumDefaultValue() throws Exception {
        Proto2Message message = Proto2Message.getDefaultInstance();
        assertEquals(Proto2EnumDefaultValueNonZero.FIRST, message.getE());
    }

    @Test
    void unknownEnumValue_toString() {
        TestUnknownEnumValue2 source = TestUnknownEnumValue2.newBuilder()
                .setField(TestUnknownEnumValue2.E2.C)
                .build();
        byte[] data = ProtobufIOUtil.toByteArray(source, TestUnknownEnumValue2.getSchema(), LinkedBuffer.allocate());
        Schema<TestUnknownEnumValue1> schema = TestUnknownEnumValue1.getSchema();
        TestUnknownEnumValue1 message = schema.newMessage();
        ProtobufIOUtil.mergeFrom(data, message, schema);
        Assertions.assertEquals("TestUnknownEnumValue1{field=UNRECOGNIZED(2)}", message.toString());
    }
}
