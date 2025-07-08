package com.example.mittise.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.mittise.R
import com.example.mittise.ui.screens.*
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.IconButton
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Article
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Feedback
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Store
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Color
import com.example.mittise.util.LocaleHelper
import com.example.mittise.ui.screens.LanguageScreen
import com.example.mittise.ui.theme.*
import androidx.compose.foundation.layout.Row

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MittiSeNavigation(
    navController: NavHostController = rememberNavController()
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            EnhancedAppDrawer(
                currentDestination = currentDestination,
                onDestinationClicked = { route ->
                    scope.launch {
                        drawerState.close()
                    }
                    navController.navigate(route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                onCloseDrawer = {
                    scope.launch {
                        drawerState.close()
                    }
                }
            )
        }
    ) {
        Scaffold(
            topBar = {
                EnhancedTopAppBar(
                    onMenuClick = {
                        scope.launch {
                            drawerState.open()
                        }
                    },
                    onNotificationClick = { /* Notifications */ }
                )
            },
            bottomBar = {
                EnhancedBottomNavigation(
                    currentDestination = currentDestination,
                    onItemClick = { route ->
                        navController.navigate(route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = Screen.Dashboard.route,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                // Dashboard
                composable(Screen.Dashboard.route) {
                    DashboardScreen(
                        onNavigateToWeather = { navController.navigate(Screen.Weather.route) },
                        onNavigateToCropCalendar = { navController.navigate(Screen.CropCalendar.route) },
                        onNavigateToLanguage = { navController.navigate(Screen.Language.route) },
                        onNavigateToHelp = { navController.navigate(Screen.Help.route) },
                        onNavigateToFeedback = { navController.navigate(Screen.Feedback.route) },
                        onNavigateToAbout = { navController.navigate(Screen.About.route) },
                        onNavigateToArticles = { navController.navigate(Screen.Articles.route) }
                    )
                }

                // Marketplace
                composable(Screen.Marketplace.route) {
                    MarketplaceScreen(
                        onProductClick = { product ->
                            navController.navigate(Screen.ProductDetails.createRoute(product.id))
                        },
                        onRegisterProduct = {
                            navController.navigate(Screen.FarmerProductRegistration.route)
                        }
                    )
                }

                // APMC
                composable(Screen.Apmc.route) {
                    ApmcScreen()
                }

                // Chatbot (formerly Social)
                composable(Screen.Social.route) {
                    ChatbotScreen()
                }

                // Profile
                composable(Screen.Profile.route) {
                    ProfileScreen()
                }

                // Weather
                composable(Screen.Weather.route) {
                    WeatherScreen()
                }

                // Crop Calendar
                composable(Screen.CropCalendar.route) {
                    CropCalendarScreen()
                }

                // Soil Testing
                composable(Screen.SoilTesting.route) {
                    SoilTestingScreen()
                }

                // Articles
                composable(Screen.Articles.route) {
                    ArticlesScreen()
                }

                // Help
                composable(Screen.Help.route) {
                    HelpScreen()
                }

                // Feedback
                composable(Screen.Feedback.route) {
                    FeedbackScreen()
                }

                // About
                composable(Screen.About.route) {
                    AboutScreen()
                }

                // Language
                composable(Screen.Language.route) {
                    LanguageScreen()
                }

                // Advisor
                composable(Screen.Advisor.route) {
                    AdvisorScreen()
                }

                // Schemes
                composable(Screen.Schemes.route) {
                    SchemesScreen()
                }

                // Product Details
                composable(
                    route = Screen.ProductDetails.route,
                    arguments = Screen.ProductDetails.arguments
                ) { backStackEntry ->
                    val productId = backStackEntry.arguments?.getString("productId") ?: ""
                    PlaceholderScreens.ProductDetailsScreen(productId = productId)
                }

                // Post Details
                composable(
                    route = Screen.PostDetails.route,
                    arguments = Screen.PostDetails.arguments
                ) { backStackEntry ->
                    val postId = backStackEntry.arguments?.getString("postId") ?: ""
                    PlaceholderScreens.PostDetailsScreen(postId = postId)
                }

                // Edit Profile
                composable(Screen.EditProfile.route) {
                    PlaceholderScreens.EditProfileScreen()
                }

                // Farmer Product Registration
                composable(Screen.FarmerProductRegistration.route) {
                    PlaceholderScreens.FarmerProductRegistrationScreen()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EnhancedTopAppBar(
    onMenuClick: () -> Unit,
    onNotificationClick: () -> Unit
) {
    TopAppBar(
        title = {
            GradientText(
                text = stringResource(R.string.app_name),
                gradientColors = GradientColors.primaryGradient,
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold
                )
            )
        },
        navigationIcon = {
            GradientButton(
                onClick = onMenuClick,
                gradientColors = GradientColors.secondaryGradient,
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    Icons.Default.Menu,
                    contentDescription = "Menu",
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.size(20.dp)
                )
            }
        },
        actions = {
            GradientButton(
                onClick = onNotificationClick,
                gradientColors = GradientColors.tertiaryGradient,
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    Icons.Default.Notifications,
                    contentDescription = "Notifications",
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.size(20.dp)
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
            titleContentColor = MaterialTheme.colorScheme.onSurface
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EnhancedBottomNavigation(
    currentDestination: androidx.navigation.NavDestination?,
    onItemClick: (String) -> Unit
) {
    val navItems = listOf(
        NavigationItem(
            route = Screen.Dashboard.route,
            title = stringResource(R.string.dashboard),
            icon = Icons.Default.Home
        ),
        NavigationItem(
            route = Screen.Marketplace.route,
            title = stringResource(R.string.marketplace),
            icon = Icons.Default.ShoppingCart
        ),
        NavigationItem(
            route = Screen.Apmc.route,
            title = stringResource(R.string.apmc),
            icon = Icons.Default.Store
        ),
        NavigationItem(
            route = Screen.Social.route,
            title = "Chatbot",
            icon = Icons.Default.SmartToy
        ),
        NavigationItem(
            route = Screen.Profile.route,
            title = stringResource(R.string.profile),
            icon = Icons.Default.Person
        )
    )

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 8.dp
    ) {
        navItems.forEach { item ->
            val selected = currentDestination?.hierarchy?.any { it.route == item.route } == true
            NavigationBarItem(
                icon = {
                    Icon(
                        item.icon,
                        contentDescription = item.title,
                        tint = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                },
                label = {
                    Text(
                        text = item.title,
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontWeight = if (selected) FontWeight.Medium else FontWeight.Normal
                        )
                    )
                },
                selected = selected,
                onClick = { onItemClick(item.route) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    indicatorColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    }
}

@Composable
fun EnhancedAppDrawer(
    currentDestination: androidx.navigation.NavDestination?,
    onDestinationClicked: (String) -> Unit,
    onCloseDrawer: () -> Unit
) {
    ModalDrawerSheet(
        drawerContainerColor = MaterialTheme.colorScheme.surface,
        modifier = Modifier.width(300.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Header
            EnhancedDrawerHeader()
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Navigation Items
            val drawerItems = listOf(
                DrawerItem(
                    route = Screen.Dashboard.route,
                    title = stringResource(R.string.dashboard),
                    icon = Icons.Default.Home
                ),
                DrawerItem(
                    route = Screen.Weather.route,
                    title = stringResource(R.string.weather),
                    icon = Icons.Default.WbSunny
                ),
                DrawerItem(
                    route = Screen.CropCalendar.route,
                    title = stringResource(R.string.crop_calendar),
                    icon = Icons.Default.CalendarToday
                ),
                DrawerItem(
                    route = Screen.SoilTesting.route,
                    title = stringResource(R.string.soil_testing),
                    icon = Icons.Default.Science
                ),
                DrawerItem(
                    route = Screen.Articles.route,
                    title = stringResource(R.string.articles),
                    icon = Icons.Default.Article
                ),
                DrawerItem(
                    route = Screen.Help.route,
                    title = stringResource(R.string.help),
                    icon = Icons.Default.Help
                ),
                DrawerItem(
                    route = Screen.Feedback.route,
                    title = stringResource(R.string.feedback),
                    icon = Icons.Default.Feedback
                ),
                DrawerItem(
                    route = Screen.About.route,
                    title = stringResource(R.string.about),
                    icon = Icons.Default.Info
                ),
                DrawerItem(
                    route = Screen.Language.route,
                    title = stringResource(R.string.language),
                    icon = Icons.Default.Language
                ),
                DrawerItem(
                    route = Screen.Advisor.route,
                    title = stringResource(R.string.advisor),
                    icon = Icons.Default.AccountCircle
                ),
                DrawerItem(
                    route = Screen.Schemes.route,
                    title = stringResource(R.string.schemes),
                    icon = Icons.Default.Store
                )
            )
            
            drawerItems.forEach { item ->
                EnhancedDrawerItem(
                    item = item,
                    selected = currentDestination?.hierarchy?.any { it.route == item.route } == true,
                    onClick = { onDestinationClicked(item.route) }
                )
            }
            
            Spacer(modifier = Modifier.weight(1f))
            
            // Footer
            EnhancedDrawerFooter()
        }
    }
}

@Composable
fun EnhancedDrawerHeader() {
    EnhancedCard(
        gradientColors = GradientColors.primaryGradient,
        elevation = 12,
        cornerRadius = 20
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Animated App Icon
            FloatingAnimation {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = "MittiSe",
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.size(48.dp)
                )
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Text(
                text = stringResource(R.string.app_name),
                style = MaterialTheme.typography.headlineSmall.copy(
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold
                )
            )
            
            Text(
                text = "Your Farming Companion",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.9f)
                ),
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

@Composable
fun EnhancedDrawerItem(
    item: DrawerItem,
    selected: Boolean,
    onClick: () -> Unit
) {
    val gradientColors = if (selected) {
        GradientColors.primaryGradient
    } else {
        GradientColors.tertiaryGradient
    }
    
    EnhancedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        gradientColors = gradientColors,
        elevation = if (selected) 8 else 4,
        cornerRadius = 12
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Animated Icon
            if (selected) {
                Icon(
                    imageVector = item.icon,
                    contentDescription = item.title,
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.size(24.dp)
                )
            } else {
                Icon(
                    imageVector = item.icon,
                    contentDescription = item.title,
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.size(24.dp)
                )
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Text(
                text = item.title,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = if (selected) FontWeight.Medium else FontWeight.Normal
                )
            )
        }
    }
}

@Composable
fun EnhancedDrawerFooter() {
    EnhancedCard(
        gradientColors = GradientColors.secondaryGradient,
        elevation = 8,
        cornerRadius = 16
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Logout,
                contentDescription = "Logout",
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.size(24.dp)
            )
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Text(
                text = stringResource(R.string.logout),
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Medium
                )
            )
        }
    }
}

data class NavigationItem(
    val route: String,
    val title: String,
    val icon: ImageVector
)

data class DrawerItem(
    val route: String,
    val title: String,
    val icon: ImageVector
) 