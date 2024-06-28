package io.jenkins.plugins;

import static org.junit.Assert.assertTrue;

import io.jenkins.plugins.casc.misc.ConfiguredWithCode;
import io.jenkins.plugins.casc.misc.JenkinsConfiguredWithCodeRule;
import jenkins.model.Jenkins;
import org.junit.Rule;
import org.junit.Test;

public class ConfigurationAsCodeTest {

    @Rule
    public JenkinsConfiguredWithCodeRule jenkinsRule = new JenkinsConfiguredWithCodeRule();

    @Test
    @ConfiguredWithCode("configuration-as-code.yaml")
    public void should_support_configuration_as_code() {
        Jenkins jenkins = jenkinsRule.jenkins;

        assertTrue(
                "Markdown markup formatter should be configured",
                jenkins.getMarkupFormatter() instanceof MarkdownFormatter);
        assertTrue(
                "Formatter should be configured with syntax highlighting disabled",
                ((MarkdownFormatter) jenkins.getMarkupFormatter()).isDisableSyntaxHighlighting());
    }
}
