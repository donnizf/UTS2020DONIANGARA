package ac.id.atmaluhur.mahasiswa.uts_amub_ti7jm_1711500070_doni_anggara;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.annotations.Nullable;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class RegisterTwoActivity extends AppCompatActivity {
    ImageView picture_photo_register_user,
            button_add_photoshot;
    ImageButton image_button_regis_dua;
    EditText edit_hobinya, edit_alamatnya;

    Uri photo_location;
    Integer photo_max = 1;

    DatabaseReference reference;
    StorageReference storage;

    String USERNAME_KEY = "usernamekey";
    String username_key ="";
    String username_key_new ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_two);

        getUsernameLocal();
        picture_photo_register_user = findViewById(R.id.picture_photo_register_user);
        button_add_photoshot = findViewById(R.id.image_btn_add_photo);
        image_button_regis_dua = findViewById(R.id.image_button_regis_dua);
        edit_alamatnya = findViewById(R.id.edit_alamatnya);
        edit_hobinya= findViewById(R.id.edit_hobinya);

        button_add_photoshot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findPhoto();
            }
        });

        image_button_regis_dua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reference = FirebaseDatabase.getInstance().getReference()
                        .child("Users").child(username_key_new);
                storage = FirebaseStorage.getInstance().getReference().child("Photousers")
                        .child(username_key_new);

                if (photo_location != null) {
                    StorageReference storageReference1 = storage.child(System.currentTimeMillis() + "." +
                            getFileExtension(photo_location));
                    storageReference1.putFile(photo_location)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {
                                    String uri_photo = taskSnapshot.getMetadata().getReference().getDownloadUrl().toString();
                                    reference.getRef().child("url_photo_profile").setValue(uri_photo);
                                    reference.getRef().child("hobi").setValue(edit_hobinya.getText().toString());
                                    reference.getRef().child("alamat").setValue(edit_alamatnya.getText().toString());
                                }
                            }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            Intent gotosuccess = new Intent(RegisterTwoActivity.this, MainActivity.class);
                            startActivity(gotosuccess);
                        }
                    });
                }
            }
        });
    }
    String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    public void findPhoto() {
        Intent pic = new Intent();
        pic.setType("image/*");
        pic.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(pic, photo_max);
    }

    private void getUsernameLocal() {
        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
        username_key_new = sharedPreferences.getString(username_key, "");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode,data);
        if (requestCode == photo_max && resultCode == RESULT_OK && data.getData() != null) {
            photo_location = data.getData();

            Picasso.with(this).load(photo_location).centerCrop().fit().into(picture_photo_register_user);
        }

    }
}