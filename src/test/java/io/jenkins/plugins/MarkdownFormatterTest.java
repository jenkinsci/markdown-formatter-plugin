package io.jenkins.plugins;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.jvnet.hudson.test.JenkinsRule;
import org.jvnet.hudson.test.junit.jupiter.WithJenkins;

@WithJenkins
class MarkdownFormatterTest {

    private JenkinsRule r;

    @BeforeEach
    void setup(JenkinsRule r) {
        this.r = r;
        this.r.jenkins.setMarkupFormatter(new MarkdownFormatter(false));
    }

    @Test
    void handlesSyntaxHighlightingEnabled() {
        MarkdownFormatter formatter = new MarkdownFormatter(false);

        assertFalse(formatter.isDisableSyntaxHighlighting(), "Syntax highlighting should be enabled");
        assertEquals("markdown", formatter.getCodeMirrorMode(), "Code mirror code should be set");
    }

    @Test
    void handlesSyntaxHighlightingDisabled() {
        MarkdownFormatter formatter = new MarkdownFormatter(true);

        assertTrue(formatter.isDisableSyntaxHighlighting(), "Syntax highlighting should be disabled");
        assertNull(formatter.getCodeMirrorMode(), "Code mirror code should not be set");
    }

    @Test
    @Deprecated
    void handlesSyntaxHighlightingDisabledDeprecatedConstructor() {
        MarkdownFormatter formatter = new MarkdownFormatter();

        assertTrue(formatter.isDisableSyntaxHighlighting(), "Syntax highlighting should be disabled");
        assertNull(formatter.getCodeMirrorMode(), "Code mirror code should not be set");
    }

    @Test
    void handlesMarkdown() throws Exception {
        // basic markdown
        assertEquals(
                "<p><em>bold</em></p>",
                r.jenkins.getMarkupFormatter().translate("*bold*").trim());
    }

    @Test
    void handlesEmoji() throws Exception {
        // basic markdown
        assertEquals(
                "<p>\uD83D\uDE8C</p>",
                r.jenkins.getMarkupFormatter().translate(":bus:").trim());
    }

    @Test
    void defaultEscaped() throws Exception {
        // basic markdown
        assertEquals(
                "<p><em>bold</em></p>",
                r.jenkins.getMarkupFormatter().translate("*bold*").trim());
        // html gets escaped
        assertEquals(
                "<p>&lt;ul&gt;&lt;li&gt;list&lt;/li&gt;&lt;/ul&gt;</p>",
                r.jenkins
                        .getMarkupFormatter()
                        .translate("<ul><li>list</li></ul>")
                        .trim());
        // basic link
        assertEquals(
                "<p><a rel=\"nofollow\" href=\"http://www.youtube.com/watch?v=YOUTUBE_VIDEO_ID_HERE\"><img src=\"http://img.youtube.com/vi/YOUTUBE_VIDEO_ID_HERE/0.jpg\" alt=\"IMAGE ALT TEXT HERE\" /></a></p>",
                r.jenkins
                        .getMarkupFormatter()
                        .translate(
                                "[![IMAGE ALT TEXT HERE](http://img.youtube.com/vi/YOUTUBE_VIDEO_ID_HERE/0.jpg)](http://www.youtube.com/watch?v=YOUTUBE_VIDEO_ID_HERE)")
                        .trim());
        // basic xss gets escaped
        assertEquals(
                "<p><a rel=\"nofollow\" href=\"\">some text</a></p>",
                r.jenkins
                        .getMarkupFormatter()
                        .translate("[some text](javascript:alert('xss'))")
                        .trim());
        // mixed xss gets escaped
        assertEquals(
                "<p>hello &lt;a name=&quot;n&quot;</p>\n" + "<blockquote>\n"
                        + "<p>href=&quot;javascript:alert('xss')&quot;&gt;<em>you</em>&lt;/a&gt;</p>\n"
                        + "</blockquote>",
                r.jenkins
                        .getMarkupFormatter()
                        .translate("hello <a name=\"n\"\n" + "> href=\"javascript:alert('xss')\">*you*</a>")
                        .trim());
        // more complex XSS
        assertEquals(
                "<p>Payload: <a rel=\"nofollow\" href=\"\">click me</a></p>",
                r.jenkins
                        .getMarkupFormatter()
                        .translate("Payload: [click me](javascript&#X3a;alert`XSS`)")
                        .trim());
    }

    @Test
    void handlesNull() throws Exception {
        assertEquals("", r.jenkins.getMarkupFormatter().translate(null).trim());
    }
}
