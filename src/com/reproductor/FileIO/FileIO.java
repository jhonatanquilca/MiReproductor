package com.reproductor.FileIO;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface FileIO {


		//leer y escribir archivo
		public InputStream leerAsset(String fileName) throws IOException;
		
		public InputStream leerArchivo(String fileName) throws IOException;
		//se realizara desde la tarjeta de memoria
		public OutputStream escribirArchivo(String nombreArchivo) throws IOException;
		
		
	


}
