package io.github.edmm.plugins;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Objects;

import io.github.edmm.core.plugin.PluginFileAccess;
import io.github.edmm.core.transformation.Transformation;
import io.github.edmm.core.transformation.TransformationContext;
import io.github.edmm.model.DeploymentModel;
import io.github.edmm.plugins.terraform.TerraformLifecycle;
import io.github.edmm.plugins.terraform.TerraformPlugin;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TerraformTests {

    private static final Logger logger = LoggerFactory.getLogger(TerraformTests.class);

    private File rootDirectory;
    private TransformationContext context;
    private PluginFileAccess fileAccess;

    @Before
    public void init() throws Exception {
        rootDirectory = Files.createTempDirectory("terraform-").toFile();
        ClassPathResource resource = new ClassPathResource("templates/scenario_iaas.yml");
        DeploymentModel model = DeploymentModel.of(resource.getFile());
        Transformation transformation = mock(Transformation.class);
        when(transformation.getModel()).thenReturn(model);
        logger.info("Root directory is '{}'", rootDirectory);
        context = new TransformationContext(transformation, rootDirectory);
        fileAccess = context.getFileAccess();
    }

    @Test
    public void test() throws Exception {
        // Setup plugin
        TerraformPlugin plugin = new TerraformPlugin();
        TerraformLifecycle lifecycle = plugin.getLifecycle(context);
        // Execute lifecycle phases
        lifecycle.checkEnvironment();
        lifecycle.checkModel();
        lifecycle.prepare();
        lifecycle.transform();
        lifecycle.cleanup();
        // Assert ...
        Assert.assertEquals(1, Objects.requireNonNull(fileAccess.getTargetDirectory().listFiles()).length);
        ClassPathResource expectedResource = new ClassPathResource("terraform/deploy.tf");
        String expected = FileUtils.readFileToString(expectedResource.getFile(), StandardCharsets.UTF_8);
        String test = FileUtils.readFileToString(new File(fileAccess.getAbsolutePath("deploy.tf")), StandardCharsets.UTF_8);
        Assert.assertEquals(expected.trim(), test.trim());
    }

    @After
    public void destroy() throws Exception {
        FileUtils.deleteDirectory(rootDirectory);
    }
}