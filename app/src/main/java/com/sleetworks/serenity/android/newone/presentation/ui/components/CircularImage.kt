import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.compose.rememberAsyncImagePainter

import java.io.File

@Composable
fun CircularImage(userName: String?, imageFile: File?) {
    Box(
        modifier = Modifier
            .size(72.dp)  // Adjust size
            .clip(CircleShape)  // Circular shape
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(70.dp)  // Adjust size
                .clip(CircleShape)  // Circular shape
                .background(Color.Gray)  // Background color when no image is available
        ) {
            if (imageFile?.exists() == true) {
                // If the image file exists, load the image using Coil
                AsyncImage(
                    model = imageFile,
                    contentDescription = "User Avatar",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop // Ensures the image fits the circle
                )
            } else {
                val firstLetter = userName?.firstOrNull()?.uppercase() ?: "?"
                Text(
                    text = firstLetter,
                    modifier = Modifier
                        .fillMaxSize()
                        .wrapContentSize(Alignment.Center),
                    style = TextStyle(
                        fontSize = 36.sp, // Adjust font size
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewCircularImageWithFallback() {
    // Example usage with no image file
    CircularImage("John Doe", imageFile = null)

    // Example usage with a sample image file (you can replace with an actual image path)
    // CircularImageWithFallback("Jane Smith", imageFile = File("path_to_image"))
}
