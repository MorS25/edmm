package io.github.edmm.core.transformation.rules;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.github.edmm.core.transformation.TransformationContext;
import io.github.edmm.model.component.RootComponent;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class RulesEngine {

    @SuppressWarnings("unchecked")
    public void fire(TransformationContext context, List<Rule> rules) {
        if (rules.isEmpty()) {
            log.warn("No rules registered! Nothing to apply");
            return;
        }
        log.debug("Rules evaluation started");
        for (Rule rule : rules) {
            String name = rule.getName();
            for (RootComponent component : context.getModel().getComponents()) {
                if (rule.isApplicable(component)) {
                    if (rule.evaluate(component)) {
                        log.debug("Rule '{}' triggered", name);
                        try {
                            rule.execute(component);
                            log.debug("Rule '{}' performed successfully", name);
                        } catch (Exception exception) {
                            log.error("Rule '" + name + "' performed with error", exception);
                        }
                    } else {
                        log.debug("Rule '{}' has been evaluated to false, it has not been executed", name);
                    }
                }
            }
        }
    }

    public Map<Rule, Boolean> check(TransformationContext context, List<Rule> rules) {
        log.debug("Checking rules...");
        Map<Rule, Boolean> result = new HashMap<>();
        for (Rule rule : rules) {
            for (RootComponent component : context.getModel().getComponents()) {
                result.put(rule, rule.evaluate(component));
            }
        }
        return result;
    }
}
