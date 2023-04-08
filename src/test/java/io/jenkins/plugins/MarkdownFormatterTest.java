package io.jenkins.plugins;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.jvnet.hudson.test.JenkinsRule;

public class MarkdownFormatterTest {
    @Rule
    public JenkinsRule j = new JenkinsRule();

    @Before
    public void setup() {
        j.jenkins.setMarkupFormatter(new MarkdownFormatter());
    }

    @Test
    public void handlesMarkdown() throws Exception {
        // basic markdown
        assertEquals(
                "<p><em>bold</em></p>",
                j.jenkins.getMarkupFormatter().translate("*bold*").trim());
    }

    @Test
    public void handlesEmoji() throws Exception {
        // basic markdown
        assertEquals(
                "<p>\uD83D\uDE8C</p>",
                j.jenkins.getMarkupFormatter().translate(":bus:").trim());
    }

    @Test
    public void defaultEscaped() throws Exception {
        // basic markdown
        assertEquals(
                "<p><em>bold</em></p>",
                j.jenkins.getMarkupFormatter().translate("*bold*").trim());
        // html gets escaped
        assertEquals(
                "<p>&lt;ul&gt;&lt;li&gt;list&lt;/li&gt;&lt;/ul&gt;</p>",
                j.jenkins
                        .getMarkupFormatter()
                        .translate("<ul><li>list</li></ul>")
                        .trim());
        // basic link
        assertEquals(
                "<p><a rel=\"nofollow\" href=\"http://www.youtube.com/watch?v=YOUTUBE_VIDEO_ID_HERE\"><img src=\"http://img.youtube.com/vi/YOUTUBE_VIDEO_ID_HERE/0.jpg\" alt=\"IMAGE ALT TEXT HERE\" /></a></p>",
                j.jenkins
                        .getMarkupFormatter()
                        .translate(
                                "[![IMAGE ALT TEXT HERE](http://img.youtube.com/vi/YOUTUBE_VIDEO_ID_HERE/0.jpg)](http://www.youtube.com/watch?v=YOUTUBE_VIDEO_ID_HERE)")
                        .trim());
        // basic xss gets escaped
        assertEquals(
                "<p><a rel=\"nofollow\" href=\"\">some text</a></p>",
                j.jenkins
                        .getMarkupFormatter()
                        .translate("[some text](javascript:alert('xss'))")
                        .trim());
        // mixed xss gets escaped
        assertEquals(
                "<p>hello &lt;a name=&quot;n&quot;</p>\n" + "<blockquote>\n"
                        + "<p>href=&quot;javascript:alert('xss')&quot;&gt;<em>you</em>&lt;/a&gt;</p>\n"
                        + "</blockquote>",
                j.jenkins
                        .getMarkupFormatter()
                        .translate("hello <a name=\"n\"\n" + "> href=\"javascript:alert('xss')\">*you*</a>")
                        .trim());
        // more complex XSS
        assertEquals(
                "<p>Payload: <a rel=\"nofollow\" href=\"\">click me</a></p>",
                j.jenkins
                        .getMarkupFormatter()
                        .translate("Payload: [click me](javascript&#X3a;alert`XSS`)")
                        .trim());
    }

    @Test
    public void handlesNull() throws Exception {
        assertEquals("", j.jenkins.getMarkupFormatter().translate(null).trim());
    }
}
