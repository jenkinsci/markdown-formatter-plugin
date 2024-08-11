# Markdown formatter

Markdown Formatter allows plain text to be converted into HTML in Jenkins.
The formatter uses [CommonMark](https://commonmark.org/).

## Getting started

In Manage Jenkins -> Security -> Markup Formatter select "Markdown Formatter" from the drop down list.

Markdown syntax highlighting is enabled by default while editing the markdown text in the Jenkins pages.
A configuration checkbox is available in the "Security" page to disable syntax highlighting while editing markdown text in Jenkins.

## Configuration as code

The [configuration as code plugin](https://plugins.jenkins.io/configuration-as-code/) can define the markup formatter and its configuration.

```yaml
jenkins:
  markupFormatter:
    markdownFormatter:
      disableSyntaxHighlighting: false
```

## Contributing

Refer to our [contribution guidelines](https://github.com/jenkinsci/.github/blob/master/CONTRIBUTING.md)

## LICENSE

Licensed under MIT, see [LICENSE](LICENSE.md)
