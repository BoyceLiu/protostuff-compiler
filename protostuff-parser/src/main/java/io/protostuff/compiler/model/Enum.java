package io.protostuff.compiler.model;

import com.google.common.base.MoreObjects;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Kostiantyn Shchepanovskyi
 */
public class Enum extends AbstractUserType {

    protected List<EnumConstant> constants;

    public Enum(UserTypeContainer parent) {
        super(parent);
    }

    public List<EnumConstant> getConstants() {
        if (constants == null) {
            return Collections.emptyList();
        }
        return constants;
    }

    public void setConstants(List<EnumConstant> constants) {
        this.constants = constants;
    }

    public Set<String> getConstantNames() {
        if (constants == null) {
            return Collections.emptySet();
        }
        return constants.stream()
                .map(EnumConstant::getName)
                .collect(Collectors.toSet());
    }

    public EnumConstant getConstant(String name) {
        for (EnumConstant enumConstant : getConstants()) {
            if (enumConstant.getName().equals(name)) {
                return enumConstant;
            }
        }
        return null;
    }

    public void addConstant(EnumConstant value) {
        if (constants == null) {
            constants = new ArrayList<>();
        }
        constants.add(value);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .omitNullValues()
                .add("name", name)
                .add("fullyQualifiedName", fullyQualifiedName)
                .add("constants", constants)
                .add("options", options)
                .toString();
    }

    @Override
    public DescriptorType getDescriptorType() {
        return DescriptorType.ENUM;
    }


    @Override
    public boolean isScalar() {
        return true;
    }

    @Override
    public boolean isEnum() {
        return true;
    }

    @Override
    public boolean isMessage() {
        return false;
    }
}
