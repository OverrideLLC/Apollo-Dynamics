package com.feature.desktop.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.feature.desktop.home.utils.copyToClipboard
import com.shared.resources.Res
import com.shared.resources.content_copy_24dp_E8EAED_FILL0_wght400_GRAD0_opsz24
import com.shared.resources.terminal_24dp_E8EAED_FILL0_wght400_GRAD0_opsz24
import org.commonmark.node.BulletList
import org.commonmark.node.Code
import org.commonmark.node.Emphasis
import org.commonmark.node.FencedCodeBlock
import org.commonmark.node.HardLineBreak
import org.commonmark.node.ListItem
import org.commonmark.node.Node
import org.commonmark.node.OrderedList
import org.commonmark.node.Paragraph
import org.commonmark.node.SoftLineBreak
import org.commonmark.node.StrongEmphasis
import org.commonmark.node.Text
import org.commonmark.parser.Parser
import org.jetbrains.compose.resources.painterResource

@Composable
fun MarkdownText(
    markdown: String,
    color: Color,
    onRunCode: (String) -> Unit
) {
    val parser = remember { Parser.builder().build() }
    // Parsear el markdown sólo cuando el string cambie
    val rootNode = remember(markdown) { parser.parse(markdown) }

    // Columna principal para todos los bloques de markdown
    Column {
        var child = rootNode.firstChild
        while (child != null) {
            // Renderizar cada nodo de bloque principal
            RenderNode(
                node = child,
                textColor = color,
                onRunCode = { code -> onRunCode(code) }
            )
            // Añadir espacio entre bloques principales si no son el último
            if (child.next != null) {
                Spacer(modifier = Modifier.height(8.dp)) // Espacio entre párrafos, listas, etc.
            }
            child = child.next
        }
    }
}

@Composable
private fun RenderNode(
    node: Node,
    textColor: Color,
    indentLevel: Int = 0,
    onRunCode: (String) -> Unit = {}
) { // Añadir indentLevel para anidación
    // Calcular padding inicial basado en el nivel de anidación
    val startPadding = (16 * indentLevel).dp

    when (node) {
        // Párrafo: Renderiza su contenido inline
        is Paragraph -> {
            val annotatedString = buildAnnotatedString {
                var child = node.firstChild
                while (child != null) {
                    RenderInlineNode(node = child, builder = this, textColor = textColor)
                    child = child.next
                }
            }
            // Aplicar padding inicial al Text del párrafo
            Text(text = annotatedString, modifier = Modifier.padding(start = startPadding))
        }

        // Lista con viñetas
        is BulletList -> {
            // Columna para los elementos de la lista
            Column(modifier = Modifier.padding(start = startPadding)) { // Aplicar padding general a la lista
                var listItem = node.firstChild
                while (listItem != null) {
                    if (listItem is ListItem) { // Asegurarse de que es un ListItem
                        RenderListItem(
                            itemNode = listItem,
                            marker = "•",
                            textColor = textColor,
                            indentLevel = indentLevel + 1
                        )
                    }
                    // Espacio vertical entre elementos de la lista si hay siguiente
                    if (listItem.next != null) {
                        Spacer(modifier = Modifier.height(4.dp))
                    }
                    listItem = listItem.next
                }
            }
        }

        // Lista ordenada
        is OrderedList -> {
            // Columna para los elementos de la lista
            Column(modifier = Modifier.padding(start = startPadding)) { // Aplicar padding general a la lista
                var itemNumber = 1 // Contador para los números
                var listItem = node.firstChild
                while (listItem != null) {
                    if (listItem is ListItem) { // Asegurarse de que es un ListItem
                        RenderListItem(
                            itemNode = listItem,
                            marker = "$itemNumber.",
                            textColor = textColor,
                            indentLevel = indentLevel + 1
                        )
                        itemNumber++ // Incrementar número para el siguiente item
                    }
                    // Espacio vertical entre elementos de la lista si hay siguiente
                    if (listItem.next != null) {
                        Spacer(modifier = Modifier.height(4.dp))
                    }
                    listItem = listItem.next
                }
            }
        }

        // Ignorar nodos de texto directamente bajo el root (deberían estar en párrafos)
        is Text -> {
            // No hacer nada, el texto se maneja dentro de Paragraph/RenderInlineNode
        }

        // Puedes añadir más casos para otros tipos de nodos de bloque:
        // is Heading -> { /* Renderizar encabezado */ }
        // is BlockQuote -> { /* Renderizar cita */ }
        is FencedCodeBlock -> {
            // Renderizar bloque de código cercado
            Column(
                modifier = Modifier
                    .padding(start = startPadding)
                    .background(
                        color = Color.LightGray.copy(alpha = 0.3f),
                        shape = MaterialTheme.shapes.small
                    )
            ) {
                // Título opcional basado en la información del bloque de código
                if (node.info.isNotEmpty()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                color = Color.Gray.copy(alpha = 0.3f),
                                shape = MaterialTheme.shapes.small
                            ),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = node.info,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Black,
                            modifier = Modifier.padding(bottom = 4.dp, start = 8.dp)
                        )
                        Row(
                            horizontalArrangement = Arrangement.End,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconButton(
                                onClick = {
                                    copyToClipboard(
                                        text = node.literal.trimIndent()
                                    )
                                },
                                modifier = Modifier.size(24.dp),
                                content = {
                                    Icon(
                                        painter = painterResource(Res.drawable.content_copy_24dp_E8EAED_FILL0_wght400_GRAD0_opsz24),
                                        contentDescription = "Copiar código",
                                        tint = Color.Black
                                    )
                                }
                            )
                            if (node.info == "python") {
                                IconButton(
                                    onClick = {
                                        onRunCode(node.literal.trimIndent())
                                    },
                                    modifier = Modifier.size(24.dp),
                                    content = {
                                        Icon(
                                            painter = painterResource(Res.drawable.terminal_24dp_E8EAED_FILL0_wght400_GRAD0_opsz24),
                                            contentDescription = "Run Code",
                                            tint = Color.Black
                                        )
                                    }
                                )
                            }
                        }
                    }
                }
                // Renderizar el contenido del bloque de código como texto monoespaciado
                Text(
                    text = node.literal,
                    fontFamily = FontFamily.Monospace,
                    style = MaterialTheme.typography.bodyMedium,
                    color = textColor,
                    modifier = Modifier.padding(4.dp)
                )
            }
        }
        // is ThematicBreak -> { /* Renderizar línea horizontal */ }

        // Fallback: Renderizar hijos si el nodo actual no se maneja explícitamente
        else -> {
            var child = node.firstChild
            while (child != null) {
                // Pasar el nivel de indentación actual a los hijos
                RenderNode(node = child, textColor = textColor, indentLevel = indentLevel)
                child = child.next
            }
        }
    }
}

