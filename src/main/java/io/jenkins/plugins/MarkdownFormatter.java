package io.jenkins.plugins;

import com.vladsch.flexmark.ext.emoji.EmojiImageType;
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
    private static HtmlRenderer htmlRenderer = null;
    private static Parser markdownParser = null;
    private static MutableDataSet options = new MutableDataSet();
    private static Logger logger = LoggerFactory.getLogger(MarkdownFormatter.class);

    @DataBoundConstructor
    public MarkdownFormatter() {
    }

    static {
        options.set(Parser.EXTENSIONS, Arrays.asList(
                TablesExtension.create(),
                StrikethroughExtension.create(),
                TaskListExtension.create(),
//                GfmUsersExtension.create(),
//                GfmIssuesExtension.create(),
                EmojiExtension.create()
        ));
        options.set(EmojiExtension.USE_IMAGE_TYPE, EmojiImageType.UNICODE_FALLBACK_TO_IMAGE);
        options.set(EmojiExtension.USE_SHORTCUT_TYPE, EmojiShortcutType.GITHUB);
        htmlRenderer = HtmlRenderer.builder(options).escapeHtml(true).build();
        markdownParser = Parser.builder(options).build();
    }

    @Override
    public void translate(String markup, Writer output) throws IOException {
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
