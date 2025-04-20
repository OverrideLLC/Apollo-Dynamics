package com.feature.desktop.home.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.commonmark.node.Code
import org.commonmark.node.Emphasis
import org.commonmark.node.HardLineBreak
import org.commonmark.node.Node
import org.commonmark.node.Paragraph
import org.commonmark.node.SoftLineBreak
import org.commonmark.node.StrongEmphasis
import org.commonmark.node.Text
import org.commonmark.parser.Parser

@Composable
fun MarkdownText(markdown: String, color: Color) {
    val parser = remember { Parser.builder().build() }
    val rootNode = remember(markdown) { parser.parse(markdown) }

    Column {
        var child = rootNode.firstChild
        while (child != null) {
            RenderNode(node = child, textColor = color)
            if (child.next != null) {
                Spacer(modifier = Modifier.height(8.dp))
            }
            child = child.next
        }
    }
}

@Composable
private fun RenderNode(node: Node, textColor: Color) {
    when (node) {
        is Paragraph -> {
            val annotatedString = buildAnnotatedString {
                var child = node.firstChild
                while (child != null) {
                    RenderInlineNode(node = child, builder = this, textColor = textColor)
                    child = child.next
                }
            }
            Text(text = annotatedString)
        }

        is Text -> {
        }
        // Puedes añadir más casos para otros tipos de nodos de bloque:
        // is Heading -> { /* Renderizar encabezado */ }
        // is BulletList -> { /* Renderizar lista con viñetas */ }
        // is OrderedList -> { /* Renderizar lista ordenada */ }
        // is BlockQuote -> { /* Renderizar cita */ }
        // is FencedCodeBlock -> { /* Renderizar bloque de código */ }
        // is ThematicBreak -> { /* Renderizar línea horizontal */ }
        else -> {
            // Para cualquier otro nodo de bloque no manejado explícitamente,
            // intentamos renderizar sus hijos si los tiene.
            var child = node.firstChild
            while (child != null) {
                RenderNode(node = child, textColor = textColor)
                child = child.next
            }
        }
    }
}

private fun RenderInlineNode(node: Node, builder: AnnotatedString.Builder, textColor: Color) {
    when (node) {
        is Text -> {
            builder.pushStyle(
                SpanStyle(
                    color = textColor,
                    fontWeight = FontWeight.Normal
                )
            )
            builder.append(node.literal)
            builder.pop()
        }

        is StrongEmphasis -> {
            builder.pushStyle(
                SpanStyle(
                    fontWeight = FontWeight.Bold,
                    color = Color(0xff0d9b03),
                    fontSize = 16.sp
                )
            )
            var child = node.firstChild
            while (child != null) {
                RenderInlineNode(node = child, builder = builder, textColor = Color(0xff0d9b03))
                child = child.next
            }
            builder.pop()
        }

        is Emphasis -> {
            builder.pushStyle(
                SpanStyle(
                    fontStyle = FontStyle.Italic,
                    color = textColor
                )
            )
            var child = node.firstChild
            while (child != null) {
                RenderInlineNode(node = child, builder = builder, textColor = textColor)
                child = child.next
            }
            builder.pop()
        }

        is SoftLineBreak -> {
            builder.append("\n")
        }

        is HardLineBreak -> {
            builder.append("\n") // Añade un salto de línea real
        }

        is Code -> {
            builder.pushStyle(
                TextStyle(fontFamily = FontFamily.Monospace).toSpanStyle()
            )
            builder.pop()
        }
        // is Link -> { /* Renderizar enlace */ }
        // is Image -> { /* Renderizar imagen */ }
        else -> {
            var child = node.firstChild
            while (child != null) {
                RenderInlineNode(node = child, builder = builder, textColor = textColor)
                child = child.next
            }
        }
    }
}