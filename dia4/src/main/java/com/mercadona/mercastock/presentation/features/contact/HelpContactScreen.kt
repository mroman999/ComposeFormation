package com.mercadona.mercastock.presentation.features.contact

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mercadona.mercastock.dia4.R

@Composable
fun HelpContactScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = stringResource(R.string.contact_info_title),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(stringResource(R.string.contact_email))
        Spacer(modifier = Modifier.height(8.dp))
        Text(stringResource(R.string.contact_phone))
        Spacer(modifier = Modifier.height(8.dp))
        Text(stringResource(R.string.contact_hours))
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Text(
            text = stringResource(R.string.contact_support_channels_title),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(stringResource(R.string.contact_live_chat))
        Spacer(modifier = Modifier.height(8.dp))
        Text(stringResource(R.string.contact_tickets))
        Spacer(modifier = Modifier.height(8.dp))
        Text(stringResource(R.string.contact_knowledge_base))
    }
}

@Composable
@Preview
fun HelpContactScreenPreview() {
    MaterialTheme {
        HelpContactScreen()
    }
}
