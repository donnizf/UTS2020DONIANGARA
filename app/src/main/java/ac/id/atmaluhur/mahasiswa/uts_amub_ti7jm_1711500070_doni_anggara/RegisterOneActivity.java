package ac.id.atmaluhur.mahasiswa.uts_amub_ti7jm_1711500070_doni_anggara;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RegisterOneActivity extends AppCompatActivity {
    ImageButton img_btn_lanjutmas;
    EditText edit_nama_satu,
            edit_pass_satu,
            edit_alamatemail;

    DatabaseReference reference;
    String USERNAME_KEY = " usernamekey";
    String username_key = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_one);
        edit_nama_satu = findViewById(R.id.edit_nama_satu);
        edit_pass_satu = findViewById(R.id.edit_password_satu);
        edit_alamatemail = findViewById(R.id.edit_email);

        img_btn_lanjutmas = findViewById(R.id.btn_lanjut);

        img_btn_lanjutmas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(username_key, edit_nama_satu.getText().toString());
                editor.apply();

                reference = FirebaseDatabase.getInstance().getReference().child("Users").child(edit_nama_satu.getText().toString());
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        dataSnapshot.getRef().child("username").setValue(edit_nama_satu.getText().toString());
                        dataSnapshot.getRef().child("password").setValue(edit_pass_satu.getText().toString());
                        dataSnapshot.getRef().child("email").setValue(edit_alamatemail.getText().toString());
                        dataSnapshot.getRef().child("user_balance").setValue(100000);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                //berpindah ke activity lain activity registertwoactivity
                Intent gotonextregister = new Intent(RegisterOneActivity.this, RegisterTwoActivity.class);
                startActivity(gotonextregister);
            }
        });
    }
}