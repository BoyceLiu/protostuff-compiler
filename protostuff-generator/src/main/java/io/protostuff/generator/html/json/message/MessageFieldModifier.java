package io.protostuff.generator.html.json.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.ImmutableMap;

import javax.annotation.Nonnull;
import java.util.Map;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author Kostiantyn Shchepanovskyi
 */
public enum MessageFieldModifier {

    @JsonProperty("optional")
    OPTIONAL,

    @JsonProperty("required")
    REQUIRED,

    @JsonProperty("repeated")
    REPEATED;

    private static final Map<String, MessageFieldModifier> MODIFIER_BY_PROTO_NAME =
            ImmutableMap.<String, MessageFieldModifier>builder()
                    .put("optional", OPTIONAL)
                    .put("required", REQUIRED)
                    .put("repeated", REPEATED)
                    .build();

    @Nonnull
    public static MessageFieldModifier fromString(String s) {
        checkNotNull(s);
        MessageFieldModifier modifier = MODIFIER_BY_PROTO_NAME.get(s);
        checkArgument(modifier != null, s);
        return modifier;
    }
}
