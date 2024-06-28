package io.jenkins.plugins;

import static org.junit.Assert.assertFalse;
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
    public void should_support_configuration_as_code_with_highlighting_disabled() {
        Jenkins jenkins = jenkinsRule.jenkins;

        assertTrue(
                "Markdown markup formatter should be configured",
                jenkins.getMarkupFormatter() instanceof MarkdownFormatter);
        assertTrue(
                "Formatter should be configured with syntax highlighting disabled",
                ((MarkdownFormatter) jenkins.getMarkupFormatter()).isDisableSyntaxHighlighting());
    }

    @Test
    @ConfiguredWithCode("configuration-as-code-with-syntax-highlighting.yaml")
    public void should_support_configuration_as_code_with_highlighting_enabled() {
        Jenkins jenkins = jenkinsRule.jenkins;

        assertTrue(
                "Markdown markup formatter should be configured",
                jenkins.getMarkupFormatter() instanceof MarkdownFormatter);
        assertFalse(
                "Formatter should be configured with syntax highlighting enabled",
                ((MarkdownFormatter) jenkins.getMarkupFormatter()).isDisableSyntaxHighlighting());
    }

    @Test
    @ConfiguredWithCode("configuration-as-code-191.yaml") // Release 191.vf7955d4d081b_ 25 Jun 2024
    public void should_support_configuration_as_code_legacy() {
        Jenkins jenkins = jenkinsRule.jenkins;

        assertTrue(
                "Markdown markup formatter should be configured",
                jenkins.getMarkupFormatter() instanceof MarkdownFormatter);
        assertFalse(
                "Formatter should be configured with syntax highlighting enabled",
                ((MarkdownFormatter) jenkins.getMarkupFormatter()).isDisableSyntaxHighlighting());
    }
}
