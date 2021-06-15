package com.stsf.globalbackend.services

import org.intellij.markdown.flavours.commonmark.CommonMarkFlavourDescriptor
import org.intellij.markdown.html.HtmlGenerator
import org.intellij.markdown.parser.MarkdownParser
import org.springframework.stereotype.Service

@Service
class MarkdownService {
    fun markdownToHTML(s: String): String {
        val flavor = CommonMarkFlavourDescriptor()
        val parsedTree = MarkdownParser(flavor).buildMarkdownTreeFromString(s)
        return HtmlGenerator(s, parsedTree, flavor).generateHtml()
    }
}