package franco.duda.lista.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import franco.duda.lista.R;

public class NewItemActivity extends AppCompatActivity {

    static int PHOTO_PICKER_REQUEST = 1;
    Uri photoSelected = null; //Uri é um endereço para um dado que não está localizado dentro do espaço reservado a app, mas sim no espaço de outras apps.
    // photoSelected guardará o endereço da foto selecionada pelo usuário, e não a foto em si.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_new_item);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.cl), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //onClick mostra oq vai acontecer quando clicarmos no botao
        ImageButton imgCI = findViewById(R.id.imbCI); //obtemos o botao

        imgCI.setOnClickListener(new View.OnClickListener() { //definimos ovidor de cliques
            @Override
            public void onClick(View v) { //executamos a abertura da galeria
                Intent photoPickerIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT); //criamos um Intent e abrimos o documento com ACTION_OPEN_DOCUMENT
                photoPickerIntent.setType("image/*"); // falamos pro Intent q queremos apenas documento do tipo mimetype “image/*
                startActivityForResult(photoPickerIntent,PHOTO_PICKER_REQUEST); //executamos o intent
            }
        });

        Button btnAddItem = findViewById(R.id.btnAddItem);

        btnAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (photoSelected == null) { //verificamos se os campos foram preenchidos pelo usuário
                    Toast.makeText(NewItemActivity.this,"É necessário selecionar uma imagem!", Toast.LENGTH_LONG).show(); // se tiver vazio exibimos uma mensagem de erro
                    return;
                }
                EditText etTitle = findViewById(R.id.etText);
                String title = etTitle.getText().toString();

                if (title.isEmpty()) {
                    Toast.makeText(NewItemActivity.this, "É necessário inserir um título", Toast.LENGTH_LONG).show(); // se tiver vazio exibimos uma mensagem de erro
                    return;
                }

                EditText etDesc = findViewById(R.id.etDesc);
                String description = etDesc.getText().toString();
                if (description.isEmpty()) {
                    Toast.makeText(NewItemActivity.this, "É necessário inserir uma descrição", Toast.LENGTH_LONG).show(); // se tiver vazio exibimos uma mensagem de erro
                    return;
                }
                //mostram como uma Activity pode retornar dados para a Activity que a chamou
                Intent i = new Intent();
                i.setData(photoSelected);
                i.putExtra("title", title);
                i.putExtra("description", description);
                setResult(Activity.RESULT_OK, i);
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,@Nullable Intent data) { //Toda vez que uma Activity retorna dados, o método onActivityResult é chamado dentro da Activity que iniciou a ação.
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PHOTO_PICKER_REQUEST) {
            if(resultCode == Activity.RESULT_OK) {
                photoSelected = data.getData();
                ImageView imvfotoPreview = findViewById(R.id.imvPhotoPreview);
                imvfotoPreview.setImageURI(photoSelected);
            }
        }
    }


}