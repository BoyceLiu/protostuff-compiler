package io.protostuff.generator.html.json.message;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import org.immutables.value.Value;

import java.util.List;

import javax.annotation.Nullable;

import io.protostuff.generator.html.json.index.NodeType;

/**
 * @author Kostiantyn Shchepanovskyi
 */
@Value.Immutable
@JsonSerialize(as = ImmutableMessageDescriptor.class)
@JsonDeserialize(as = ImmutableMessageDescriptor.class)
public interface MessageDescriptor {

    String name();

    NodeType type();

    String canonicalName();

    @Nullable
    String description();

    List<MessageField> fields();

}
