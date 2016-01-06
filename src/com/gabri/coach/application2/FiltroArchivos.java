package com.gabri.coach.application2;

import java.io.File;
import java.io.FilenameFilter;


public class FiltroArchivos implements FilenameFilter 
{
	@Override
	public boolean accept(File directorio, String nombreArchivo) 
	{
		/*Devuelve true si el final del archivo coincide con alguna de las extensiones que le indicamos, 
		* en caso contrario devuelve false y no se tiene en cuenta el archivo
		*/
		if(nombreArchivo.endsWith(".dat") || nombreArchivo.endsWith(".bmp") || nombreArchivo.endsWith(".jpg") || nombreArchivo.endsWith(".png") || nombreArchivo.endsWith(".gif"))
			return true;
		
		return false;
	}
}