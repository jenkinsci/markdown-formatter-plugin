package io.jenkins.plugins;

import com.vdurmont.emoji.EmojiParser;
import edu.umd.cs.findbugs.annotations.NonNull;
import hudson.Extension;
import hudson.markup.MarkupFormatter;
import hudson.markup.MarkupFormatterDescriptor;
import java.io.IOException;
import java.io.Writer;
import java.util.List;
import org.commonmark.ext.autolink.AutolinkExtension;
import org.commonmark.ext.gfm.strikethrough.StrikethroughExtension;
import org.commonmark.ext.gfm.tables.TablesExtension;
import org.commonmark.ext.ins.InsExtension;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.kohsuke.stapler.DataBoundConstructor;

public class MarkdownFormatter extends MarkupFormatter {

    private static final List<org.commonmark.Extension> EXTENSIONS = List.of(
            TablesExtension.create(),
            AutolinkExtension.create(),
            StrikethroughExtension.create(),
            InsExtension.create());

    private static final HtmlRenderer HTML_RENDERER = HtmlRenderer.builder()
            .extensions(EXTENSIONS)
            .escapeHtml(true)
            .sanitizeUrls(true)
            .build();

    private static final Parser MARKDOWN_PARSER =
            Parser.builder().extensions(EXTENSIONS).build();

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

    public boolean isDisableSyntaxHighlighting() {
        return disableSyntaxHighlighting;
    }

    @Override
    public void translate(String markup, @NonNull Writer output) throws IOException {
        if (markup != null) {
            Node document = MARKDOWN_PARSER.parse(EmojiParser.parseToUnicode(markup));
            output.write(HTML_RENDERER.render(document));
        } else {
            output.write("");
        }
    }

    public String getCodeMirrorMode() {
        return disableSyntaxHighlighting ? null : "markdown";
    }

    @Extension
    public static class DescriptorImpl extends MarkupFormatterDescriptor {

        @NonNull
        @Override
        public String getDisplayName() {
            return "Markdown Formatter";
        }
    }
}
