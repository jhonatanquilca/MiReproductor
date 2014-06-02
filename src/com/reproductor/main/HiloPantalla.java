package com.reproductor.main;

import java.util.Random;

import android.graphics.Color;
import android.media.MediaPlayer;
import android.view.View;
import android.widget.SeekBar;

public class HiloPantalla extends Thread{
	
	View v;
	Random rand;
	boolean vida;

	// int[] colores = new int[] { Color.BLACK, Color.BLUE, Color.CYAN,
	// Color.DKGRAY, Color.GREEN, Color.RED, Color.YELLOW,
	// Color.rgb(230, 0, 255), Color.rgb(0, 255, 119),Color.rgb(255, 51, 0)};

	public HiloPantalla(View view) {
		// TODO Auto-generated constructor stub
		
		this.start();
		v = view;
		vida = true;
		rand = new Random();
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();

		while (vida == true) {

			try {			
					
					v.invalidate();
					 v.setBackgroundColor(Color.rgb(rand.nextInt(256),
					 rand.nextInt(256), rand.nextInt(256)));

					 this.sleep(250);

					if (vida == false) {						
						return;
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
