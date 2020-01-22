package io.github.edmm.plugins.cfn;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import io.github.edmm.core.plugin.AbstractLifecycle;
import io.github.edmm.core.plugin.support.CheckModelResult;
import io.github.edmm.core.transformation.TransformationContext;
import io.github.edmm.model.component.RootComponent;
import io.github.edmm.model.visitor.VisitorHelper;
import org.jeasy.rules.api.Rules;
import org.jeasy.rules.api.RulesEngine;
import org.jeasy.rules.core.DefaultRulesEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CloudFormationLifecycle extends AbstractLifecycle {

    private static final Logger logger = LoggerFactory.getLogger(CloudFormationLifecycle.class);

    private final CloudFormationModule module;
    private final RulesEngine engine = new DefaultRulesEngine();
    private final List<TransformationRule> rules;

    public CloudFormationLifecycle(TransformationContext context) {
        super(context);
        this.module = new CloudFormationModule("eu-west-1");
        this.module.setKeyPair(true);
        this.rules = new ArrayList<>();
        this.rules.add(new RuleTest(context));
    }

    @Override
    public CheckModelResult checkModel() {
        CloudFormationSupportVisitor visitor = new CloudFormationSupportVisitor(context);
        VisitorHelper.visit(context.getModel().getComponents(), visitor);
        return visitor.getResult();
    }

    @Override
    public void prepare() {
        super.prepare();
    }

    @Override
    public void transform() {
        logger.info("Begin transformation to AWS CloudFormation...");
//        Facts facts = new Facts();
//        facts.put("context", context);
//        facts.put("model", context.getModel());
//        facts.put("components", context.getModel().getComponents());
//        facts.put("relations", context.getModel().getRelations());
//        facts.put("topology", context.getModel().getTopology());
//        facts.put("reversedTopology", context.getModel().getReversedTopology());
//        facts.put("componentStacks", context.getModel().findComponentStacks());
//        engine.fire(rules, facts);

        Set<RootComponent> components = context.getModel().getComponents();

        for (TransformationRule rule : rules) {
            components.forEach(component -> {

                rule.evaluate(component);


                if (!component.isTransformed()) {

                }
            });
        }

//        CloudFormationVisitor visitor = new CloudFormationVisitor(context, module);
//        // Visit compute components first
//        VisitorHelper.visit(context.getModel().getComponents(), visitor, component -> component instanceof Compute);
//        // ... then all relations
//        VisitorHelper.visit(context.getModel().getRelations(), visitor);
//        // ... finally all other components
//        VisitorHelper.visit(context.getModel().getComponents(), visitor);
//        // Finalize transformation
//        visitor.complete();
//        try {
//            // Write template file
//            context.getFileAccess().append("template.yaml", module.toString());
//        } catch (IOException e) {
//            logger.error("Failed to write template file", e);
//            throw new TransformationException(e);
//        }
        logger.info("Transformation to AWS CloudFormation successful");
    }
}