// Nuevo Composable para renderizar un elemento de lista (ListItem)
@Composable
private fun RenderListItem(itemNode: ListItem, marker: String, textColor: Color, indentLevel: Int) {
    Row { // Usar Row para alinear marcador y contenido
        // Marcador (viñeta o número)
        Text(
            text = marker,
            color = textColor,
            fontWeight = FontWeight.Normal,
            fontFamily = MaterialTheme.typography.displayMedium.fontFamily,
            modifier = Modifier.width(20.dp) // Ancho fijo para el marcador
        )
        // Columna para el contenido del elemento de lista
        // Un ListItem puede contener varios párrafos u otros bloques
        Column {
            var contentChild = itemNode.firstChild
            while (contentChild != null) {
                // Renderizar cada nodo hijo del ListItem (usualmente Párrafos)
                // Pasar el indentLevel para manejar correctamente el padding en RenderNode
                RenderNode(
                    node = contentChild,
                    textColor = textColor,
                    indentLevel = 0
                ) // Reset indentLevel para el contenido del item
                contentChild = contentChild.next
            }
        }
    }
}


// Función para nodos inline (sin cambios respecto a la versión anterior)
private fun RenderInlineNode(node: Node, builder: AnnotatedString.Builder, textColor: Color) {
    when (node) {
        is org.commonmark.node.Text -> { // Asegurarse que es el Text de commonmark
            builder.pushStyle(
                SpanStyle(
                    color = textColor,
                    fontWeight = FontWeight.Normal // Asegurar peso normal para texto base
                )
            )
            builder.append(node.literal)
            builder.pop()
        }

        is StrongEmphasis -> {
            builder.pushStyle(
                SpanStyle(
                    fontWeight = FontWeight.Bold, // Aplicar negrita
                    color = Color(0xff0d9b03) // Mantener el color base o especificar otro
                )
            )
            var child = node.firstChild
            while (child != null) {
                // Pasar el color actual al renderizar hijos de StrongEmphasis
                RenderInlineNode(node = child, builder = builder, textColor = Color(0xff0d9b03))
                child = child.next
            }
            builder.pop()
        }

        is Emphasis -> {
            builder.pushStyle(
                SpanStyle(
                    fontStyle = FontStyle.Italic, // Aplicar cursiva
                    color = textColor // Mantener el color base
                )
            )
            var child = node.firstChild
            while (child != null) {
                // Pasar el color actual al renderizar hijos de Emphasis
                RenderInlineNode(node = child, builder = builder, textColor = textColor)
                child = child.next
            }
            builder.pop()
        }

        is SoftLineBreak -> {
            // Comportamiento estándar de Markdown: tratar como espacio
            builder.append(" ")
            // O si prefieres un salto de línea real: builder.append("\n")
        }

        is HardLineBreak -> {
            builder.append("\n") // Añade un salto de línea real
        }

        // Código inline (ej: `código`)
        is Code -> {
            print(
                "Code: ${node.literal}"
            )
            builder.pushStyle(
                SpanStyle(
                    fontFamily = FontFamily.Monospace, // Fuente monoespaciada
                    color = textColor, // Usar color base o uno específico para código
                    background = Color.LightGray.copy(alpha = 0.3f) // Fondo sutil para código inline
                )
            )
            builder.append(node.literal) // Añadir el contenido del nodo Code
            builder.pop()
        }

        // is Link -> { /* Renderizar enlace */ }
        // is Image -> { /* Renderizar imagen */ }
        else -> {
            // Para cualquier otro nodo inline no manejado, renderiza sus hijos.
            var child = node.firstChild
            while (child != null) {
                RenderInlineNode(node = child, builder = builder, textColor = textColor)
                child = child.next
            }
        }
    }
}
