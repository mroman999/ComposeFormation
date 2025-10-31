package com.mercadona.mercastock.presentation.features.about

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mercadona.mercastock.dia3.R

@Composable
fun HelpAboutScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = stringResource(R.string.about_app_info_title),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(stringResource(R.string.about_version))
        Spacer(modifier = Modifier.height(8.dp))
        Text(stringResource(R.string.about_build))
        Spacer(modifier = Modifier.height(8.dp))
        Text(stringResource(R.string.about_last_update))
        Spacer(modifier = Modifier.height(8.dp))
        Text(stringResource(R.string.about_tech))
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Text(
            text = stringResource(R.string.about_development_title),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(stringResource(R.string.about_development))
        Spacer(modifier = Modifier.height(8.dp))
        Text(stringResource(R.string.about_company))
        Spacer(modifier = Modifier.height(8.dp))
        Text(stringResource(R.string.about_day))
        Spacer(modifier = Modifier.height(8.dp))
        Text(stringResource(R.string.about_technologies))
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Text(
            text = stringResource(R.string.about_features_title),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(stringResource(R.string.about_feature_navigation))
        Spacer(modifier = Modifier.height(8.dp))
        Text(stringResource(R.string.about_feature_architecture))
        Spacer(modifier = Modifier.height(8.dp))
        Text(stringResource(R.string.about_feature_design))
        Spacer(modifier = Modifier.height(8.dp))
        Text(stringResource(R.string.about_feature_di))
        Spacer(modifier = Modifier.height(8.dp))
        Text(stringResource(R.string.about_feature_ui))
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Text(
            text = stringResource(R.string.about_thanks_title),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(stringResource(R.string.about_thanks_text))
    }
}

@Composable
@Preview
private fun HelpAboutScreenPreview() {
    MaterialTheme {
        HelpAboutScreen()
    }
}
