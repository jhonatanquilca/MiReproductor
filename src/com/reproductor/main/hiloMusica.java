package com.reproductor.main;

import java.util.Random;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.view.View;
import android.widget.SeekBar;

public class hiloMusica extends Thread {

	SeekBar barra;
	MediaPlayer mp;
	
	Random rand;
	boolean vida;


	public hiloMusica(SeekBar s, MediaPlayer m) {
		// TODO Auto-generated constructor stub
		this.barra = s;
		this.mp = m;
		this.start();
		
		vida = true;
		rand = new Random();
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();

		while (vida == true) {

			try {
					

					 this.sleep(1000);

					if (vida == false) {
						mp.stop();
						return;
					}

					

				

				if (mp.getCurrentPosition() < mp.getDuration()) {
					barra.setProgress(mp.getCurrentPosition());
				}

			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO: handle exception
			}

		}
	}

	public void matarHilo() {
		vida = false;
	}

	public boolean esstaenEjecucion() {
		return this.isAlive();
	}

	

}
