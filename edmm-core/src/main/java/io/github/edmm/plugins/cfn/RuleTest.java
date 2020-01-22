package io.github.edmm.plugins.cfn;

import java.util.Optional;

import io.github.edmm.core.plugin.TopologyGraphHelper;
import io.github.edmm.core.transformation.TransformationContext;
import io.github.edmm.model.component.AwsBeanstalk;
import io.github.edmm.model.component.RootComponent;
import io.github.edmm.model.component.WebApplication;

public class RuleTest implements TransformationRule<WebApplication> {

    private final TransformationContext context;

    public RuleTest(TransformationContext context) {
        this.context = context;
    }

    @Override
    public boolean evaluate(WebApplication component) {
        Optional<RootComponent> hostingComponent
                = TopologyGraphHelper.resolveHostingComponent(context.getTopologyGraph(), component);
        return hostingComponent.isPresent() && hostingComponent.get() instanceof AwsBeanstalk;
    }

    @Override
    public void execute(WebApplication component) {

    }
}
