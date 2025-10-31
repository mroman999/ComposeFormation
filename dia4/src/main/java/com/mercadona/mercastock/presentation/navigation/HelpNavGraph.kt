package com.mercadona.mercastock.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.mercadona.mercastock.presentation.features.about.HelpAboutScreen
import com.mercadona.mercastock.presentation.features.contact.HelpContactScreen
import com.mercadona.mercastock.presentation.features.faq.HelpFaqScreen
import com.mercadona.mercastock.presentation.features.guides.HelpGuidesScreen
import com.mercadona.mercastock.presentation.features.help.HelpScreen
import com.mercadona.mercastock.presentation.features.privacy.HelpPrivacyScreen
import com.mercadona.mercastock.presentation.features.terms.HelpTermsScreen

fun NavGraphBuilder.addHelpNavGraph(navController: NavHostController) {
    navigation<NavGraphRoutes.HelpNavGraphRoute>(
        startDestination = NavGraphRoutes.HelpNavGraphRoute.root
    ) {
        composable<NavigationDestination.Help> {
            HelpScreen(
                onNavigateToFaq = {
                    navController.navigate(NavigationDestination.HelpFaq)
                },
                onNavigateToContact = {
                    navController.navigate(NavigationDestination.HelpContact)
                },
                onNavigateToGuides = {
                    navController.navigate(NavigationDestination.HelpGuides)
                },
                onNavigateToTerms = {
                    navController.navigate(NavigationDestination.HelpTerms)
                },
                onNavigateToPrivacy = {
                    navController.navigate(NavigationDestination.HelpPrivacy)
                },
                onNavigateToAbout = {
                    navController.navigate(NavigationDestination.HelpAbout)
                }
            )
        }

        composable<NavigationDestination.HelpFaq> {
            HelpFaqScreen()
        }

        composable<NavigationDestination.HelpContact> {
            HelpContactScreen()
        }

        composable<NavigationDestination.HelpGuides> {
            HelpGuidesScreen()
        }

        composable<NavigationDestination.HelpTerms> {
            HelpTermsScreen()
        }

        composable<NavigationDestination.HelpPrivacy> {
            HelpPrivacyScreen()
        }

        composable<NavigationDestination.HelpAbout> {
            HelpAboutScreen()
        }
    }
}