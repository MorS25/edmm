package io.github.edmm.plugins.cfn;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.github.edmm.core.plugin.AbstractLifecycle;
import io.github.edmm.core.plugin.support.CheckModelResult;
import io.github.edmm.core.transformation.TransformationContext;
import io.github.edmm.core.transformation.TransformationException;
import io.github.edmm.core.transformation.rules.Rule;
import io.github.edmm.core.transformation.rules.RulesEngine;
import io.github.edmm.model.component.Compute;
import io.github.edmm.model.visitor.VisitorHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CloudFormationLifecycle extends AbstractLifecycle {

    private static final Logger logger = LoggerFactory.getLogger(CloudFormationLifecycle.class);

    private final CloudFormationModule module;

    public CloudFormationLifecycle(TransformationContext context) {
        super(context);
        this.module = new CloudFormationModule("eu-west-1");
        this.module.setKeyPair(true);
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
        // ===================================================
        List<Rule> rules = new ArrayList<>();
        rules.add(new RuleTest(context));
        RulesEngine engine = new RulesEngine();
        // engine.check(context, rules);
        engine.fire(context, rules);
        // ===================================================
        CloudFormationVisitor visitor = new CloudFormationVisitor(context, module);
        // Visit compute components first
        VisitorHelper.visit(context.getModel().getComponents(), visitor, component -> component instanceof Compute);
        // ... then all relations
        VisitorHelper.visit(context.getModel().getRelations(), visitor);
        // ... finally all other components
        VisitorHelper.visit(context.getModel().getComponents(), visitor);
        // Finalize transformation
        visitor.complete();
        try {
            // Write template file
            context.getFileAccess().append("template.yaml", module.toString());
        } catch (IOException e) {
            logger.error("Failed to write template file", e);
            throw new TransformationException(e);
        }
        logger.info("Transformation to AWS CloudFormation successful");
    }
}
