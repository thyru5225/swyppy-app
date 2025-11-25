package com.fred.swyppy.ui.screens.home


import androidx.compose.foundation.Image
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.fred.swyppy.R
import com.fred.swyppy.ui.theme.newpurple

@Composable
fun HomeScreen(navController: NavController){
    //Main Layout
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center

    ) {


        //SearchBar
        var search by remember { mutableStateOf("") }
        OutlinedTextField(
            value = search,
            onValueChange = {search = it},
            modifier = Modifier.fillMaxWidth().padding(start = 20.dp, end = 20.dp),
            trailingIcon = {Icon(imageVector = Icons.Default.Search, contentDescription = "Search")},
            label = {Text(text ="Search a Space...")},
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.DarkGray,
                focusedTrailingIconColor = newpurple,
                focusedLabelColor = newpurple,


            )



        )
        //Row
        Row(
            modifier = Modifier
                .horizontalScroll(rememberScrollState())
                .padding(start=20.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.bnb),
                contentDescription = "bnb",
                modifier = Modifier.size(width=200.dp, height = 300.dp).clip(shape= RoundedCornerShape(10.dp)),
                contentScale = ContentScale.FillHeight
            )
            Spacer(modifier = Modifier.width(20.dp))



            Image(
                painter = painterResource(id = R.drawable.bnb),
                contentDescription = "bnb",
                modifier = Modifier.size(width=200.dp, height = 300.dp).clip(shape= RoundedCornerShape(10.dp)),
                contentScale = ContentScale.FillHeight
            )

            Spacer(modifier = Modifier.width(20.dp))


            Image(
                painter = painterResource(id = R.drawable.bnb),
                contentDescription = "bnb",
                modifier = Modifier.size(width=200.dp, height = 300.dp).clip(shape= RoundedCornerShape(10.dp)),
                contentScale = ContentScale.FillHeight
            )
            Spacer(modifier = Modifier.width(20.dp))

            Image(
                painter = painterResource(id = R.drawable.bnb),
                contentDescription = "bnb",
                modifier = Modifier.size(width=200.dp, height = 300.dp).clip(shape= RoundedCornerShape(10.dp)),
                contentScale = ContentScale.FillHeight
            )



        }

        //End of row

        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "BOOK A SPACE",
            fontSize = 30.sp,
            fontWeight = FontWeight.ExtraBold,
            color = newpurple,
            modifier = Modifier.align(alignment= Alignment.CenterHorizontally)

        )
        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "Got great space in Kilimani. Kshs 5,000 per day.",
            modifier = Modifier.align(alignment = Alignment.CenterHorizontally)

        )
        Spacer(modifier = Modifier.height(10.dp))

        Button(
            onClick = {},
            modifier = Modifier
                .height(50.dp)
                .fillMaxWidth()
                .padding(20.dp, 0.dp, 20.dp),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(newpurple)


        ) {
            Text(text = "Call/Text", fontSize = 20.sp, fontWeight = FontWeight.ExtraBold )
        }





    }
//End of main layout

}

@Preview(showBackground=true)
@Composable
fun HomeScreenPreview(){
    HomeScreen(rememberNavController())


}