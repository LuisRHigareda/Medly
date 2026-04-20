package util;

import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author Leonardo Flores Leyva - 252390
 */
public class GeneradorReferencia {
    
    private static final AtomicInteger contador = new AtomicInteger(0);

    public static Long generarFolioUnico() {
        
        // Obtenemos el tiempo actual en milisegundos
        long tiempo = System.currentTimeMillis();
        
        // Un contador cíclico (de 0 a 999) para manejar peticiones en el mismo milisegundo
        int consecutivo = contador.incrementAndGet() % 1000;
        
        // Multiplicamos el tiempo para hacer espacio para el consecutivo
        return (tiempo * 1000) + consecutivo;
    }
}