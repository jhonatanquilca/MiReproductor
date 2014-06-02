package com.reproductor.main;

import java.util.ArrayList;

import com.example.mireproductor.R;
import com.reproductor.Listar.AndroidFileIO;
import com.reproductor.Listar.Listar;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class Reproductor extends Activity implements OnClickListener,
		OnCompletionListener, OnItemClickListener, OnSeekBarChangeListener {
	View vistaView;
	ListView listaMusicasListView;
	SeekBar progresoMusicaSeekBar;

	hiloMusica hm = null;
	HiloPantalla hp=null;
	TextView mireproductorTextView, nombreMusica, numMusicasTextView;
	Button reproducirButtom, anteriorButtom, siguienteirButtom, detenerButtom,
			listarButton;

	MediaPlayer mediaPlayer = null;

	
	Uri[] MusicasArray = new Uri[] {};

	Listar listar = new Listar();
	int contadorRoductor = 0;

	AndroidFileIO palyList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reproductor);

		vistaView = this.findViewById(R.id.VistaView);
		listaMusicasListView = (ListView) this.findViewById(R.id.listaMusicas);

		reproducirButtom = (Button) this.findViewById(R.id.PlayPauseButton);
		siguienteirButtom = (Button) this.findViewById(R.id.SiguienteButton);
		anteriorButtom = (Button) this.findViewById(R.id.AnteriorButton);
		detenerButtom = (Button) this.findViewById(R.id.DetenerButton);
		listarButton = (Button) this.findViewById(R.id.listar);

		progresoMusicaSeekBar = (SeekBar) this.findViewById(R.id.barraProgreso);

		mireproductorTextView = (TextView) this.findViewById(R.id.titulo);
		nombreMusica = (TextView) this.findViewById(R.id.numeros);
		numMusicasTextView = (TextView) this
				.findViewById(R.id.numMusucasTextView);

		vistaView.setBackgroundColor(Color.GREEN);

		reproducirButtom.setOnClickListener(this);
		siguienteirButtom.setOnClickListener(this);
		anteriorButtom.setOnClickListener(this);
		detenerButtom.setOnClickListener(this);
		listarButton.setOnClickListener(this);

		listaMusicasListView.setOnItemClickListener(this);
		

		progresoMusicaSeekBar.setOnSeekBarChangeListener(this);
		

		palyList = new AndroidFileIO(getAssets());

		try {
			MusicasArray = (listar.load(palyList));

			
			if (MusicasArray.length != 0) {	
				cargarlista();
			}

			cargarMusica(android.net.Uri.parse(MusicasArray[0].toString()));

		} catch (Exception e) {

		}

		setVolumeControlStream(AudioManager.STREAM_MUSIC);// controles de sonido 

	}

	// -----------------------------------------

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int posicion,
			long arg3) {
		// TODO Auto-generated method stub
		detener();
		detener();
		reproducir(posicion);

//		Toast t = Toast.makeText(this, "" + posicion, Toast.LENGTH_LONG + 10);
//		t.show();

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		if (v == reproducirButtom) {
			if (reproducirButtom.getText().equals("Reproducir |►")) {
				reproducir(contadorRoductor);
			} else {
				pausar();
			}

		} else if (v == anteriorButtom) {

			anterior(contadorRoductor);

		} else if (v == siguienteirButtom) {

			sigiente(contadorRoductor);

		} else if (v == detenerButtom) {
			detener();

		} else if (v == listarButton) {
			detener();

			Intent t = Listar.cargarArchivos();

			startActivityForResult(t, 0);

		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// Metodo se utiliza cunado queremos un resultado al finalizar
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == RESULT_OK) {
			
			boolean esta = false;

			Uri nuevoUri = data.getData();

			
			for (int i = 0; i < MusicasArray.length; i++) {
				if (nuevoUri.equals(MusicasArray[i])) {
					esta = true;
				}	
			}

			if (esta == false) {
				AgreagarMusica(nuevoUri);
				
				mensajes("Se ha cargado " + nuevoUri.getLastPathSegment()
								+ " al Reproductor.", 5);
			} else {
				
				mensajes("La musica fue agregada anteriormente \nArgrege una nueva", 5);
			}

		
			cargarlista();
			cargarMusica(nuevoUri);

		} else {
			progresoMusicaSeekBar.setProgress(0);
		}
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		// TODO Auto-generated method stub

		if (MusicasArray.length!=0) {
			if (mediaPlayer != null) {
				if (mediaPlayer.isPlaying()) {
					if (progresoMusicaSeekBar.isPressed()) {
						mediaPlayer.seekTo(progress);
						reproducir(contadorRoductor);
					}

				} else {
					mediaPlayer.seekTo(progress);

				}

			} else {
				cargarMusica((Uri) MusicasArray[contadorRoductor]);
				mediaPlayer.seekTo(progress);
				reproducir(contadorRoductor);
				pausar();
			}

		} else {
			
			mensajes("carger musicas por favor..\n TOcando el Boton 'Listar' .", 5);
		}

	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onCompletion(MediaPlayer mp) {
		// TODO Auto-generated method stub
		// Se le llama cuando se llega al final de un medio de comunicación
		// durante la reproducción.

		detener();
		sigiente(contadorRoductor);

	}

	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		detener();
		if(MusicasArray.length>0){
			listar.save(palyList, MusicasArray);
		}
		
		if (mediaPlayer != null) {
			mediaPlayer.release();
		}

	}

	// --------------metodos----------------------------

	public void cargarlista() {

		ArrayAdapter<String> adaptador;
		ArrayList<String> datos = new ArrayList<String>();

		ordenarListaPOrNombre(MusicasArray);// Ordena por nombre

		for (int i = 0; i < MusicasArray.length; i++) {
			String dato = (((Uri) MusicasArray[i]).getLastPathSegment());
			datos.add(dato);

		}

		adaptador = new ArrayAdapter<String>(this,
				android.R.layout.simple_expandable_list_item_1, datos);

		listaMusicasListView.setAdapter(adaptador);

		numMusicasTextView.setText("Musicas: "
				+ listaMusicasListView.getCount());
		
		listar.save(palyList, MusicasArray);
		
	}

	public void reproducir(int p) {
		// TODO Auto-generated method stub
		super.onStart();
		if (MusicasArray.length==0) {
			
			mensajes("Aun no se an cargado las musicas",0);
			return;
		}

		if (mediaPlayer == null) {
			try{
			cargarMusica((Uri) MusicasArray[p]);
			}catch(Exception e){				
				mensajes("El archivo no se encuentra", 5);
				sigiente(p);
			}

		}

		mediaPlayer.start();// metodo que inicia la reproduccion

		reproducirButtom.setText("Pause ||");

		contadorRoductor = p;

		if (hm== null) {
			hm = new hiloMusica(progresoMusicaSeekBar, mediaPlayer);
			hp=new HiloPantalla(vistaView);

		}
	}

	public void detener() {
		// TODO Auto-generated method stub
		super.onStop();
		if (MusicasArray.length==0) {
			
			mensajes("Aun no se an cargado las musicas", 0);
			return;
		}
		if (mediaPlayer != null) {

			if (hm != null) {
				hm.matarHilo();
				hp.matarHilo();
				hm=null;
				hp=null;

			}

			mediaPlayer.stop();
			mediaPlayer = null;

		}

		progresoMusicaSeekBar.setProgress(0);
		reproducirButtom.setText("Reproducir |►");

		// --------------
		

	}

	public void pausar() {
		super.onPause();
		mediaPlayer.pause();// pausa la musica
		reproducirButtom.setText("Reproducir |►");

	}

	public void sigiente(int p) {

		if (MusicasArray.length > 1) {
			p += 1;

			if (p > MusicasArray.length - 1) {
				contadorRoductor = p = 0;
				detener();
				reproducir(p);

			} else {
				contadorRoductor = p;

				detener();
				reproducir(p);

			}

		}else{
			mensajes("No hay mas musicas", 5);
		}

	}

	public void anterior(int p) {

		if (MusicasArray.length > 1) {
			p -= 1;

			if (p < 0) {
				contadorRoductor = p = MusicasArray.length - 1;
				detener();
				reproducir(p);
			} else {
				contadorRoductor = p;
				detener();
				reproducir(p);

			}
		}else{
			mensajes("No hay mas musicas", 5);
		}

	}

	public void cargarMusica(Uri uri) {
		mediaPlayer = MediaPlayer.create(this, uri);// direccio del recurso
		mediaPlayer.setOnCompletionListener(this);// escucha de nuestro
		mediaPlayer.setVolume(2, 2);
		mediaPlayer.seekTo(0);
		progresoMusicaSeekBar.setMax(mediaPlayer.getDuration());
		progresoMusicaSeekBar.setProgress(0);
		nombreMusica.setText("Reproduciendo: " + uri.getLastPathSegment()
				+ " ♪");

	}

	public void ordenarListaPOrNombre(Object[] x) {
		for (int i = 0; i < x.length; i++) {
			try {
				if ((((Uri) x[i]).getLastPathSegment()
						.compareTo(((Uri) x[i + 1]).getLastPathSegment())) == 1) {

					Object a = x[i + 1];
					x[i + 1] = x[i];
					x[i] = a;
				}
			} catch (Exception e) {

			}
		}
	}

	private void mensajes(String mensaje, int duracion) {
		Toast t = Toast.makeText(this, mensaje, Toast.LENGTH_LONG + duracion);
		t.show();
	}

	private void AgreagarMusica(Uri direccion) {
		Uri[] aux = new Uri[MusicasArray.length];
		for (int i = 0; i < MusicasArray.length; i++) {
			aux[i] = MusicasArray[i];
		}

		MusicasArray = new Uri[aux.length + 1];

		for (int i = 0; i < aux.length; i++) {
			MusicasArray[i] = aux[i];
		}

		MusicasArray[aux.length] = direccion;

	}
}