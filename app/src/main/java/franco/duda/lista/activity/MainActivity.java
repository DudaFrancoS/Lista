package franco.duda.lista.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import franco.duda.lista.R;
import franco.duda.lista.adapter.MyAdapter;
import franco.duda.lista.model.MyItem;
import franco.duda.lista.util.Util;

public class MainActivity extends AppCompatActivity {

    static int NEW_ITEM_REQUEST = 1;
    List<MyItem> itens = new ArrayList<>(); //atributo

    MyAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.cl), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //onClick mostra oq vai acontecer quando clicarmos no botao
        FloatingActionButton fabAddItem = findViewById(R.id.floatingActionButton); //obtemos um botao

        fabAddItem.setOnClickListener(new View.OnClickListener() { //registramos ouvidor de cliques

            @Override
            public void onClick(View v) { //realizamos a navegação em si através do uso de um Intent explícito para navegar para NewItemAcitvity
                Intent i = new Intent(MainActivity.this,NewItemActivity.class);
                startActivityForResult(i, NEW_ITEM_REQUEST); //executamos o Intent usando um método especial
            }
        });

        RecyclerView rvItens = findViewById(R.id.rvItens);

        myAdapter = new MyAdapter(this,itens);
        rvItens.setAdapter(myAdapter);

        rvItens.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        rvItens.setLayoutManager(layoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rvItens.getContext(),DividerItemDecoration.VERTICAL);
        rvItens.addItemDecoration(dividerItemDecoration);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode,Intent data) { //Ao retornar de NewItemActivity, o método onActivityResult é chamado em MainActivity
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == NEW_ITEM_REQUEST) { //verificamos se as condições de retorno foram cumpridas
            if (resultCode == Activity.RESULT_OK) {
                MyItem myItem = new MyItem(); //Em caso afirmativo, criamos uma instância de MyItem para guardar os dados do item
                myItem.title = data.getStringExtra("title"); //obtemos os dados retornados por NewItemActivity e os guardamos dentro de myItem.
                myItem.description = data.getStringExtra("description");
                Uri selectedPhotoURI = data.getData();

                try {
                    Bitmap photo = Util.getBitmap( MainActivity.this, selectedPhotoURI, 100, 100 ); //Essa função carrega a imagem e a guarda dentro de um Bitmap
                    myItem.photo = photo; //guardamos o Bitmap da imagem dentro de um objeto do tipo MyItem
                }
                catch (FileNotFoundException e) {
                    e.printStackTrace();
                }


                itens.add(myItem); //adicionamos o item a uma lista de itens
                myAdapter.notifyItemInserted(itens.size()-1);
            }
        }
    }
}