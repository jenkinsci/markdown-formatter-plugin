package io.jenkins.plugins;

import com.vdurmont.emoji.EmojiParser;
import hudson.Extension;
import hudson.markup.MarkupFormatter;
import hudson.markup.MarkupFormatterDescriptor;
import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;
import java.util.List;
import org.commonmark.ext.autolink.AutolinkExtension;
import org.commonmark.ext.gfm.strikethrough.StrikethroughExtension;
import org.commonmark.ext.gfm.tables.TablesExtension;
import org.commonmark.ext.ins.InsExtension;
import org.commonmark.node.*;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.kohsuke.stapler.DataBoundConstructor;

public class MarkdownFormatter extends MarkupFormatter {
    private static HtmlRenderer htmlRenderer = null;
    private static Parser markdownParser = null;
    // private static MutableDataSet options = new MutableDataSet();

    private final boolean disableSyntaxHighlighting;

    /** @deprecated */
    @Deprecated
    public MarkdownFormatter() {
        this(true);
    }

    @DataBoundConstructor
    public MarkdownFormatter(boolean disableSyntaxHighlighting) {
        this.disableSyntaxHighlighting = disableSyntaxHighlighting;
    }

    static {
        List<org.commonmark.Extension> extensions = Arrays.asList(
                TablesExtension.create(),
                AutolinkExtension.create(),
                StrikethroughExtension.create(),
                InsExtension.create());
        htmlRenderer = HtmlRenderer.builder()
                .extensions(extensions)
                .escapeHtml(true)
                .sanitizeUrls(true)
                .build();
        markdownParser = Parser.builder().extensions(extensions).build();
    }

    public boolean isDisableSyntaxHighlighting() {
        return disableSyntaxHighlighting;
    }

    @Override
    public void translate(String markup, Writer output) throws IOException {
        if (markup != null) {
            Node document = markdownParser.parse(EmojiParser.parseToUnicode(markup));
            output.write(htmlRenderer.render(document));
        } else {
            output.write("");
        }
    }

    public String getCodeMirrorMode() {
        return disableSyntaxHighlighting ? null : "markdown";
    }

    @Extension
    public static class DescriptorImpl extends MarkupFormatterDescriptor {
        @Override
        public String getDisplayName() {
            return "Markdown Formatter";
        }
    }
}
