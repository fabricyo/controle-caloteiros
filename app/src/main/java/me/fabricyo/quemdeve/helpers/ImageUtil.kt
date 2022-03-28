package me.fabricyo.quemdeve.helpers

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import me.fabricyo.quemdeve.R
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

class ImageUtil {

    companion object {

        fun colors(): List<String> {
            return listOf("#3700B3", "#03DAC6", "#CF6679", "#3F729B", "#0D47A1")
        }

        private val APP_KEY = "me.fabricyo.quemdeve"
        private val PRINT_FILENAME_KEY = "print_filename"

        fun share(context: Context, debt: View) {
            val bitmap = getScreenShotFromView(debt)
            bitmap?.let {
                saveMediaToStorage(context, bitmap)
            }
        }

        private fun getScreenShotFromView(debt: View): Bitmap? {
            var screenshot: Bitmap? = null
            try {
                screenshot = Bitmap.createBitmap(
                    debt.measuredWidth,
                    debt.measuredHeight,
                    Bitmap.Config.ARGB_8888
                )
                val canvas = Canvas(screenshot)

                debt.draw(canvas)
            } catch (e: Exception) {
                Log.e("Error", "Falha ao capturar a imagem" + e.message)
            }

            return screenshot
        }

        private fun saveMediaToStorage(context: Context, bitmap: Bitmap) {
            val filename = "${System.currentTimeMillis()}.jpg"

            var fos: OutputStream? = null

            val sharedPref = context.getSharedPreferences(APP_KEY, Context.MODE_PRIVATE)


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                context.contentResolver?.also { resolver ->
                    val contentValues = ContentValues().apply {
                        put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
                        put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg")
                        put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
                    }
                    val imageUri: Uri? =
                        resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

                    with(sharedPref.edit()){
                        putString(PRINT_FILENAME_KEY, imageUri?.encodedPath)
                        apply()
                    }

                    fos = imageUri?.let {
                        shareIntent(context, imageUri)
                        resolver.openOutputStream(it)
                    }
                }
            } else {
                //Devices rodando < Q
                val imagesDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                val image = File(imagesDir, filename)
                with(sharedPref.edit()){
                    putString(PRINT_FILENAME_KEY, image.absolutePath)
                    apply()
                }
                shareIntent(context, Uri.fromFile(image))
                fos = FileOutputStream(image)
            }


            fos?.use {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
                Toast.makeText(context, "Imagem capturada com sucesso", Toast.LENGTH_SHORT).show()
            }
        }

        private fun shareIntent(context: Context, imageUri: Uri) {
            val shareIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_STREAM, imageUri)
                type = "imagem/jpeg"
            }

            context.startActivity(
                Intent.createChooser(
                    shareIntent,
                    context.resources.getText(R.string.label_share)
                )
            )
        }

        fun lookAndErasePrints(context: Context){
            //função para procurar prints antigos e apagar eles
            val sharedPref = context.getSharedPreferences(APP_KEY, Context.MODE_PRIVATE)
            val filename = sharedPref.getString(PRINT_FILENAME_KEY, null)
            if (filename != null) {
                val file = File(filename)
                if(file.delete(context)){
                    Toast.makeText(context, "Cache de imagens apagado com sucesso", Toast.LENGTH_SHORT).show()
                }
            }
        }

        fun File.delete(context: Context): Boolean {
            var selectionArgs = arrayOf(this.absolutePath)
            val contentResolver = context.getContentResolver()
            val where: String?
            val filesUri: Uri?
            if (Build.VERSION.SDK_INT >= 29) {
                filesUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                where = MediaStore.Images.Media._ID + "=?"
                selectionArgs = arrayOf(this.name)
            } else {
                where = MediaStore.MediaColumns.DATA + "=?"
                filesUri = MediaStore.Files.getContentUri("external")
            }

            val int = contentResolver.delete(filesUri!!, where, selectionArgs)
            return int > 0
        }
    }
}