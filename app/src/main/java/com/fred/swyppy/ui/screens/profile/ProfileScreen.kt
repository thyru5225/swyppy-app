package com.fred.swyppy.ui.screens.profile

import com.fred.swyppy.R

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.fred.swyppy.navigation.ROUT_HOME
import com.fred.swyppy.navigation.ROUT_START
import com.fred.swyppy.ui.theme.newpurple

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        //TopAppBar
        TopAppBar(
            title = { Text(text = "Profile") },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = newpurple,
                titleContentColor = Color.White,
                navigationIconContentColor = Color.White,
                actionIconContentColor = Color.White
            ),

            navigationIcon = {
                IconButton(onClick = {})
                {
                    Icon(imageVector = Icons.Default.Menu, contentDescription = "Menu")

                }
            },
            actions = {
                IconButton(onClick = {}) {
                    Icon(
                        imageVector = Icons.Default.ShoppingCart,
                        contentDescription = "Shopping Cart"
                    )
                }

                IconButton(onClick = { navController.navigate(ROUT_START) })
                {
                    Icon(imageVector = Icons.Default.Info, contentDescription = "Info")

                }
            }
        )


        //End of TopAppBar

        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Image(

                painter = painterResource(id = R.drawable.bnb),
                contentDescription = "bnb",
                modifier = Modifier
                    .size(300.dp)
                    .clip(shape = CircleShape),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(20.dp))


            Text(
                text = "Nayanna Realtors",
                fontSize = 40.sp,
                fontWeight = FontWeight.ExtraBold,
                color = newpurple,
                fontStyle = FontStyle.Italic,
                fontFamily = FontFamily.SansSerif

            )

            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "An e-commerce application is a digital system, either a mobile or web-based application, that manages the buying and selling of products or services over the internet. These applications allow businesses to create online storefronts (e.g., using platforms like Shopify or WooCommerce) and customers to browse, purchase, and manage their transactions online.",
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview(){
    ProfileScreen(rememberNavController())


}




