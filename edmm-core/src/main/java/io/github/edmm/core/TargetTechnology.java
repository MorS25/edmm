package io.github.edmm.core;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Getter
@Builder
public final class TargetTechnology {

    public static final TargetTechnology NOOP = TargetTechnology.builder().id("noop").name("noop").build();

    private final String id;
    private final String name;

    @JsonCreator
    public TargetTechnology(@JsonProperty("id") @NonNull String id, @JsonProperty("name") @NonNull String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        TargetTechnology targetTechnology = (TargetTechnology) object;
        return Objects.equals(id, targetTechnology.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
