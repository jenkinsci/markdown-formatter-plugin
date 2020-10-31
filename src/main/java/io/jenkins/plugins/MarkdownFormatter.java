package io.jenkins.plugins;

import hudson.Extension;
import hudson.markup.MarkupFormatter;
import hudson.markup.MarkupFormatterDescriptor;
import org.jetbrains.annotations.NotNull;
import org.kohsuke.stapler.DataBoundConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;

import com.vladsch.flexmark.ext.emoji.EmojiExtension;
import com.vladsch.flexmark.ext.emoji.EmojiShortcutType;
import com.vladsch.flexmark.ext.gfm.issues.GfmIssuesExtension;
import com.vladsch.flexmark.ext.gfm.strikethrough.StrikethroughExtension;
import com.vladsch.flexmark.ext.gfm.tasklist.TaskListExtension;
import com.vladsch.flexmark.ext.gfm.users.GfmUsersExtension;
import com.vladsch.flexmark.ext.tables.TablesExtension;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.data.MutableDataSet;

public class MarkdownFormatter extends MarkupFormatter {
    private transient HtmlRenderer htmlRenderer = null;
    private transient Parser markdownParser = null;
    private Logger logger = LoggerFactory.getLogger(MarkdownFormatter.class);

    @DataBoundConstructor
    public MarkdownFormatter() {
    }

    @NotNull
    private MutableDataSet getOptions() {
        MutableDataSet options = new MutableDataSet();

        options.set(Parser.EXTENSIONS, Arrays.asList(
                TablesExtension.create(),
                StrikethroughExtension.create(),
                GfmUsersExtension.create(),
                TaskListExtension.create(),
                GfmIssuesExtension.create(),
                EmojiExtension.create()
        ));
        options.set(EmojiExtension.USE_SHORTCUT_TYPE, EmojiShortcutType.GITHUB);
        return options;
    }

    @Override
    public void translate(String markup, Writer output) throws IOException {
        if (this.htmlRenderer == null) {
            MutableDataSet options = getOptions();
            this.htmlRenderer = HtmlRenderer.builder(options).escapeHtml(true).build();
        }
        if (this.markdownParser == null) {
            MutableDataSet options = getOptions();
            this.markdownParser = Parser.builder(options).build();
        }
        output.write(htmlRenderer.render(markdownParser.parse(markup)));
    }

    @Extension
    public static class DescriptorImpl extends MarkupFormatterDescriptor {
        @Override
        public String getDisplayName() {
            return "Markdown Formatter";
        }
    }
}
