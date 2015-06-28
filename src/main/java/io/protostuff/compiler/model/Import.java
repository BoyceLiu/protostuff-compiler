package io.protostuff.compiler.model;

import java.util.Objects;

/**
 * @author Kostiantyn Shchepanovskyi
 */
public final class Import extends AbstractElement {

    private final String value;
    private final boolean aPublic;
    private Proto proto;

    public Import(String value, boolean aPublic) {
        this.value = value;
        this.aPublic = aPublic;
    }

    public String getValue() {
        return value;
    }

    public boolean isPublic() {
        return aPublic;
    }

    public Proto getProto() {
        return proto;
    }

    public void setProto(Proto proto) {
        this.proto = proto;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Import anImport = (Import) o;
        return Objects.equals(aPublic, anImport.aPublic) &&
                Objects.equals(value, anImport.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, aPublic);
    }

    @Override
    public String toString() {
        return value;
    }
}
