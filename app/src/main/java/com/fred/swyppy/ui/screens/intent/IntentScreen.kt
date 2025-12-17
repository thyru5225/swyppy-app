package com.fred.swyppy.ui.screens.intent

import android.content.Intent
import android.provider.MediaStore
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
//import com.fred.swyppy.navigation.ROUT_HOME
import com.fred.swyppy.ui.theme.newpurple

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IntentScreen(navController: NavController){
    val mContext = LocalContext.current

//Scaffold

    var selectedIndex by remember { mutableStateOf(0) }

    Scaffold(

        //TopBar
        topBar = {
            TopAppBar(
                title = { Text("IntentScreen") },
                navigationIcon = {
                    IconButton(onClick = { /* Handle back/nav */ }) {
                        Icon(Icons.Default.Menu, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = newpurple,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White,
                    actionIconContentColor = Color.White
                ),
                actions = {
                    IconButton(onClick = {}) { Icon(imageVector = Icons.Default.Share, contentDescription ="") }
                    IconButton(onClick = {}) { Icon(imageVector = Icons.Default.Settings, contentDescription ="") }
                }

            )
        },



        //BottomBar
        bottomBar = {
            NavigationBar(
                containerColor = newpurple
            ) {
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Home,
                        contentDescription = "Home",
                        tint = newpurple
                    )
                    },
                    label = { Text("Login", color = Color.White) },
                    selected = selectedIndex == 0,
                    onClick = {
                        selectedIndex = 0 }
                )
            }
        },


        //FloatingActionButton
        floatingActionButton = {
            FloatingActionButton(
                onClick = { /* Add action */ },
                containerColor = newpurple
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add", tint = Color.White)
            }
        },



        //Contents

        content = { paddingValues ->
            Column(horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
                // .background(newpurple)
            ) {
                //Main Contents of the page
                Spacer(modifier = Modifier.height(20.dp))
                Text(text = "Common Android Intents",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold

                )
                Spacer(modifier = Modifier.height(20.dp))

                //STK MPESA
                Button(onClick = {

                    val simToolKitLaunchIntent =
                        mContext.packageManager.getLaunchIntentForPackage("com.android.stk")
                    simToolKitLaunchIntent?.let { mContext.startActivity(it) }

                },
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(newpurple)
                )
                {Text(text = "STK")}

                Spacer(modifier = Modifier.height(20.dp))

                //Call
                Button(onClick = {
                    val callIntent= Intent(Intent.ACTION_DIAL)
                    callIntent.data="tel:0720245837".toUri()
                    mContext.startActivity(callIntent)
                },
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(newpurple)
                )
                {Text(text = "Call")}
                Spacer(modifier = Modifier.height(20.dp))

                //Message Us
                Button(onClick = {
                    val smsIntent= Intent(Intent.ACTION_SENDTO)
                    smsIntent.data="smsto:0720245837".toUri()
                    smsIntent.putExtra("sms_body","Hey, what is the price?")
                    mContext.startActivity(smsIntent)

                },
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(newpurple)
                )
                {Text(text = "Message Us")}

                Spacer(modifier = Modifier.height(20.dp))
                //Email Us
                Button(onClick = {
                    val shareIntent = Intent(Intent.ACTION_SEND)
                    shareIntent.type = "text/plain"
                    shareIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf("akinyiglory2@gmail.com"))
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "subject")
                    shareIntent.putExtra(Intent.EXTRA_TEXT, "Hello, this is the email body")
                    mContext.startActivity(shareIntent)
                },
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(newpurple)
                )
                {Text(text = "Email Us")}
                Spacer(modifier = Modifier.height(20.dp))

                //Share
                Button(onClick = {
                    val shareIntent=Intent(Intent.ACTION_SEND)
                    shareIntent.type="text/plain"
                    shareIntent.putExtra(Intent.EXTRA_TEXT, "https://github.com/gloryakinyi")
                    mContext.startActivity(Intent.createChooser(shareIntent, "Share"))
                },
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(newpurple)
                )
                {Text(text = "Share")}

                Spacer(modifier = Modifier.height(20.dp))
                //Camera
                Button(onClick = {
                    val cameraIntent=Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    if (cameraIntent.resolveActivity(mContext.packageManager)!=null){
                        mContext.startActivity(cameraIntent)
                    }else{
                        println("Camera app is not available")
                    }





                },
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(newpurple)
                )
                {Text(text = "Camera")}

















            }
        }
    )

    //End of scaffold



}

@Preview(showBackground = true)
@Composable
fun IntentScreenPreview(){
    IntentScreen(rememberNavController())


}