package com.mercadona.mercastock.presentation.features.help

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mercadona.mercastock.dia4.R
import com.mercadona.mercastock.presentation.ui.model.HelpItem

@Composable
fun HelpScreen(
    onNavigateToFaq: () -> Unit,
    onNavigateToContact: () -> Unit,
    onNavigateToGuides: () -> Unit,
    onNavigateToTerms: () -> Unit,
    onNavigateToPrivacy: () -> Unit,
    onNavigateToAbout: () -> Unit
) {
    val context = LocalContext.current
    
    val quickHelpItems = listOf(
        HelpItem(
            title = context.getString(R.string.help_faq_title),
            description = context.getString(R.string.help_faq_description),
            icon = Icons.Default.Warning,
            onClick = onNavigateToFaq
        ),
        HelpItem(
            title = context.getString(R.string.help_contact_title),
            description = context.getString(R.string.help_contact_description),
            icon = Icons.Default.Phone,
            onClick = onNavigateToContact
        ),
        HelpItem(
            title = context.getString(R.string.help_guides_title),
            description = context.getString(R.string.help_guides_description),
            icon = Icons.Default.ThumbUp,
            onClick = onNavigateToGuides
        )
    )
    
    val legalHelpItems = listOf(
        HelpItem(
            title = context.getString(R.string.help_terms_title),
            description = context.getString(R.string.help_terms_description),
            icon = Icons.Default.Home,
            onClick = onNavigateToTerms
        ),
        HelpItem(
            title = context.getString(R.string.help_privacy_title),
            description = context.getString(R.string.help_privacy_description),
            icon = Icons.Default.Check,
            onClick = onNavigateToPrivacy
        ),
        HelpItem(
            title = context.getString(R.string.help_about_title),
            description = context.getString(R.string.help_about_description),
            icon = Icons.Default.Info,
            onClick = onNavigateToAbout
        )
    )
    
    HelpScreenContent(
        quickHelpItems = quickHelpItems,
        legalHelpItems = legalHelpItems
    )
}

@Composable
private fun HelpScreenContent(
    quickHelpItems: List<HelpItem>,
    legalHelpItems: List<HelpItem>
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Sección de ayuda rápida
        item {
            HelpSectionTitle(stringResource(R.string.help_quick_section))
        }

        items(quickHelpItems) { item ->
            HelpOptionCard (
                title = item.title,
                description = item.description,
                icon = item.icon,
                onClick = item.onClick
            )
        }

        // Sección legal e información
        item {
            Spacer(modifier = Modifier.height(8.dp))
            HelpSectionTitle(stringResource(R.string.help_legal_section))
        }

        items(legalHelpItems) { item ->
            HelpOptionCard (
                title = item.title,
                description = item.description,
                icon = item.icon,
                onClick = item.onClick
            )
        }
    }
}

@Composable
private fun HelpSectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onSurface,
        modifier = Modifier.padding(vertical = 8.dp)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HelpOptionCard(
    title: String,
    description: String,
    icon: ImageVector,
    onClick: () -> Unit,
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(32.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun HelpScreenPreview() {
    MaterialTheme {
        HelpScreenContent(
            quickHelpItems = listOf(
                HelpItem(
                    title = "Preguntas Frecuentes",
                    description = "Respuestas a las preguntas más comunes sobre el uso de MercaStock",
                    icon = Icons.Default.ThumbUp,
                    onClick = {}
                ),
                HelpItem(
                    title = "Contacto y Soporte",
                    description = "Información de contacto y canales de soporte disponibles",
                    icon = Icons.Default.Phone,
                    onClick = {}
                ),
                HelpItem(
                    title = "Guías de Usuario",
                    description = "Tutoriales paso a paso para aprovechar al máximo la aplicación",
                    icon = Icons.Default.Home,
                    onClick = {}
                )
            ),
            legalHelpItems = listOf(
                HelpItem(
                    title = "Términos y Condiciones",
                    description = "Información legal y términos de uso de la aplicación",
                    icon = Icons.Default.Warning,
                    onClick = {}
                ),
                HelpItem(
                    title = "Política de Privacidad",
                    description = "Cómo recopilamos, utilizamos y protegemos tus datos personales",
                    icon = Icons.Default.Check,
                    onClick = {}
                ),
                HelpItem(
                    title = "Acerca de MercaStock",
                    description = "Información sobre la versión, tecnologías y equipo de desarrollo",
                    icon = Icons.Default.Info,
                    onClick = {}
                )
            )
        )
    }
}