package io.github.edmm.plugins.cfn;

import java.util.Optional;

import io.github.edmm.core.plugin.TopologyGraphHelper;
import io.github.edmm.core.transformation.TransformationContext;
import io.github.edmm.core.transformation.rules.Rule;
import io.github.edmm.model.component.Compute;
import io.github.edmm.model.component.RootComponent;
import io.github.edmm.model.component.WebApplication;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RuleTest extends Rule<WebApplication> {

    private final TransformationContext context;

    public RuleTest(TransformationContext context) {
        super(WebApplication.class);
        this.context = context;
    }

    @Override
    public boolean evaluate(WebApplication component) {
        log.info("Evaluate rule for component '{}'", component.getName());
        Optional<RootComponent> hostingComponent
                = TopologyGraphHelper.resolveHostingComponent(context.getTopologyGraph(), component);
        return hostingComponent.isPresent() && hostingComponent.get() instanceof Compute;
    }

    @Override
    public void execute(WebApplication component) {
        System.out.println(component);
        log.info("Execute rule for component '{}'", component.getName());
    }
}
