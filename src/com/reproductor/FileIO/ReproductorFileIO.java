package com.reproductor.FileIO;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.res.AssetManager;
import android.os.Environment;

public class ReproductorFileIO implements FileIO{
	AssetManager assets;
	String rutaAlmacenamientoExterno;
	
	
	public ReproductorFileIO(AssetManager asset) {
		// TODO Auto-generated constructor stub
		this.assets = asset;
		this.rutaAlmacenamientoExterno = Environment
				.getExternalStorageDirectory().getAbsolutePath()
				+ File.separator;
	}

	@Override
	public InputStream leerAsset(String nombreArchivo) throws IOException {
		// TODO Auto-generated method stub
		return assets.open(nombreArchivo);
	}

	@Override
	public InputStream leerArchivo(String nombreArchivo) throws IOException {
		// TODO Auto-generated method stub
		return new FileInputStream(rutaAlmacenamientoExterno + nombreArchivo);
	}
	
	@Override
	public OutputStream escribirArchivo(String nombreArchivo)
			throws IOException {
		// TODO Auto-generated method stub
		return new FileOutputStream(rutaAlmacenamientoExterno+nombreArchivo);
	} 

}
