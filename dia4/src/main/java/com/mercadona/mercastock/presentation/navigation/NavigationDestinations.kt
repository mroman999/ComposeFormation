package com.mercadona.mercastock.presentation.navigation

import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.toRoute
import com.mercadona.mercastock.dia4.R
import kotlinx.serialization.Serializable

@Serializable
sealed class NavigationDestination(
    val title: Int,
    val showBackButton: Boolean = false,
    val showBottomBar: Boolean = true,
) {

    @Serializable
    data object Dashboard : NavigationDestination(title = R.string.dashboard_title)

    @Serializable
    data class ProductForm(
        val productId: String? = null,
    ) : NavigationDestination(
        title = if (productId != null) R.string.ui_edit_product else R.string.nav_add_product,
        showBackButton = true
    ) {
        companion object {
            val PRODUCT_ID = "productId"
        }
    }

    @Serializable
    data object Help : NavigationDestination(
        title = R.string.help_title
    )

    @Serializable
    data object HelpFaq : NavigationDestination(
        title = R.string.help_faq_title,
        showBackButton = true,
    )

    @Serializable
    data object HelpContact : NavigationDestination(
        title = R.string.help_contact_title,
        showBackButton = true,
    )

    @Serializable
    data object HelpGuides : NavigationDestination(
        title = R.string.help_guides_title,
        showBackButton = true,
    )

    @Serializable
    data object HelpTerms : NavigationDestination(
        title = R.string.help_terms_title,
        showBackButton = true,
        showBottomBar = false
    )

    @Serializable
    data object HelpPrivacy : NavigationDestination(
        title = R.string.help_privacy_title,
        showBackButton = true,
        showBottomBar = false
    )

    @Serializable
    data object HelpAbout : NavigationDestination(
        title = R.string.help_about_title,
        showBackButton = true,
        showBottomBar = false
    )
}


fun getCurrentNavigationDestination(navBackStackEntry: NavBackStackEntry?): NavigationDestination {
    val destination = navBackStackEntry?.destination
    return when {
        destination == null -> NavigationDestination.Dashboard
        destination.hasRoute(NavigationDestination.Dashboard::class) -> NavigationDestination.Dashboard
        destination.hasRoute(NavigationDestination.ProductForm::class) -> {
            try {
                navBackStackEntry.toRoute<NavigationDestination.ProductForm>()
            } catch (e: Exception) {
                e.printStackTrace()
                NavigationDestination.ProductForm()
            }
        }
        destination.hasRoute(NavigationDestination.HelpFaq::class) -> NavigationDestination.HelpFaq
        destination.hasRoute(NavigationDestination.HelpContact::class) -> NavigationDestination.HelpContact
        destination.hasRoute(NavigationDestination.HelpGuides::class) -> NavigationDestination.HelpGuides
        destination.hasRoute(NavigationDestination.HelpTerms::class) -> NavigationDestination.HelpTerms
        destination.hasRoute(NavigationDestination.HelpPrivacy::class)-> NavigationDestination.HelpPrivacy
        destination.hasRoute(NavigationDestination.HelpAbout::class) -> NavigationDestination.HelpAbout
        destination.hasRoute(NavigationDestination.Help::class) -> NavigationDestination.Help
        else -> NavigationDestination.Dashboard
    }
}