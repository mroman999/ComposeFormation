package com.mercadona.mercastock.presentation.features.guides

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
fun HelpGuidesScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = stringResource(R.string.guides_available_title),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(stringResource(R.string.guides_getting_started))
        Spacer(modifier = Modifier.height(8.dp))
        Text(stringResource(R.string.guides_getting_started_desc))
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(stringResource(R.string.guides_advanced))
        Spacer(modifier = Modifier.height(8.dp))
        Text(stringResource(R.string.guides_advanced_desc))
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(stringResource(R.string.guides_reports))
        Spacer(modifier = Modifier.height(8.dp))
        Text(stringResource(R.string.guides_reports_desc))
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(stringResource(R.string.guides_alerts_title))
        Spacer(modifier = Modifier.height(8.dp))
        Text(stringResource(R.string.guides_alerts_desc))
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(stringResource(R.string.guides_best_practices_title))
        Spacer(modifier = Modifier.height(8.dp))
        Text(stringResource(R.string.guides_best_practices_desc))
    }
}

@Composable
@Preview
fun HelpGuidesScreenPreview() {
    MaterialTheme {
        HelpGuidesScreen()
    }
}

