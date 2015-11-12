package com.example.guestbook;

import java.util.Stack;

/**
 * Created by Vladi on 05/11/15.
 */
public class XMLStringBuilder {

    private StringBuilder sb = new StringBuilder();
    private int currentIndent = 0;
    private Stack<String> openTags = new Stack<>();
    private Stack<Boolean> tagNewLines = new Stack<>();

    public XMLStringBuilder reset() {
        sb = new StringBuilder();
        currentIndent = 0;
        return this;
    }

    public XMLStringBuilder openTag(String tagName, boolean newLine) {
        indent();
        currentIndent++;
        sb.append("<").append(tagName).append(">");
        if (newLine) sb.append("\n");
        openTags.push(tagName);
        tagNewLines.push(newLine);
        return this;
    }

    public XMLStringBuilder closeTag() {
        currentIndent--;
        if (tagNewLines.pop()) indent();
        sb.append("</").append(openTags.pop()).append(">\n");
        return this;
    }

    private void indent() {
        for (int i = 0; i < currentIndent; i++) {
            sb.append("\t");
        }
    }

    public XMLStringBuilder append(String s) {
        sb.append(s);
        return this;
    }

    public XMLStringBuilder appendParagraph(String s) {
        indent();
        return append(s);
    }

    public String toString() {
        return sb.toString();
    }
}
