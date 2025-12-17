package com.fred.swyppy.ui.screens.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.fred.swyppy.navigation.ROUTE_ADD_PROPERTY
import com.fred.swyppy.ui.theme.newpurple

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(navController: NavController) {

    val cardItems = listOf(
        DashboardCard("User Profiles", Icons.Default.Person) {
            //navController.navigate("profile")
        },
        DashboardCard("Products", Icons.Default.ShoppingCart) {
            // navController.navigate(ROUT_ADD_PROPERTY)
        },
        DashboardCard("Orders", Icons.Default.List) {
            // navController.navigate("orders")
        },
        DashboardCard("Payments", Icons.Default.Info) {
            // navController.navigate("payments")
        },
        DashboardCard("Messages", Icons.Default.Email) {
            // navController.navigate("messages")
        },
        DashboardCard("Settings", Icons.Default.Settings) {
            // navController.navigate("settings")
        }
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF4F4F8))
    ) {

        // Modern Gradient Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .background(
                    Brush.linearGradient(
                        colors = listOf(
                            newpurple,
                            Color(0xFF673AB7)
                        )
                    )
                ),
            contentAlignment = Alignment.CenterStart
        ) {
            Column(modifier = Modifier.padding(start = 20.dp)) {
                Text(
                    text = "Welcome Admin",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "Manage your Swyppy business",
                    color = Color.White.copy(0.9f),
                    fontSize = 14.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Dashboard Grid
        Text(
            text = "Dashboard",
            modifier = Modifier.padding(start = 20.dp, bottom = 10.dp),
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        )

        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            cardItems.chunked(2).forEach { rowItems ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    rowItems.forEach { item ->
                        AdminFeatureCard(
                            item = item,
                            modifier = Modifier.weight(1f)
                        )
                    }
                    // Add spacer for odd number of items
                    if (rowItems.size == 1) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

data class DashboardCard(
    val title: String,
    val icon: ImageVector,
    val onClick: () -> Unit
)

@Composable
fun AdminFeatureCard(
    item: DashboardCard,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .height(140.dp)
            .shadow(4.dp, shape = RoundedCornerShape(20.dp))
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White)
            .clickable { item.onClick() }
            .padding(16.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start,
            modifier = Modifier.fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .size(45.dp)
                    .clip(CircleShape)
                    .background(newpurple.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = item.icon,
                    contentDescription = item.title,
                    tint = newpurple,
                    modifier = Modifier.size(26.dp)
                )
            }

            Spacer(modifier = Modifier.height(15.dp))

            Text(
                text = item.title,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DashboardScreenPreview() {
    DashboardScreen(rememberNavController())
}