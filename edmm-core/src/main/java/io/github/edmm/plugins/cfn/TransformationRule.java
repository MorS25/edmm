package io.github.edmm.plugins.cfn;

import io.github.edmm.model.support.ModelEntity;

public interface TransformationRule<T extends ModelEntity> {

    boolean evaluate(T entity);

    void execute(T entity);
}
