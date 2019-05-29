/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tareaexamen;

import java.io.FileInputStream;
import java.io.InputStream;

/**
 *
 * @author root
 */
public class TareaExamen {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        InputStream is = null;
        int i;
        char c;
        int[] suma = {0, 0};
        try {
            // new input stream created
            is = new FileInputStream("/home/netbeans10/Documento");

            System.out.println("Characters printed:");

            // reads till the end of the stream
            while (true) {
                // converts integer to character
                if ((i = is.read()) == -1) {
                    break;
                }
                c = (char) i;
                // prints character       
                //System.out.print(c);
                System.out.println(c + "   " + i);
                suma[0] = (suma[0] + i) & 0b11111111;
                //System.out.println("");
                //break;

                if ((i = is.read()) == -1) {
                    break;
                }
                c = (char) i;
                // prints character       
                System.out.println(c + "   " + i);
                suma[1] = (suma[1] + i) & 0b11111111;
                //break;

            }
            suma[0] = (suma[0] * suma[1]) & 0b11111111;
            suma[1] = (suma[1] * suma[0]) & 0b11111111;
            int resultado = (((256* suma[1]))  + suma[0] ) ;  //recuerda que el resultado es un entero de 16 bits
            System.out.println(String.format("%x", suma[0]) + " " + String.format("%x", suma[1])   + " " + String.format("%x", resultado));
            System.out.println("resultado en decimal"+ resultado);
        } catch (Exception e) {
            // if any I/O error occurs
            e.printStackTrace();
        } finally {
            // releases system resources associated with this stream
            if (is != null) {
                is.close();
            }
        }
    }

}
