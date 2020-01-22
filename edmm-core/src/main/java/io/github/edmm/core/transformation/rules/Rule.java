package io.github.edmm.core.transformation.rules;

import java.util.Objects;

import io.github.edmm.model.component.RootComponent;
import lombok.Getter;

public abstract class Rule<T extends RootComponent> implements Comparable<Rule> {

    @Getter
    private String name;

    private final Class<T> applicableType;

    public Rule(Class<T> applicableType) {
        this.applicableType = applicableType;
        this.name = this.getClass().getSimpleName();
    }

    public boolean isApplicable(RootComponent component) {
        return component.getClass().isAssignableFrom(applicableType);
    }

    public abstract boolean evaluate(T component);

    public abstract void execute(T component) throws Exception;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rule that = (Rule) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public int compareTo(Rule rule) {
        return getName().compareTo(rule.getName());
    }
}
