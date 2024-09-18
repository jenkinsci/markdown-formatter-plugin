package io.jenkins.plugins;

import static org.junit.jupiter.api.Assertions.*;

import io.jenkins.plugins.casc.misc.ConfiguredWithCode;
import io.jenkins.plugins.casc.misc.JenkinsConfiguredWithCodeRule;
import io.jenkins.plugins.casc.misc.junit.jupiter.WithJenkinsConfiguredWithCode;
import org.junit.jupiter.api.Test;

@WithJenkinsConfiguredWithCode
class ConfigurationAsCodeTest {

    @Test
    @ConfiguredWithCode("configuration-as-code.yaml")
    void should_support_configuration_as_code_with_highlighting_disabled(JenkinsConfiguredWithCodeRule r) {
        assertInstanceOf(
                MarkdownFormatter.class,
                r.jenkins.getMarkupFormatter(),
                "Markdown markup formatter should be configured");
        assertTrue(
                ((MarkdownFormatter) r.jenkins.getMarkupFormatter()).isDisableSyntaxHighlighting(),
                "Formatter should be configured with syntax highlighting disabled");
    }

    @Test
    @ConfiguredWithCode("configuration-as-code-with-syntax-highlighting.yaml")
    void should_support_configuration_as_code_with_highlighting_enabled(JenkinsConfiguredWithCodeRule r) {
        assertInstanceOf(
                MarkdownFormatter.class,
                r.jenkins.getMarkupFormatter(),
                "Markdown markup formatter should be configured");
        assertFalse(
                ((MarkdownFormatter) r.jenkins.getMarkupFormatter()).isDisableSyntaxHighlighting(),
                "Formatter should be configured with syntax highlighting enabled");
    }

    @Test
    @ConfiguredWithCode("configuration-as-code-191.yaml") // Release 191.vf7955d4d081b_ 25 Jun 2024
    void should_support_configuration_as_code_legacy(JenkinsConfiguredWithCodeRule r) {
        assertInstanceOf(
                MarkdownFormatter.class,
                r.jenkins.getMarkupFormatter(),
                "Markdown markup formatter should be configured");
        assertFalse(
                ((MarkdownFormatter) r.jenkins.getMarkupFormatter()).isDisableSyntaxHighlighting(),
                "Formatter should be configured with syntax highlighting enabled");
    }
}
